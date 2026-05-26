package BookHub.msordenes.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import BookHub.msordenes.entities.CompraTemporal;

public record CompraTemporalDto(
    Long id,
    Long usuarioId,
    String usuarioNombre,
    BigDecimal total,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaExpiracion,
    List<DetalleCompraTemporalResponse> detalles
) {
    public static CompraTemporalDto from(CompraTemporal ct) {
        return new CompraTemporalDto(
            ct.getId(), ct.getUsuarioId(), ct.getUsuarioNombre(),
            ct.getTotal(), ct.getEstado(), ct.getFechaCreacion(),
            ct.getFechaExpiracion(),
            ct.getDetalles().stream().map(DetalleCompraTemporalResponse::from).toList()
        );
    }
}
