package BookHub.msrecomendaciones.dto.response;

import BookHub.msrecomendaciones.entities.Recomendacion;

import java.time.LocalDateTime;

public record RecomendacionResponse(
    Long id,
    Long usuarioId,
    Long productoId,
    Double puntaje,
    String motivo,
    String estado,
    LocalDateTime creadaEn,
    LocalDateTime fechaActualizacion
) {

    public static RecomendacionResponse from(Recomendacion recomendacion) {
        return new RecomendacionResponse(
            recomendacion.getId(),
            recomendacion.getUsuarioId(),
            recomendacion.getProductoId(),
            recomendacion.getPuntaje(),
            recomendacion.getMotivo(),
            recomendacion.getEstado(),
            recomendacion.getCreadaEn(),
            recomendacion.getFechaActualizacion()
        );
    }
}
