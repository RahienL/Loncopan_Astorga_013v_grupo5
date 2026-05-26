package BookHub.msusuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
    @NotBlank @Size(min = 2, max = 100) String nombre,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(min = 6) String password,
    @Pattern(regexp = "^(cliente|vendedor|superadmin)$") String rol
) {}
