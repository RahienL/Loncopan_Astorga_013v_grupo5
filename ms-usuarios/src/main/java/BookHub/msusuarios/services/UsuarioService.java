package BookHub.msusuarios.services;

import BookHub.msusuarios.entities.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario obtenerPorId(Long id);
    List<Usuario> listarTodos();
    Usuario actualizar(Long id, Usuario usuarioActualizado);
    void eliminar(Long id);
    Usuario cambiarEstado(Long id, String nuevoEstado);
}
