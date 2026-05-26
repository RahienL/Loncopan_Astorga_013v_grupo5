package BookHub.msrecomendaciones.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecomendacionRequest(
    @NotNull Long usuarioId,
    @NotNull Long productoId,
    @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double puntaje,
    @NotBlank @Size(max = 255) String motivo
) {
}
