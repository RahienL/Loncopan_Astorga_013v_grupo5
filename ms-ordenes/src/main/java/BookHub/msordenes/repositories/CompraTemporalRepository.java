package BookHub.msordenes.repositories;

import BookHub.msordenes.entities.CompraTemporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraTemporalRepository extends JpaRepository<CompraTemporal, Long> {
    Optional<CompraTemporal> findByUsuarioIdAndEstado(Long usuarioId, String estado);
    List<CompraTemporal> findByUsuarioId(Long usuarioId);
}
