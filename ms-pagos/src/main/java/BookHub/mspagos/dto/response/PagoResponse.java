package BookHub.mspagos.dto.response;

import BookHub.mspagos.entities.Pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(
    Long id,
    Long compraId,
    Long usuarioId,
    BigDecimal monto,
    String metodoPago,
    String estado,
    String referenciaTransaccion,
    LocalDateTime fechaPago,
    LocalDateTime fechaActualizacion
) {

    public static PagoResponse from(Pago pago) {
        return new PagoResponse(
            pago.getId(),
            pago.getCompraId(),
            pago.getUsuarioId(),
            pago.getMonto(),
            pago.getMetodoPago(),
            pago.getEstado(),
            pago.getReferenciaTransaccion(),
            pago.getFechaPago(),
            pago.getFechaActualizacion()
        );
    }
}
