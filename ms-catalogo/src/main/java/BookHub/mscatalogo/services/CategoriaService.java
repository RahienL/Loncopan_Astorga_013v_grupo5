package BookHub.mscatalogo.services;

import BookHub.mscatalogo.entities.Categoria;
import java.util.List;

public interface CategoriaService {
    Categoria crear(Categoria categoria);
    Categoria obtenerPorId(Long id);
    List<Categoria> listarTodas();
    Categoria actualizar(Long id, Categoria categoriaActualizada);
    void eliminar(Long id);
}
