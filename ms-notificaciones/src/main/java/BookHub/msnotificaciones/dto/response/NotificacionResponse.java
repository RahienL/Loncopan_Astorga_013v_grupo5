package BookHub.msnotificaciones.dto.response;

import BookHub.msnotificaciones.entities.Notificacion;

import java.time.LocalDateTime;

public record NotificacionResponse(
    Long id,
    Long usuarioId,
    String emailDestino,
    String asunto,
    String mensaje,
    String tipo,
    String estado,
    Boolean leida,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaEnvio
) {

    public static NotificacionResponse from(Notificacion notificacion) {
        return new NotificacionResponse(
            notificacion.getId(),
            notificacion.getUsuarioId(),
            notificacion.getEmailDestino(),
            notificacion.getAsunto(),
            notificacion.getMensaje(),
            notificacion.getTipo(),
            notificacion.getEstado(),
            notificacion.getLeida(),
            notificacion.getFechaCreacion(),
            notificacion.getFechaEnvio()
        );
    }
}
