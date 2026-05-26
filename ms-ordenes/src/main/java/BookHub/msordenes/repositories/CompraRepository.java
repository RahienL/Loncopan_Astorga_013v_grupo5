package BookHub.msordenes.repositories;

import BookHub.msordenes.entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioId(Long usuarioId);
    List<Compra> findByEstado(String estado);
}
