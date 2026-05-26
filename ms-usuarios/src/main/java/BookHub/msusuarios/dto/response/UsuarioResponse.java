package BookHub.msusuarios.dto.response;

import java.time.LocalDateTime;

import BookHub.msusuarios.entities.Usuario;

public record UsuarioResponse(
    Long id,
    String nombre,
    String email,
    String rol,
    String estado,
    LocalDateTime fechaCreacion
) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
            u.getId(), u.getNombre(), u.getEmail(),
            u.getRol(), u.getEstado(), u.getFechaCreacion()
        );
    }
}
