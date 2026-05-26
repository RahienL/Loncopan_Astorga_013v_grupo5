package BookHub.mspagos.services;

import BookHub.mspagos.entities.Pago;

import java.util.List;

public interface PagoService {

    Pago crear(Pago pago);

    Pago obtenerPorId(Long id);

    Pago obtenerPorCompraId(Long compraId);

    List<Pago> listarTodos();

    List<Pago> listarPorUsuario(Long usuarioId);

    List<Pago> listarPorEstado(String estado);

    Pago aprobarPago(Long id, String referenciaTransaccion);

    Pago rechazarPago(Long id);

    Pago reembolsarPago(Long id);

    void eliminar(Long id);
}
