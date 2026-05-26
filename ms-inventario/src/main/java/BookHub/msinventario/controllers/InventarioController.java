package BookHub.msinventario.controllers;

import BookHub.msinventario.dto.request.InventarioRequest;
import BookHub.msinventario.dto.response.InventarioResponse;
import BookHub.msinventario.entities.Inventario;
import BookHub.msinventario.services.InventarioService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<InventarioResponse> crearInventario(@Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.obtenerPorId(id)));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponse> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.obtenerPorProductoId(productoId)));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponse>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos().stream().map(InventarioResponse::from).toList());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<InventarioResponse>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos().stream().map(InventarioResponse::from).toList());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<InventarioResponse>> listarConStockBajo() {
        return ResponseEntity.ok(inventarioService.listarConStockBajo().stream().map(InventarioResponse::from).toList());
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<InventarioResponse>> listarSinStock() {
        return ResponseEntity.ok(inventarioService.listarSinStock().stream().map(InventarioResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.actualizar(id, toEntity(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<InventarioResponse> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.cambiarEstado(id, request.get("estado"))));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<InventarioResponse> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        return ResponseEntity.ok(InventarioResponse.from(inventarioService.actualizarStock(id, request.get("stock"))));
    }

    @PostMapping("/producto/{productoId}/descontar-stock")
    public ResponseEntity<Map<String, Object>> descontarStock(@PathVariable Long productoId,
                                                               @RequestBody Map<String, Integer> body) {
        boolean ok = inventarioService.verificarYDescontarStock(productoId, body.get("cantidad"));
        Map<String, Object> result = new HashMap<>();
        result.put("success", ok);
        if (!ok) {
            result.put("error", "Stock insuficiente o inventario no encontrado");
        }
        return ok ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    private Inventario toEntity(InventarioRequest request) {
        Inventario inventario = new Inventario();
        inventario.setProductoId(request.productoId());
        inventario.setStock(request.stock());
        inventario.setStockMinimo(request.stockMinimo());
        inventario.setActivo(request.activo());
        return inventario;
    }
}
