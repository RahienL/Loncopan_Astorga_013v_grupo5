package BookHub.msordenes.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompraTemporalRequest(
    @NotNull Long usuarioId,
    @Size(max = 200) String usuarioNombre,
    @NotNull @Valid List<ItemRequest> items
) {
    public record ItemRequest(
        @NotNull Long productoId,
        @NotBlank @Size(min = 2, max = 200) String productoNombre,
        @Size(max = 500) String productoImagen,
        @NotNull @jakarta.validation.constraints.DecimalMin(value = "0.0", inclusive = false) java.math.BigDecimal precioUnitario,
        @NotNull @Min(1) Integer cantidad
    ) {}
}
