package BookHub.mscatalogo.services;

import BookHub.mscatalogo.entities.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductoService {
    Producto crear(Producto producto);
    Producto obtenerPorId(Long id);
    List<Producto> listarActivos();
    List<Producto> listarTodos();
    Producto actualizar(Long id, Producto productoActualizado);
    void eliminar(Long id);
    Producto cambiarEstado(Long id, String nuevoEstado);
    Producto actualizarImagen(Long id, String nuevaImagenUrl);
    Producto actualizarStock(Long id, Integer nuevoStock);
    List<Producto> listarProductosConStockBajo();
    List<Producto> listarProductosSinStock();
    String guardarImagenProducto(MultipartFile imagen, String nombreArchivo) throws IOException;
    // Usado por ms-ordenes para verificar y descontar stock
    boolean verificarYDescontarStock(Long productoId, Integer cantidad);
}
