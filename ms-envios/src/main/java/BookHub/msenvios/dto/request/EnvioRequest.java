package BookHub.msenvios.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EnvioRequest(
    @NotNull Long compraId,
    @NotNull Long usuarioId,
    @NotBlank @Size(max = 255) String direccionEntrega,
    @NotBlank @Size(max = 120) String ciudad,
    @NotBlank @Size(max = 20) String codigoPostal,
    @NotBlank @Size(max = 80) String empresaTransporte,
    @NotNull @FutureOrPresent LocalDateTime fechaEntregaEstimada
) {
}
