package BookHub.mspagos.repositories;

import BookHub.mspagos.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByCompraId(Long compraId);

    List<Pago> findByUsuarioIdOrderByFechaPagoDesc(Long usuarioId);

    List<Pago> findByEstado(String estado);

    @Query("SELECT p FROM Pago p WHERE p.compraId = :compraId AND p.estado = 'aprobado'")
    Optional<Pago> findPagoAprobadoByCompraId(@Param("compraId") Long compraId);
}
