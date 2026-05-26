package BookHub.mscatalogo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
    @NotBlank @Size(min = 2, max = 100) String nombre,
    @Size(max = 500) String descripcion
) {}
