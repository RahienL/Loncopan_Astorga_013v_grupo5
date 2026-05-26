package BookHub.mspagos.services.impl;

import BookHub.mspagos.entities.Pago;
import BookHub.mspagos.repositories.PagoRepository;
import BookHub.mspagos.services.PagoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public Pago crear(Pago pago) {
        pagoRepository.findByCompraId(pago.getCompraId()).ifPresent(existente -> {
            throw new RuntimeException("Ya existe un pago registrado para la compra ID: " + pago.getCompraId());
        });

        pago.setEstado("pendiente");
        pago.setReferenciaTransaccion(null);
        pago.setFechaActualizacion(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    @Override
    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    @Override
    public Pago obtenerPorCompraId(Long compraId) {
        return pagoRepository.findByCompraId(compraId)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado para compra ID: " + compraId));
    }

    @Override
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public List<Pago> listarPorUsuario(Long usuarioId) {
        return pagoRepository.findByUsuarioIdOrderByFechaPagoDesc(usuarioId);
    }

    @Override
    public List<Pago> listarPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    @Override
    public Pago aprobarPago(Long id, String referenciaTransaccion) {
        Pago pago = obtenerPorId(id);
        pago.setEstado("aprobado");
        pago.setReferenciaTransaccion(
            (referenciaTransaccion == null || referenciaTransaccion.isBlank())
                ? "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                : referenciaTransaccion
        );
        pago.setFechaActualizacion(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    @Override
    public Pago rechazarPago(Long id) {
        Pago pago = obtenerPorId(id);
        pago.setEstado("rechazado");
        pago.setFechaActualizacion(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    @Override
    public Pago reembolsarPago(Long id) {
        Pago pago = obtenerPorId(id);
        pago.setEstado("reembolsado");
        pago.setFechaActualizacion(LocalDateTime.now());
        return pagoRepository.save(pago);
    }

    @Override
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }
}
