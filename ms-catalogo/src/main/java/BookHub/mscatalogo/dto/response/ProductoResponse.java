package BookHub.mscatalogo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import BookHub.mscatalogo.entities.Producto;

public record ProductoResponse(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    Long categoriaId,
    String categoriaNombre,
    String imagenUrl,
    String estado,
    LocalDateTime fechaCreacion,
    boolean activo
) {
    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(
            p.getId(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getStock(),
            p.getCategoria() != null ? p.getCategoria().getId() : null,
            p.getCategoria() != null ? p.getCategoria().getNombre() : null,
            p.getImagenUrl(),
            p.getEstado(),
            p.getFechaCreacion(),
            p.getActivo()
        );
    }
}
