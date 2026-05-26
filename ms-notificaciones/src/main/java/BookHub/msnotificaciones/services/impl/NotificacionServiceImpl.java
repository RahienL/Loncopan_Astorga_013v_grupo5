package BookHub.msnotificaciones.services.impl;

import BookHub.msnotificaciones.entities.Notificacion;
import BookHub.msnotificaciones.repositories.NotificacionRepository;
import BookHub.msnotificaciones.services.NotificacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public Notificacion crear(Notificacion notificacion) {
        notificacion.setEstado("pendiente");
        notificacion.setLeida(false);
        notificacion.setFechaEnvio(null);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion obtenerPorId(Long id) {
        return notificacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con ID: " + id));
    }

    @Override
    public List<Notificacion> listarTodas() {
        return notificacionRepository.findAll();
    }

    @Override
    public List<Notificacion> listarPendientes() {
        return notificacionRepository.findPendientes();
    }

    @Override
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Override
    public List<Notificacion> listarNoLeidasPorUsuario(Long usuarioId) {
        return notificacionRepository.findNoLeidasPorUsuario(usuarioId);
    }

    @Override
    public Notificacion cambiarEstado(Long id, String estado) {
        if (!"pendiente".equals(estado) && !"enviada".equals(estado) && !"fallida".equals(estado)) {
            throw new RuntimeException("Estado invalido. Use 'pendiente', 'enviada' o 'fallida'.");
        }

        Notificacion notificacion = obtenerPorId(id);
        notificacion.setEstado(estado);
        if ("enviada".equals(estado)) {
            notificacion.setFechaEnvio(LocalDateTime.now());
        }
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = obtenerPorId(id);
        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public void eliminar(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificacion no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }
}
