package BookHub.msnotificaciones.services;

import BookHub.msnotificaciones.entities.Notificacion;

import java.util.List;

public interface NotificacionService {

    Notificacion crear(Notificacion notificacion);

    Notificacion obtenerPorId(Long id);

    List<Notificacion> listarTodas();

    List<Notificacion> listarPendientes();

    List<Notificacion> listarPorUsuario(Long usuarioId);

    List<Notificacion> listarNoLeidasPorUsuario(Long usuarioId);

    Notificacion cambiarEstado(Long id, String estado);

    Notificacion marcarComoLeida(Long id);

    void eliminar(Long id);
}
