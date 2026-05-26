package BookHub.msrecomendaciones.controllers;

import BookHub.msrecomendaciones.dto.request.RecomendacionRequest;
import BookHub.msrecomendaciones.dto.response.RecomendacionResponse;
import BookHub.msrecomendaciones.entities.Recomendacion;
import BookHub.msrecomendaciones.services.RecomendacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    @PostMapping
    public ResponseEntity<RecomendacionResponse> crear(@Valid @RequestBody RecomendacionRequest request) {
        return ResponseEntity.ok(RecomendacionResponse.from(recomendacionService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(RecomendacionResponse.from(recomendacionService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<RecomendacionResponse>> listarTodas() {
        return ResponseEntity.ok(recomendacionService.listarTodas().stream().map(RecomendacionResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RecomendacionResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(recomendacionService.listarPorUsuario(usuarioId).stream().map(RecomendacionResponse::from).toList());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<RecomendacionResponse>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(recomendacionService.listarPorProducto(productoId).stream().map(RecomendacionResponse::from).toList());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RecomendacionResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(recomendacionService.listarPorEstado(estado).stream().map(RecomendacionResponse::from).toList());
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RecomendacionResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(RecomendacionResponse.from(recomendacionService.desactivar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recomendacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Recomendacion toEntity(RecomendacionRequest request) {
        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setUsuarioId(request.usuarioId());
        recomendacion.setProductoId(request.productoId());
        recomendacion.setPuntaje(request.puntaje());
        recomendacion.setMotivo(request.motivo());
        return recomendacion;
    }
}
