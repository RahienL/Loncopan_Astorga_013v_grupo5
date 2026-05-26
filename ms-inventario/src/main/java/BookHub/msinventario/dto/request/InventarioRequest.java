package BookHub.msinventario.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioRequest(
    @NotNull Long productoId,
    @NotNull @Min(0) Integer stock,
    @NotNull @Min(0) Integer stockMinimo,
    boolean activo
) {
}
