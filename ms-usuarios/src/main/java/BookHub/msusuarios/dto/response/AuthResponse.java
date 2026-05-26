package BookHub.msusuarios.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    boolean success,
    String message,
    String token,
    UserInfo user
) {
    public AuthResponse(String token, UserInfo user) {
        this(true, "Login exitoso", token, user);
    }

    public AuthResponse(String message) {
        this(false, message, null, null);
    }

    public record UserInfo(
        Long id, String nombre, String email, String rol, String estado,
        LocalDateTime fechaCreacion, boolean isAdmin
    ) {
        public UserInfo(Long id, String nombre, String email, String rol, String estado, LocalDateTime fechaCreacion) {
            this(id, nombre, email, rol, estado, fechaCreacion,
                 "superadmin".equals(rol) || "vendedor".equals(rol));
        }
    }
}
