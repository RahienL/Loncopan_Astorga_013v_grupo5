package BookHub.msenvios.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "compra_id", nullable = false, unique = true)
    private Long compraId;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "direccion_entrega", nullable = false, length = 255)
    private String direccionEntrega;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String ciudad;

    @NotBlank
    @Size(max = 20)
    @Column(name = "codigo_postal", nullable = false, length = 20)
    private String codigoPostal;

    @NotBlank
    @Size(max = 80)
    @Column(name = "empresa_transporte", nullable = false, length = 80)
    private String empresaTransporte;

    @Size(max = 100)
    @Column(name = "numero_guia", length = 100, unique = true)
    private String numeroGuia;

    @NotBlank
    @Pattern(regexp = "^(preparando|en_camino|entregado|cancelado)$")
    @Column(nullable = false, length = 20)
    private String estado = "preparando";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @NotNull
    @FutureOrPresent
    @Column(name = "fecha_entrega_estimada", nullable = false)
    private LocalDateTime fechaEntregaEstimada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
