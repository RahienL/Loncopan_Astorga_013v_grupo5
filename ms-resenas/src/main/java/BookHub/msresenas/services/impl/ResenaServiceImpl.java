package BookHub.msresenas.services.impl;

import BookHub.msresenas.entities.Resena;
import BookHub.msresenas.repositories.ResenaRepository;
import BookHub.msresenas.services.ResenaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    public Resena crear(Resena resena) {
        resena.setEstado("publicada");
        return resenaRepository.save(resena);
    }

    @Override
    public Resena obtenerPorId(Long id) {
        return resenaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Resena no encontrada con ID: " + id));
    }

    @Override
    public List<Resena> listarTodas() {
        return resenaRepository.findAll();
    }

    @Override
    public List<Resena> listarPublicadasPorProducto(Long productoId) {
        return resenaRepository.findByProductoIdAndEstadoOrderByFechaCreacionDesc(productoId, "publicada");
    }

    @Override
    public List<Resena> listarPorUsuario(Long usuarioId) {
        return resenaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Override
    public Resena actualizar(Long id, Resena resenaActualizada) {
        Resena resena = obtenerPorId(id);
        resena.setTitulo(resenaActualizada.getTitulo());
        resena.setComentario(resenaActualizada.getComentario());
        resena.setCalificacion(resenaActualizada.getCalificacion());
        return resenaRepository.save(resena);
    }

    @Override
    public Resena cambiarEstado(Long id, String estado) {
        if (!"publicada".equals(estado) && !"oculta".equals(estado)) {
            throw new RuntimeException("Estado invalido. Use 'publicada' u 'oculta'.");
        }
        Resena resena = obtenerPorId(id);
        resena.setEstado(estado);
        return resenaRepository.save(resena);
    }

    @Override
    public void eliminar(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Resena no encontrada con ID: " + id);
        }
        resenaRepository.deleteById(id);
    }
}
