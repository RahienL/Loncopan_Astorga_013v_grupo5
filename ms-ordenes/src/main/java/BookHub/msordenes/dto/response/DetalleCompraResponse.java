package BookHub.msordenes.dto.response;

import java.math.BigDecimal;

import BookHub.msordenes.entities.DetalleCompra;

public record DetalleCompraResponse(
    Long id,
    Long productoId,
    String productoNombre,
    String productoImagen,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {
    public static DetalleCompraResponse from(DetalleCompra d) {
        return new DetalleCompraResponse(
            d.getId(), d.getProductoId(), d.getProductoNombre(),
            d.getProductoImagen(), d.getCantidad(), d.getPrecioUnitario(),
            d.getSubtotal()
        );
    }
}
