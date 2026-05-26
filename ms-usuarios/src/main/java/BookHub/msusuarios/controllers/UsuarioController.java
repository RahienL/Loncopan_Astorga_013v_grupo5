package BookHub.msusuarios.controllers;

import java.util.List;

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

import jakarta.validation.Valid;
import BookHub.msusuarios.dto.request.UpdateUsuarioRequest;
import BookHub.msusuarios.dto.request.UsuarioRequest;
import BookHub.msusuarios.dto.response.UsuarioResponse;
import BookHub.msusuarios.entities.Usuario;
import BookHub.msusuarios.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setRol(request.rol() != null ? request.rol() : "cliente");
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.crear(usuario)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos().stream().map(UsuarioResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateUsuarioRequest request) {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(request.nombre());
        usuarioActualizado.setEmail(request.email());
        usuarioActualizado.setPassword(request.password());
        usuarioActualizado.setRol(request.rol());
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.actualizar(id, usuarioActualizado)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.cambiarEstado(id, "inactivo")));
    }
}
