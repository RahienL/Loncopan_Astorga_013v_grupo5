package BookHub.msreportes.services;

import BookHub.msreportes.entities.Reporte;

import java.util.List;

public interface ReporteService {

    Reporte crear(Reporte reporte);

    Reporte obtenerPorId(Long id);

    List<Reporte> listarTodos();

    List<Reporte> listarPorUsuario(Long usuarioId);

    List<Reporte> listarPorTipo(String tipo);

    void eliminar(Long id);
}
