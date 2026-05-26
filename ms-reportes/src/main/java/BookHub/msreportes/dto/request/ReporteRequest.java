package BookHub.msreportes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReporteRequest(
    @NotNull Long usuarioId,
    @NotBlank @Size(max = 60) String tipo,
    @NotBlank @Size(max = 2000) String contenido
) {
}
