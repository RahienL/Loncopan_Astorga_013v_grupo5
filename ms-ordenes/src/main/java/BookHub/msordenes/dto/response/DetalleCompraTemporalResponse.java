package BookHub.msordenes.dto.response;

import java.math.BigDecimal;

import BookHub.msordenes.entities.DetalleCompraTemporal;

public record DetalleCompraTemporalResponse(
    Long id,
    Long productoId,
    String productoNombre,
    String productoImagen,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {
    public static DetalleCompraTemporalResponse from(DetalleCompraTemporal d) {
        return new DetalleCompraTemporalResponse(
            d.getId(), d.getProductoId(), d.getProductoNombre(),
            d.getProductoImagen(), d.getCantidad(), d.getPrecioUnitario(),
            d.getSubtotal()
        );
    }
}
