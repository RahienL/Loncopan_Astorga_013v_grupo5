package BookHub.msrecomendaciones.repositories;

import BookHub.msrecomendaciones.entities.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {

    List<Recomendacion> findByUsuarioIdOrderByPuntajeDesc(Long usuarioId);

    List<Recomendacion> findByProductoIdOrderByPuntajeDesc(Long productoId);

    List<Recomendacion> findByEstado(String estado);
}
