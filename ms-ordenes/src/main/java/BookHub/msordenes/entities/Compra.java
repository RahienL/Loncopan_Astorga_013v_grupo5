package BookHub.msordenes.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Denormalizado: no FK a bbdd_usuarios
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "usuario_nombre", nullable = false, length = 200)
    private String usuarioNombre;

    @Column(name = "usuario_email", nullable = false, length = 200)
    private String usuarioEmail;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 30)
    private String estado = "pendiente";

    @Column(name = "numero_factura", unique = true, length = 50)
    private String numeroFactura;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleCompra> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) fechaCompra = LocalDateTime.now();
    }
}
