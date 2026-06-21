package BookHub.msnotificaciones.mockito;

import BookHub.msnotificaciones.entities.Notificacion;
import BookHub.msnotificaciones.repositories.NotificacionRepository;
import BookHub.msnotificaciones.services.impl.NotificacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificacionServiceMockitoTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private Notificacion notificacionTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificacionTest = new Notificacion();
        notificacionTest.setId(1L);
        notificacionTest.setUsuarioId(10L);
        notificacionTest.setEmailDestino("usuario@example.com");
        notificacionTest.setAsunto("Confirmación de compra");
        notificacionTest.setMensaje("Tu compra ha sido confirmada.");
        notificacionTest.setTipo("email");
        notificacionTest.setEstado("pendiente");
        notificacionTest.setLeida(false);
        notificacionTest.setFechaCreacion(LocalDateTime.now());
        notificacionTest.setFechaEnvio(LocalDateTime.now());
    }

    @Test
    void testCrearNotificacion() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionTest);
        Notificacion resultado = notificacionService.crear(notificacionTest);
        assertNotNull(resultado);
        assertEquals("email", resultado.getTipo());
        assertEquals("usuario@example.com", resultado.getEmailDestino());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testObtenerNotificacionPorId() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionTest));
        Notificacion resultado = notificacionService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    void testListarNotificaciones() {
        List<Notificacion> notificaciones = Arrays.asList(notificacionTest);
        when(notificacionRepository.findAll()).thenReturn(notificaciones);
        List<Notificacion> resultado = notificacionService.listarTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    void testListarNotificacionesPendientes() {
        List<Notificacion> notificaciones = Arrays.asList(notificacionTest);
        when(notificacionRepository.findPendientes()).thenReturn(notificaciones);
        List<Notificacion> resultado = notificacionService.listarPendientes();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findPendientes();
    }

    @Test
    void testListarNotificacionesPorUsuario() {
        List<Notificacion> notificaciones = Arrays.asList(notificacionTest);
        when(notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(10L)).thenReturn(notificaciones);
        List<Notificacion> resultado = notificacionService.listarPorUsuario(10L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findByUsuarioIdOrderByFechaCreacionDesc(10L);
    }

    @Test
    void testMarcarNotificacionComoLeida() {
        notificacionTest.setLeida(true);
        notificacionTest.setEstado("enviada");
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionTest));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionTest);
        Notificacion resultado = notificacionService.marcarComoLeida(1L);
        assertNotNull(resultado);
        assertTrue(Boolean.TRUE.equals(resultado.getLeida()));
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testCambiarEstadoNotificacion() {
        notificacionTest.setEstado("enviada");
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionTest));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionTest);
        Notificacion resultado = notificacionService.cambiarEstado(1L, "enviada");
        assertNotNull(resultado);
        assertEquals("enviada", resultado.getEstado());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testEliminarNotificacion() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionRepository).deleteById(1L);
        notificacionService.eliminar(1L);
        verify(notificacionRepository, times(1)).deleteById(1L);
    }
}
