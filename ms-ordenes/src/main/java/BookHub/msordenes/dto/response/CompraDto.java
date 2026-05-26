package BookHub.msordenes.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import BookHub.msordenes.entities.Compra;

public record CompraDto(
    Long id,
    Long usuarioId,
    String usuarioNombre,
    String usuarioEmail,
    BigDecimal total,
    String estado,
    String numeroFactura,
    String metodoPago,
    LocalDateTime fechaCompra,
    List<DetalleCompraResponse> detalles
) {
    public static CompraDto from(Compra c) {
        return new CompraDto(
            c.getId(), c.getUsuarioId(), c.getUsuarioNombre(), c.getUsuarioEmail(),
            c.getTotal(), c.getEstado(), c.getNumeroFactura(), c.getMetodoPago(),
            c.getFechaCompra(),
            c.getDetalles().stream().map(DetalleCompraResponse::from).toList()
        );
    }
}
