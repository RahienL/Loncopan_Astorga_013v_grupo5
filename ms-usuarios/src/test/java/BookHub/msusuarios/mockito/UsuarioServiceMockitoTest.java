package BookHub.msusuarios.mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import BookHub.msusuarios.entities.Usuario;
import BookHub.msusuarios.repositories.UsuarioRepository;
import BookHub.msusuarios.services.impl.UsuarioServiceImpl;

public class UsuarioServiceMockitoTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Juan Pérez");
        usuarioTest.setEmail("juan@example.com");
        usuarioTest.setPassword("password123");
        usuarioTest.setRol("usuario");
        usuarioTest.setEstado("activo");
        usuarioTest.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testCrearUsuario() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);
        Usuario resultado = usuarioService.crear(usuarioTest);
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@example.com", resultado.getEmail());
        assertEquals("activo", resultado.getEstado());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testObtenerUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        Usuario resultado = usuarioService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuarioTest);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> resultado = usuarioService.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testActualizarUsuario() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos Pérez");
        usuarioActualizado.setEmail("juan.carlos@example.com");
        usuarioActualizado.setPassword("newPassword456");
        usuarioActualizado.setRol("admin");
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedNewPassword456");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);
        
        Usuario resultado = usuarioService.actualizar(1L, usuarioActualizado);
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testCambiarEstadoUsuario() {
        usuarioTest.setEstado("inactivo");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);
        
        Usuario resultado = usuarioService.cambiarEstado(1L, "inactivo");
        assertNotNull(resultado);
        assertEquals("inactivo", resultado.getEstado());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);
        
        usuarioService.eliminar(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
