package BookHub.msnotificaciones.controllers;

import BookHub.msnotificaciones.dto.request.NotificacionRequest;
import BookHub.msnotificaciones.dto.response.NotificacionResponse;
import BookHub.msnotificaciones.entities.Notificacion;
import BookHub.msnotificaciones.services.NotificacionService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(@Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(NotificacionResponse.from(notificacionService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(NotificacionResponse.from(notificacionService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listarTodas() {
        return ResponseEntity.ok(notificacionService.listarTodas().stream().map(NotificacionResponse::from).toList());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<NotificacionResponse>> listarPendientes() {
        return ResponseEntity.ok(notificacionService.listarPendientes().stream().map(NotificacionResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId).stream().map(NotificacionResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<NotificacionResponse>> listarNoLeidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorUsuario(usuarioId).stream().map(NotificacionResponse::from).toList());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<NotificacionResponse> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(NotificacionResponse.from(notificacionService.cambiarEstado(id, body.get("estado"))));
    }

    @PatchMapping("/{id}/marcar-leida")
    public ResponseEntity<NotificacionResponse> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(NotificacionResponse.from(notificacionService.marcarComoLeida(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Notificacion toEntity(NotificacionRequest request) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(request.usuarioId());
        notificacion.setEmailDestino(request.emailDestino());
        notificacion.setAsunto(request.asunto());
        notificacion.setMensaje(request.mensaje());
        notificacion.setTipo(request.tipo());
        return notificacion;
    }
}
