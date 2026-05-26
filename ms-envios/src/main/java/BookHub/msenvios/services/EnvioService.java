package BookHub.msenvios.services;

import BookHub.msenvios.entities.Envio;

import java.util.List;

public interface EnvioService {

    Envio crear(Envio envio);

    Envio obtenerPorId(Long id);

    Envio obtenerPorCompraId(Long compraId);

    List<Envio> listarTodos();

    List<Envio> listarPorUsuario(Long usuarioId);

    List<Envio> listarPorEstado(String estado);

    Envio actualizarEstado(Long id, String estado);

    Envio registrarGuia(Long id, String numeroGuia);

    void eliminar(Long id);
}
