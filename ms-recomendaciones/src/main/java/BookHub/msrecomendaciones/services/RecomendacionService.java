package BookHub.msrecomendaciones.services;

import BookHub.msrecomendaciones.entities.Recomendacion;

import java.util.List;

public interface RecomendacionService {

    Recomendacion crear(Recomendacion recomendacion);

    Recomendacion obtenerPorId(Long id);

    List<Recomendacion> listarTodas();

    List<Recomendacion> listarPorUsuario(Long usuarioId);

    List<Recomendacion> listarPorProducto(Long productoId);

    List<Recomendacion> listarPorEstado(String estado);

    Recomendacion desactivar(Long id);

    void eliminar(Long id);
}
