package BookHub.msusuarios.repositories;

import BookHub.msusuarios.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByEstado(String estado);
    List<Usuario> findByRol(String rol);
    @Query("SELECT u FROM Usuario u WHERE u.estado = 'activo'")
    List<Usuario> findUsuariosActivos();
}
