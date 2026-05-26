package BookHub.mscatalogo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import BookHub.mscatalogo.dto.request.ProductoRequest;
import BookHub.mscatalogo.dto.response.ProductoResponse;
import BookHub.mscatalogo.entities.Categoria;
import BookHub.mscatalogo.entities.Producto;
import BookHub.mscatalogo.services.CategoriaService;
import BookHub.mscatalogo.services.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        return ResponseEntity.ok(productoService.listarActivos().stream().map(ProductoResponse::from).toList());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ProductoResponse>> listarTodosLosProductos() {
        return ResponseEntity.ok(productoService.listarTodos().stream().map(ProductoResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.actualizar(id, toEntity(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.cambiarEstado(id, "inactivo")));
    }

    @PatchMapping("/{id}/imagen")
    public ResponseEntity<ProductoResponse> actualizarImagen(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.actualizarImagen(id, request.get("imagenUrl"))));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        return ResponseEntity.ok(ProductoResponse.from(productoService.actualizarStock(id, request.get("stock"))));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponse>> productosConStockBajo() {
        return ResponseEntity.ok(productoService.listarProductosConStockBajo().stream().map(ProductoResponse::from).toList());
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<ProductoResponse>> productosSinStock() {
        return ResponseEntity.ok(productoService.listarProductosSinStock().stream().map(ProductoResponse::from).toList());
    }

    /**
     * Endpoint interno usado por ms-ordenes para verificar y descontar stock al confirmar una compra.
     */
    @PostMapping("/{id}/descontar-stock")
    public ResponseEntity<Map<String, Object>> descontarStock(@PathVariable Long id,
                                                               @RequestBody Map<String, Integer> body) {
        boolean ok = productoService.verificarYDescontarStock(id, body.get("cantidad"));
        Map<String, Object> result = new HashMap<>();
        result.put("success", ok);
        if (!ok) result.put("error", "Stock insuficiente o producto no encontrado");
        return ok ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @PostMapping("/upload-imagen")
    public ResponseEntity<?> uploadImagenProducto(@RequestParam("imagen") MultipartFile imagen,
                                                   @RequestParam("nombreArchivo") String nombreArchivo) {
        try {
            if (imagen.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Archivo vacío"));
            }
            String contentType = imagen.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Solo se permiten imágenes"));
            }
            if (imagen.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Máximo 5MB"));
            }
            String ruta = productoService.guardarImagenProducto(imagen, nombreArchivo);
            return ResponseEntity.ok(Map.of("success", true, "rutaImagen", ruta));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    private Producto toEntity(ProductoRequest request) {
        Categoria categoria = categoriaService.obtenerPorId(request.categoriaId());
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
        producto.setImagenUrl(request.imagenUrl());
        producto.setActivo(request.activo());
        return producto;
    }
}
