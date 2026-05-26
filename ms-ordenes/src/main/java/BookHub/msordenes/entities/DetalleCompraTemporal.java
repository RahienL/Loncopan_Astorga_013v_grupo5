package BookHub.msordenes.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_compra_temporal")
public class DetalleCompraTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_temporal_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private CompraTemporal compraTemporal;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "producto_nombre", nullable = false, length = 200)
    private String productoNombre;

    @Column(name = "producto_imagen", length = 500)
    private String productoImagen;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    public BigDecimal getSubtotal() {
        if (cantidad == null || precioUnitario == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
