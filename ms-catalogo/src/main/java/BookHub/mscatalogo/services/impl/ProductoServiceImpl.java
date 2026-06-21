package BookHub.mscatalogo.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import BookHub.mscatalogo.entities.Producto;
import BookHub.mscatalogo.repositories.ProductoRepository;
import BookHub.mscatalogo.services.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto crear(Producto producto) {
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setEstado("activo");
        return productoRepository.save(producto);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepository.findProductosActivos();
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto producto = obtenerPorId(id);
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setImagenUrl(productoActualizado.getImagenUrl());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setActivo(productoActualizado.getActivo());
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public Producto cambiarEstado(Long id, String nuevoEstado) {
        Producto producto = obtenerPorId(id);
        producto.setEstado(nuevoEstado);
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarImagen(Long id, String nuevaImagenUrl) {
        Producto producto = obtenerPorId(id);
        producto.setImagenUrl(nuevaImagenUrl);
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarStock(Long id, Integer nuevoStock) {
        if (nuevoStock < 0) throw new RuntimeException("El stock no puede ser negativo");
        Producto producto = obtenerPorId(id);
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    @Override
    public List<Producto> listarProductosSinStock() {
        return productoRepository.findProductosSinStock();
    }

    @Override
    public String guardarImagenProducto(MultipartFile imagen, String nombreArchivo) throws IOException {
        String directorioBase = System.getProperty("user.dir");
        Path directorioImagenes = Paths.get(directorioBase, "uploads", "productos");
        Files.createDirectories(directorioImagenes);
        Path rutaCompleta = directorioImagenes.resolve(nombreArchivo);
        Files.copy(imagen.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
        return "uploads/productos/" + nombreArchivo;
    }

    @Override
    @Transactional
    public boolean verificarYDescontarStock(Long productoId, Integer cantidad) {
        if (productoId == null || productoId <= 0 || cantidad == null || cantidad <= 0) {
            return false;
        }
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null || producto.getStock() < cantidad) {
            return false;
        }
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
        return true;
    }
}
