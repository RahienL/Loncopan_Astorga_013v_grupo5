package BookHub.msenvios.dto.response;

import BookHub.msenvios.entities.Envio;

import java.time.LocalDateTime;

public record EnvioResponse(
    Long id,
    Long compraId,
    Long usuarioId,
    String direccionEntrega,
    String ciudad,
    String codigoPostal,
    String empresaTransporte,
    String numeroGuia,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaEntregaEstimada,
    LocalDateTime fechaEntregaReal
) {

    public static EnvioResponse from(Envio envio) {
        return new EnvioResponse(
            envio.getId(),
            envio.getCompraId(),
            envio.getUsuarioId(),
            envio.getDireccionEntrega(),
            envio.getCiudad(),
            envio.getCodigoPostal(),
            envio.getEmpresaTransporte(),
            envio.getNumeroGuia(),
            envio.getEstado(),
            envio.getFechaCreacion(),
            envio.getFechaEntregaEstimada(),
            envio.getFechaEntregaReal()
        );
    }
}
