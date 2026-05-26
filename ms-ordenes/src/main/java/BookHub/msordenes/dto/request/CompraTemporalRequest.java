package BookHub.msordenes.dto.request;

import java.util.List;

public record CompraTemporalRequest(
    Long usuarioId,
    String usuarioNombre,
    List<ItemRequest> items
) {
    public record ItemRequest(
        Long productoId,
        String productoNombre,
        String productoImagen,
        java.math.BigDecimal precioUnitario,
        Integer cantidad
    ) {}
}
