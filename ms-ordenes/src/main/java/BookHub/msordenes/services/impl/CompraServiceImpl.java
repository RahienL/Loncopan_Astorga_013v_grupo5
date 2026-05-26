package BookHub.msordenes.services.impl;

import BookHub.msordenes.services.CompraService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import BookHub.msordenes.dto.request.CompraTemporalRequest;
import BookHub.msordenes.entities.*;
import BookHub.msordenes.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final CompraTemporalRepository compraTemporalRepository;
    private final RestTemplate restTemplate;

    @Value("${ms.catalogo.url:http://localhost:8082}")
    private String msCatalogoUrl;

    public CompraServiceImpl(CompraRepository compraRepository,
                              CompraTemporalRepository compraTemporalRepository,
                              RestTemplate restTemplate) {
        this.compraRepository = compraRepository;
        this.compraTemporalRepository = compraTemporalRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public CompraTemporal crearCompratemporal(CompraTemporalRequest request) {
        CompraTemporal ct = new CompraTemporal();
        ct.setUsuarioId(request.usuarioId());
        ct.setUsuarioNombre(request.usuarioNombre() != null ? request.usuarioNombre() : "");
        ct.setEstado("activo");
        List<DetalleCompraTemporal> detalles = new ArrayList<>();
        if (request.items() != null) {
            for (CompraTemporalRequest.ItemRequest item : request.items()) {
                DetalleCompraTemporal d = buildDetalleTemporal(ct, item);
                detalles.add(d);
            }
        }
        ct.setDetalles(detalles);
        ct.setTotal(calcularTotal(detalles));
        return compraTemporalRepository.save(ct);
    }

    @Override
    public CompraTemporal obtenerCarritoActivoDeUsuario(Long usuarioId) {
        return compraTemporalRepository.findByUsuarioIdAndEstado(usuarioId, "activo")
                .orElse(null);
    }

    @Override
    @Transactional
    public CompraTemporal agregarItemAlCarrito(Long compraTemporalId, CompraTemporalRequest.ItemRequest item) {
        CompraTemporal ct = compraTemporalRepository.findById(compraTemporalId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + compraTemporalId));
        ct.getDetalles().add(buildDetalleTemporal(ct, item));
        ct.setTotal(calcularTotal(ct.getDetalles()));
        return compraTemporalRepository.save(ct);
    }

    @Override
    @Transactional
    public CompraTemporal eliminarItemDelCarrito(Long compraTemporalId, Long detalleId) {
        CompraTemporal ct = compraTemporalRepository.findById(compraTemporalId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + compraTemporalId));
        ct.getDetalles().removeIf(d -> d.getId().equals(detalleId));
        ct.setTotal(calcularTotal(ct.getDetalles()));
        return compraTemporalRepository.save(ct);
    }

    @Override
    @Transactional
    public Compra confirmarCompra(Long compraTemporalId, Long usuarioId,
                                   String usuarioNombre, String usuarioEmail, String metodoPago) {
        CompraTemporal ct = compraTemporalRepository.findById(compraTemporalId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado: " + compraTemporalId));

        if (!"activo".equals(ct.getEstado())) {
            throw new RuntimeException("El carrito ya fue procesado o expiró");
        }

        // Verificar y descontar stock en ms-catalogo para cada ítem
        for (DetalleCompraTemporal d : ct.getDetalles()) {
            Map<String, Integer> body = Map.of("cantidad", d.getCantidad());
            String url = msCatalogoUrl + "/api/productos/" + d.getProductoId() + "/descontar-stock";
            try {
                ResponseEntity<Map> resp = restTemplate.postForEntity(url, body, Map.class);
                if (!resp.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Stock insuficiente para producto: " + d.getProductoNombre());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al verificar stock para '" + d.getProductoNombre() + "': " + e.getMessage());
            }
        }

        // Crear compra confirmada
        Compra compra = new Compra();
        compra.setUsuarioId(usuarioId);
        compra.setUsuarioNombre(usuarioNombre);
        compra.setUsuarioEmail(usuarioEmail);
        compra.setEstado("confirmada");
        compra.setMetodoPago(metodoPago);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setNumeroFactura("FAC-" + System.currentTimeMillis());

        List<DetalleCompra> detalles = ct.getDetalles().stream().map(d -> {
            DetalleCompra dc = new DetalleCompra();
            dc.setCompra(compra);
            dc.setProductoId(d.getProductoId());
            dc.setProductoNombre(d.getProductoNombre());
            dc.setProductoImagen(d.getProductoImagen());
            dc.setCantidad(d.getCantidad());
            dc.setPrecioUnitario(d.getPrecioUnitario());
            return dc;
        }).toList();

        compra.setDetalles(detalles);
        compra.setTotal(calcularTotalCompra(detalles));

        ct.setEstado("confirmado");
        compraTemporalRepository.save(ct);

        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> obtenerComprasPorUsuario(Long usuarioId) {
        return compraRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
    }

    @Override
    public List<Compra> listarTodasLasCompras() {
        return compraRepository.findAll();
    }

    @Override
    public byte[] generarFacturaPdf(Long compraId) {
        Compra compra = obtenerCompraPorId(compraId);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            Paragraph title = new Paragraph("BookHub - Factura", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("N° Factura: " + compra.getNumeroFactura(), headerFont));
            document.add(new Paragraph("Fecha: " + compra.getFechaCompra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
            document.add(new Paragraph("Cliente: " + compra.getUsuarioNombre(), normalFont));
            document.add(new Paragraph("Email: " + compra.getUsuarioEmail(), normalFont));
            document.add(new Paragraph("Método de pago: " + (compra.getMetodoPago() != null ? compra.getMetodoPago() : "N/A"), normalFont));
            document.add(new Paragraph(" "));

            // Tabla de productos
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{50, 15, 20, 15});

            String[] headers = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }

            for (DetalleCompra d : compra.getDetalles()) {
                table.addCell(new Phrase(d.getProductoNombre(), normalFont));
                table.addCell(new Phrase(String.valueOf(d.getCantidad()), normalFont));
                table.addCell(new Phrase("$" + d.getPrecioUnitario(), normalFont));
                table.addCell(new Phrase("$" + d.getSubtotal(), normalFont));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            Paragraph total = new Paragraph("TOTAL: $" + compra.getTotal(), headerFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    // Helpers
    private DetalleCompraTemporal buildDetalleTemporal(CompraTemporal ct, CompraTemporalRequest.ItemRequest item) {
        DetalleCompraTemporal d = new DetalleCompraTemporal();
        d.setCompraTemporal(ct);
        d.setProductoId(item.productoId());
        d.setProductoNombre(item.productoNombre());
        d.setProductoImagen(item.productoImagen());
        d.setCantidad(item.cantidad());
        d.setPrecioUnitario(item.precioUnitario());
        return d;
    }

    private BigDecimal calcularTotal(List<DetalleCompraTemporal> detalles) {
        return detalles.stream().map(DetalleCompraTemporal::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalCompra(List<DetalleCompra> detalles) {
        return detalles.stream().map(DetalleCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
