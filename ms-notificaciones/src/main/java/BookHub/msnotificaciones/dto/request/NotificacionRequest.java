package BookHub.msnotificaciones.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NotificacionRequest(
    @NotNull Long usuarioId,
    @NotBlank @Email @Size(max = 150) String emailDestino,
    @NotBlank @Size(max = 200) String asunto,
    @NotBlank @Size(max = 2000) String mensaje,
    @NotBlank @Pattern(regexp = "^(email|sms|push|sistema)$") String tipo
) {
}
