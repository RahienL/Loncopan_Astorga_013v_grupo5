package BookHub.mscatalogo.repositories;

import BookHub.mscatalogo.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(String estado);

    @Query("SELECT p FROM Producto p WHERE p.estado = 'activo'")
    List<Producto> findProductosActivos();

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.estado = 'activo'")
    List<Producto> findByCategoriaIdAndEstadoActivo(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.stock < 5 AND p.estado = 'activo'")
    List<Producto> findProductosConStockBajo();

    @Query("SELECT p FROM Producto p WHERE p.stock = 0 AND p.estado = 'activo'")
    List<Producto> findProductosSinStock();

    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMin AND :precioMax AND p.estado = 'activo'")
    List<Producto> findByPrecioBetweenAndEstadoActivo(@Param("precioMin") BigDecimal precioMin,
                                                       @Param("precioMax") BigDecimal precioMax);
}
