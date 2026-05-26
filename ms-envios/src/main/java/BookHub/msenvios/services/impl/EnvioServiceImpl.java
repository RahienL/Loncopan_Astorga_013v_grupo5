package BookHub.msenvios.services.impl;

import BookHub.msenvios.entities.Envio;
import BookHub.msenvios.repositories.EnvioRepository;
import BookHub.msenvios.services.EnvioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;

    public EnvioServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public Envio crear(Envio envio) {
        envioRepository.findByCompraId(envio.getCompraId()).ifPresent(e -> {
            throw new RuntimeException("Ya existe envio para la compra ID: " + envio.getCompraId());
        });
        envio.setEstado("preparando");
        envio.setNumeroGuia(null);
        envio.setFechaEntregaReal(null);
        return envioRepository.save(envio);
    }

    @Override
    public Envio obtenerPorId(Long id) {
        return envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envio no encontrado con ID: " + id));
    }

    @Override
    public Envio obtenerPorCompraId(Long compraId) {
        return envioRepository.findByCompraId(compraId)
            .orElseThrow(() -> new RuntimeException("Envio no encontrado para compra ID: " + compraId));
    }

    @Override
    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    @Override
    public List<Envio> listarPorUsuario(Long usuarioId) {
        return envioRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Override
    public List<Envio> listarPorEstado(String estado) {
        return envioRepository.findByEstado(estado);
    }

    @Override
    public Envio actualizarEstado(Long id, String estado) {
        if (!"preparando".equals(estado) && !"en_camino".equals(estado)
                && !"entregado".equals(estado) && !"cancelado".equals(estado)) {
            throw new RuntimeException("Estado invalido. Use 'preparando', 'en_camino', 'entregado' o 'cancelado'.");
        }
        Envio envio = obtenerPorId(id);
        envio.setEstado(estado);
        if ("entregado".equals(estado)) {
            envio.setFechaEntregaReal(LocalDateTime.now());
        }
        return envioRepository.save(envio);
    }

    @Override
    public Envio registrarGuia(Long id, String numeroGuia) {
        if (numeroGuia == null || numeroGuia.isBlank()) {
            throw new RuntimeException("El numero de guia es obligatorio");
        }
        Envio envio = obtenerPorId(id);
        envio.setNumeroGuia(numeroGuia);
        if ("preparando".equals(envio.getEstado())) {
            envio.setEstado("en_camino");
        }
        return envioRepository.save(envio);
    }

    @Override
    public void eliminar(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new RuntimeException("Envio no encontrado con ID: " + id);
        }
        envioRepository.deleteById(id);
    }
}
