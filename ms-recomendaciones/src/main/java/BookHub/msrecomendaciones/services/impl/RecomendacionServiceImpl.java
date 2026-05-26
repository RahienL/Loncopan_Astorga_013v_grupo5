package BookHub.msrecomendaciones.services.impl;

import BookHub.msrecomendaciones.entities.Recomendacion;
import BookHub.msrecomendaciones.repositories.RecomendacionRepository;
import BookHub.msrecomendaciones.services.RecomendacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacionServiceImpl implements RecomendacionService {

    private final RecomendacionRepository recomendacionRepository;

    public RecomendacionServiceImpl(RecomendacionRepository recomendacionRepository) {
        this.recomendacionRepository = recomendacionRepository;
    }

    @Override
    public Recomendacion crear(Recomendacion recomendacion) {
        recomendacion.setEstado("activa");
        recomendacion.setFechaActualizacion(LocalDateTime.now());
        return recomendacionRepository.save(recomendacion);
    }

    @Override
    public Recomendacion obtenerPorId(Long id) {
        return recomendacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Recomendacion no encontrada con ID: " + id));
    }

    @Override
    public List<Recomendacion> listarTodas() {
        return recomendacionRepository.findAll();
    }

    @Override
    public List<Recomendacion> listarPorUsuario(Long usuarioId) {
        return recomendacionRepository.findByUsuarioIdOrderByPuntajeDesc(usuarioId);
    }

    @Override
    public List<Recomendacion> listarPorProducto(Long productoId) {
        return recomendacionRepository.findByProductoIdOrderByPuntajeDesc(productoId);
    }

    @Override
    public List<Recomendacion> listarPorEstado(String estado) {
        return recomendacionRepository.findByEstado(estado);
    }

    @Override
    public Recomendacion desactivar(Long id) {
        Recomendacion recomendacion = obtenerPorId(id);
        recomendacion.setEstado("inactiva");
        recomendacion.setFechaActualizacion(LocalDateTime.now());
        return recomendacionRepository.save(recomendacion);
    }

    @Override
    public void eliminar(Long id) {
        if (!recomendacionRepository.existsById(id)) {
            throw new RuntimeException("Recomendacion no encontrada con ID: " + id);
        }
        recomendacionRepository.deleteById(id);
    }
}
