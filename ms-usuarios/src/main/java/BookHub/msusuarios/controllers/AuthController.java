package BookHub.msusuarios.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import BookHub.msusuarios.dto.request.AuthRequest;
import BookHub.msusuarios.dto.response.AuthResponse;
import BookHub.msusuarios.entities.Usuario;
import BookHub.msusuarios.repositories.UsuarioRepository;
import BookHub.msusuarios.security.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, UserDetailsService userDetailsService,
                          JwtUtils jwtUtils, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(req.email()).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Email o contraseña incorrectos"));
            }
            if (!"activo".equals(usuario.getEstado())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Usuario inactivo. Contacte al administrador"));
            }
            if (!passwordEncoder.matches(req.password(), usuario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Email o contraseña incorrectos"));
            }
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
            UserDetails user = userDetailsService.loadUserByUsername(req.email());
            String token = jwtUtils.generateToken(user.getUsername());
            var userInfo = new AuthResponse.UserInfo(
                    usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                    usuario.getRol(), usuario.getEstado(), usuario.getFechaCreacion());
            return ResponseEntity.ok(new AuthResponse(token, userInfo));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciales inválidas"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Usuario inactivo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error interno: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody BookHub.msusuarios.dto.request.UsuarioRequest request) {
        try {
            if (usuarioRepository.findByEmail(request.email()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new AuthResponse("El email ya está registrado"));
            }
            Usuario usuario = new Usuario();
            usuario.setNombre(request.nombre());
            usuario.setEmail(request.email());
            usuario.setPassword(passwordEncoder.encode(request.password()));
            usuario.setEstado("activo");
            usuario.setRol(request.rol() != null && !request.rol().isEmpty() ? request.rol() : "cliente");
            Usuario saved = usuarioRepository.save(usuario);
            String token = jwtUtils.generateToken(saved.getEmail());
            var userInfo = new AuthResponse.UserInfo(
                    saved.getId(), saved.getNombre(), saved.getEmail(),
                    saved.getRol(), saved.getEstado(), saved.getFechaCreacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, userInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error al registrar: " + e.getMessage()));
        }
    }
}
