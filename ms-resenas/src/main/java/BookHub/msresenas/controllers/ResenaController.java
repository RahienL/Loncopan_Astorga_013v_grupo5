package BookHub.msresenas.controllers;

import BookHub.msresenas.dto.request.ResenaRequest;
import BookHub.msresenas.dto.response.ResenaResponse;
import BookHub.msresenas.entities.Resena;
import BookHub.msresenas.services.ResenaService;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public ResponseEntity<ResenaResponse> crear(@Valid @RequestBody ResenaRequest request) {
        return ResponseEntity.ok(ResenaResponse.from(resenaService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ResenaResponse.from(resenaService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<ResenaResponse>> listarTodas() {
        return ResponseEntity.ok(resenaService.listarTodas().stream().map(ResenaResponse::from).toList());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ResenaResponse>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.listarPublicadasPorProducto(productoId).stream().map(ResenaResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ResenaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.listarPorUsuario(usuarioId).stream().map(ResenaResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaRequest request) {
        return ResponseEntity.ok(ResenaResponse.from(resenaService.actualizar(id, toEntity(request))));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ResenaResponse> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ResenaResponse.from(resenaService.cambiarEstado(id, body.get("estado"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Resena toEntity(ResenaRequest request) {
        Resena resena = new Resena();
        resena.setProductoId(request.productoId());
        resena.setUsuarioId(request.usuarioId());
        resena.setTitulo(request.titulo());
        resena.setComentario(request.comentario());
        resena.setCalificacion(request.calificacion());
        return resena;
    }
}
