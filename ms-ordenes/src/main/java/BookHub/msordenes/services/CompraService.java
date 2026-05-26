package BookHub.msordenes.services;

import BookHub.msordenes.dto.request.CompraTemporalRequest;
import BookHub.msordenes.entities.Compra;
import BookHub.msordenes.entities.CompraTemporal;

import java.util.List;

public interface CompraService {
    CompraTemporal crearCompratemporal(CompraTemporalRequest request);
    CompraTemporal obtenerCarritoActivoDeUsuario(Long usuarioId);
    CompraTemporal agregarItemAlCarrito(Long compraTemporalId, CompraTemporalRequest.ItemRequest item);
    CompraTemporal eliminarItemDelCarrito(Long compraTemporalId, Long detalleId);
    Compra confirmarCompra(Long compraTemporalId, Long usuarioId, String usuarioNombre, String usuarioEmail, String metodoPago);
    List<Compra> obtenerComprasPorUsuario(Long usuarioId);
    Compra obtenerCompraPorId(Long id);
    List<Compra> listarTodasLasCompras();
    byte[] generarFacturaPdf(Long compraId);
}
