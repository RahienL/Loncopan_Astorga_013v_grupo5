package BookHub.msordenes.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookHub.msordenes.dto.request.CompraTemporalRequest;
import BookHub.msordenes.dto.response.CompraDto;
import BookHub.msordenes.dto.response.CompraResponse;
import BookHub.msordenes.dto.response.CompraTemporalDto;
import BookHub.msordenes.services.CompraService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    /** Crear o iniciar un carrito temporal */
    @PostMapping("/temporal")
    public ResponseEntity<CompraTemporalDto> crearCarrito(@Valid @RequestBody CompraTemporalRequest request) {
        return ResponseEntity.ok(CompraTemporalDto.from(compraService.crearCompratemporal(request)));
    }

    /** Obtener el carrito activo de un usuario */
    @GetMapping("/temporal/usuario/{usuarioId}")
    public ResponseEntity<CompraTemporalDto> obtenerCarrito(@PathVariable Long usuarioId) {
        var ct = compraService.obtenerCarritoActivoDeUsuario(usuarioId);
        if (ct == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CompraTemporalDto.from(ct));
    }

    /** Agregar un ítem al carrito */
    @PostMapping("/temporal/{carritoId}/items")
    public ResponseEntity<CompraTemporalDto> agregarItem(@PathVariable Long carritoId,
                                                          @Valid @RequestBody CompraTemporalRequest.ItemRequest item) {
        return ResponseEntity.ok(CompraTemporalDto.from(compraService.agregarItemAlCarrito(carritoId, item)));
    }

    /** Eliminar un ítem del carrito */
    @DeleteMapping("/temporal/{carritoId}/items/{detalleId}")
    public ResponseEntity<CompraTemporalDto> eliminarItem(@PathVariable Long carritoId,
                                                           @PathVariable Long detalleId) {
        return ResponseEntity.ok(CompraTemporalDto.from(compraService.eliminarItemDelCarrito(carritoId, detalleId)));
    }

    /** Confirmar la compra (llama a ms-catalogo para verificar/descontar stock) */
    @PostMapping("/temporal/{carritoId}/confirmar")
    public ResponseEntity<CompraResponse> confirmarCompra(@PathVariable Long carritoId,
                                                           @RequestBody Map<String, String> body,
                                                           Authentication auth) {
        try {
            Long usuarioId = Long.parseLong(body.getOrDefault("usuarioId", "0"));
            String usuarioNombre = body.getOrDefault("usuarioNombre", "");
            String usuarioEmail = body.getOrDefault("usuarioEmail", auth != null ? auth.getName() : "");
            String metodoPago = body.getOrDefault("metodoPago", "efectivo");
            var compra = compraService.confirmarCompra(carritoId, usuarioId, usuarioNombre, usuarioEmail, metodoPago);
            return ResponseEntity.ok(new CompraResponse(true, "Compra confirmada", CompraDto.from(compra)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CompraResponse(false, e.getMessage(), null));
        }
    }

    /** Historial de compras de un usuario */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CompraDto>> comprasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(compraService.obtenerComprasPorUsuario(usuarioId).stream()
                .map(CompraDto::from).toList());
    }

    /** Obtener una compra por ID */
    @GetMapping("/{id}")
    public ResponseEntity<CompraDto> obtenerCompraPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CompraDto.from(compraService.obtenerCompraPorId(id)));
    }

    /** Listar todas las compras (admin) */
    @GetMapping
    public ResponseEntity<List<CompraDto>> listarTodasLasCompras() {
        return ResponseEntity.ok(compraService.listarTodasLasCompras().stream()
                .map(CompraDto::from).toList());
    }

    /** Descargar factura PDF */
    @GetMapping("/{id}/factura")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long id) {
        byte[] pdf = compraService.generarFacturaPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
