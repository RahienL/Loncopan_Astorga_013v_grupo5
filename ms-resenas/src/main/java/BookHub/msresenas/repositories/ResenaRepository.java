package BookHub.msresenas.repositories;

import BookHub.msresenas.entities.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByProductoIdAndEstadoOrderByFechaCreacionDesc(Long productoId, String estado);

    List<Resena> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Resena> findByEstado(String estado);
}
