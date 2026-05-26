package BookHub.mspagos.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PagoRequest(
    @NotNull Long compraId,
    @NotNull Long usuarioId,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal monto,
    @NotBlank @Pattern(regexp = "^(tarjeta|paypal|transferencia|efectivo)$") String metodoPago
) {
}
