package BookHub.mscatalogo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
    @NotBlank @Size(min = 2, max = 200) String nombre,
    @NotBlank @Size(min = 10, max = 2000) String descripcion,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal precio,
    @NotNull @Min(0) Integer stock,
    @NotNull Long categoriaId,
    @Size(max = 500) String imagenUrl,
    boolean activo
) {}
