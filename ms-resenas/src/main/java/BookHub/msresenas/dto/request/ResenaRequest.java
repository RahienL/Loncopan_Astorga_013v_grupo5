package BookHub.msresenas.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResenaRequest(
    @NotNull Long productoId,
    @NotNull Long usuarioId,
    @NotBlank @Size(max = 200) String titulo,
    @NotBlank @Size(max = 2000) String comentario,
    @NotNull @Min(1) @Max(5) Integer calificacion
) {
}
