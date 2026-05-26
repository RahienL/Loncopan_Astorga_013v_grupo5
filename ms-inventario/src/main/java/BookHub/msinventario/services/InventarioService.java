package BookHub.msinventario.services;

import BookHub.msinventario.entities.Inventario;

import java.util.List;

public interface InventarioService {

    Inventario crear(Inventario inventario);

    Inventario obtenerPorId(Long id);

    Inventario obtenerPorProductoId(Long productoId);

    List<Inventario> listarActivos();

    List<Inventario> listarTodos();

    List<Inventario> listarConStockBajo();

    List<Inventario> listarSinStock();

    Inventario actualizar(Long id, Inventario inventarioActualizado);

    void eliminar(Long id);

    Inventario cambiarEstado(Long id, String nuevoEstado);

    Inventario actualizarStock(Long id, Integer nuevoStock);

    boolean verificarYDescontarStock(Long productoId, Integer cantidad);
}
