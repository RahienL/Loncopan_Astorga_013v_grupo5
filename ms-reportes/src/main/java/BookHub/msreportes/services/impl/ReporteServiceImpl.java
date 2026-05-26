package BookHub.msreportes.services.impl;

import BookHub.msreportes.entities.Reporte;
import BookHub.msreportes.repositories.ReporteRepository;
import BookHub.msreportes.services.ReporteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteServiceImpl(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    public Reporte crear(Reporte reporte) {
        reporte.setFechaActualizacion(LocalDateTime.now());
        return reporteRepository.save(reporte);
    }

    @Override
    public Reporte obtenerPorId(Long id) {
        return reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
    }

    @Override
    public List<Reporte> listarTodos() {
        return reporteRepository.findAll();
    }

    @Override
    public List<Reporte> listarPorUsuario(Long usuarioId) {
        return reporteRepository.findByUsuarioIdOrderByGeneradoEnDesc(usuarioId);
    }

    @Override
    public List<Reporte> listarPorTipo(String tipo) {
        return reporteRepository.findByTipoOrderByGeneradoEnDesc(tipo);
    }

    @Override
    public void eliminar(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }
}
