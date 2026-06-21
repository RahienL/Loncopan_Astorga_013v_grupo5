package BookHub.msinventario.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BookHub.msinventario.entities.Inventario;
import BookHub.msinventario.repositories.InventarioRepository;
import BookHub.msinventario.services.InventarioService;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public Inventario crear(Inventario inventario) {
        inventarioRepository.findByProductoId(inventario.getProductoId()).ifPresent(i -> {
            throw new RuntimeException("Ya existe inventario para el producto ID: " + inventario.getProductoId());
        });
        inventario.setEstado(inventario.getActivo() ? "activo" : "inactivo");
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }

    @Override
    public Inventario obtenerPorProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado para producto ID: " + productoId));
    }

    @Override
    public List<Inventario> listarActivos() {
        return inventarioRepository.findActivos();
    }

    @Override
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    @Override
    public List<Inventario> listarConStockBajo() {
        return inventarioRepository.findConStockBajo();
    }

    @Override
    public List<Inventario> listarSinStock() {
        return inventarioRepository.findSinStock();
    }

    @Override
    public Inventario actualizar(Long id, Inventario inventarioActualizado) {
        Inventario inventario = obtenerPorId(id);

        if (!inventario.getProductoId().equals(inventarioActualizado.getProductoId())) {
            inventarioRepository.findByProductoId(inventarioActualizado.getProductoId()).ifPresent(existente -> {
                throw new RuntimeException("Ya existe inventario para el producto ID: " + inventarioActualizado.getProductoId());
            });
        }

        inventario.setProductoId(inventarioActualizado.getProductoId());
        inventario.setStock(inventarioActualizado.getStock());
        inventario.setStockMinimo(inventarioActualizado.getStockMinimo());
        inventario.setActivo(inventarioActualizado.getActivo());
        return inventarioRepository.save(inventario);
    }

    @Override
    public void eliminar(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    public Inventario cambiarEstado(Long id, String nuevoEstado) {
        if (!"activo".equals(nuevoEstado) && !"inactivo".equals(nuevoEstado)) {
            throw new RuntimeException("Estado invalido. Use 'activo' o 'inactivo'.");
        }
        Inventario inventario = obtenerPorId(id);
        inventario.setEstado(nuevoEstado);
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarStock(Long id, Integer nuevoStock) {
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        Inventario inventario = obtenerPorId(id);
        inventario.setStock(nuevoStock);
        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional
    public boolean verificarYDescontarStock(Long productoId, Integer cantidad) {
        if (productoId == null || productoId <= 0 || cantidad == null || cantidad <= 0) {
            return false;
        }
        Inventario inventario = inventarioRepository.findByProductoId(productoId).orElse(null);
        if (inventario == null || !inventario.getActivo() || inventario.getStock() < cantidad) {
            return false;
        }
        inventario.setStock(inventario.getStock() - cantidad);
        inventarioRepository.save(inventario);
        return true;
    }
}
