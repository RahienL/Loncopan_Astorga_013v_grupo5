package BookHub.msnotificaciones.repositories;

import BookHub.msnotificaciones.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    @Query("SELECT n FROM Notificacion n WHERE n.estado = 'pendiente' ORDER BY n.fechaCreacion ASC")
    List<Notificacion> findPendientes();

    @Query("SELECT n FROM Notificacion n WHERE n.usuarioId = :usuarioId AND n.leida = false ORDER BY n.fechaCreacion DESC")
    List<Notificacion> findNoLeidasPorUsuario(@Param("usuarioId") Long usuarioId);

    List<Notificacion> findByEstado(String estado);
}
