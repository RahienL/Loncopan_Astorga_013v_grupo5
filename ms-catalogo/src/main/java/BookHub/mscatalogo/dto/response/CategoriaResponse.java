package BookHub.mscatalogo.dto.response;

import BookHub.mscatalogo.entities.Categoria;

public record CategoriaResponse(
    Long id,
    String nombre,
    String descripcion
) {
    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
