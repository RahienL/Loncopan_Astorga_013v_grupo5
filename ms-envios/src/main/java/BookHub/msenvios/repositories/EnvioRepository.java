package BookHub.msenvios.repositories;

import BookHub.msenvios.entities.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByCompraId(Long compraId);

    List<Envio> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Envio> findByEstado(String estado);

    Optional<Envio> findByNumeroGuia(String numeroGuia);
}
