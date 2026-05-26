package BookHub.msnotificaciones.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "email_destino", nullable = false, length = 150)
    private String emailDestino;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String asunto;

    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String mensaje;

    @NotBlank
    @Pattern(regexp = "^(email|sms|push|sistema)$")
    @Column(nullable = false, length = 20)
    private String tipo = "email";

    @NotBlank
    @Pattern(regexp = "^(pendiente|enviada|fallida)$")
    @Column(nullable = false, length = 20)
    private String estado = "pendiente";

    @Column(nullable = false)
    private Boolean leida = false;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
