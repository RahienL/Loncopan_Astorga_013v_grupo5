package BookHub.msinventario.repositories;

import BookHub.msinventario.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoId(Long productoId);

    List<Inventario> findByEstado(String estado);

    @Query("SELECT i FROM Inventario i WHERE i.estado = 'activo'")
    List<Inventario> findActivos();

    @Query("SELECT i FROM Inventario i WHERE i.stock <= i.stockMinimo AND i.estado = 'activo'")
    List<Inventario> findConStockBajo();

    @Query("SELECT i FROM Inventario i WHERE i.stock = 0 AND i.estado = 'activo'")
    List<Inventario> findSinStock();

    @Query("SELECT i FROM Inventario i WHERE i.productoId IN :productoIds AND i.estado = 'activo'")
    List<Inventario> findActivosByProductoIds(@Param("productoIds") List<Long> productoIds);
}
