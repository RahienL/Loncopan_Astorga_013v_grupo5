package BookHub.msresenas.dto.response;

import BookHub.msresenas.entities.Resena;

import java.time.LocalDateTime;

public record ResenaResponse(
    Long id,
    Long productoId,
    Long usuarioId,
    String titulo,
    String comentario,
    Integer calificacion,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {

    public static ResenaResponse from(Resena resena) {
        return new ResenaResponse(
            resena.getId(),
            resena.getProductoId(),
            resena.getUsuarioId(),
            resena.getTitulo(),
            resena.getComentario(),
            resena.getCalificacion(),
            resena.getEstado(),
            resena.getFechaCreacion(),
            resena.getFechaActualizacion()
        );
    }
}
