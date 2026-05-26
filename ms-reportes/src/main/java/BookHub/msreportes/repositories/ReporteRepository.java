package BookHub.msreportes.repositories;

import BookHub.msreportes.entities.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByUsuarioIdOrderByGeneradoEnDesc(Long usuarioId);

    List<Reporte> findByTipoOrderByGeneradoEnDesc(String tipo);
}
