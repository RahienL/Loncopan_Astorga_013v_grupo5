package BookHub.msinventario.dto.response;

import BookHub.msinventario.entities.Inventario;

import java.time.LocalDateTime;

public record InventarioResponse(
    Long id,
    Long productoId,
    Integer stock,
    Integer stockMinimo,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion,
    boolean activo
) {

    public static InventarioResponse from(Inventario inventario) {
        return new InventarioResponse(
            inventario.getId(),
            inventario.getProductoId(),
            inventario.getStock(),
            inventario.getStockMinimo(),
            inventario.getEstado(),
            inventario.getFechaCreacion(),
            inventario.getFechaActualizacion(),
            inventario.getActivo()
        );
    }
}
