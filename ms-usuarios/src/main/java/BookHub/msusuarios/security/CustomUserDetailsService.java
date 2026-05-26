package BookHub.msusuarios.security;

import BookHub.msusuarios.entities.Usuario;
import BookHub.msusuarios.repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()));
        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.isActivo(), true, true, true,
                authorities
        );
    }
}
