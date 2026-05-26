package BookHub.msresenas.services;

import BookHub.msresenas.entities.Resena;

import java.util.List;

public interface ResenaService {

    Resena crear(Resena resena);

    Resena obtenerPorId(Long id);

    List<Resena> listarTodas();

    List<Resena> listarPublicadasPorProducto(Long productoId);

    List<Resena> listarPorUsuario(Long usuarioId);

    Resena actualizar(Long id, Resena resenaActualizada);

    Resena cambiarEstado(Long id, String estado);

    void eliminar(Long id);
}
