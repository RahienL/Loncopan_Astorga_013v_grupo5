package BookHub.msrecomendaciones.mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import BookHub.msrecomendaciones.entities.Recomendacion;
import BookHub.msrecomendaciones.repositories.RecomendacionRepository;
import BookHub.msrecomendaciones.services.impl.RecomendacionServiceImpl;

public class RecomendacionServiceMockitoTest {

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @InjectMocks
    private RecomendacionServiceImpl recomendacionService;

    private Recomendacion recomendacionTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recomendacionTest = new Recomendacion();
        recomendacionTest.setId(1L);
        recomendacionTest.setUsuarioId(10L);
        recomendacionTest.setProductoId(50L);
        recomendacionTest.setPuntaje(0.85);
        recomendacionTest.setMotivo("Basado en historial de compras");
        recomendacionTest.setEstado("activa");
        recomendacionTest.setCreadaEn(LocalDateTime.now());
    }

    @Test
    void testCrearRecomendacion() {
        when(recomendacionRepository.save(any(Recomendacion.class))).thenReturn(recomendacionTest);
        Recomendacion resultado = recomendacionService.crear(recomendacionTest);
        assertNotNull(resultado);
        assertEquals(0.85, resultado.getPuntaje());
        assertEquals("activa", resultado.getEstado());
        verify(recomendacionRepository, times(1)).save(any(Recomendacion.class));
    }

    @Test
    void testObtenerRecomendacionPorId() {
        when(recomendacionRepository.findById(1L)).thenReturn(Optional.of(recomendacionTest));
        Recomendacion resultado = recomendacionService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(recomendacionRepository, times(1)).findById(1L);
    }

    @Test
    void testListarRecomendaciones() {
        List<Recomendacion> recomendaciones = Arrays.asList(recomendacionTest);
        when(recomendacionRepository.findAll()).thenReturn(recomendaciones);
        List<Recomendacion> resultado = recomendacionService.listarTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(recomendacionRepository, times(1)).findAll();
    }

    @Test
    void testListarRecomendacionesPorUsuario() {
        List<Recomendacion> recomendaciones = Arrays.asList(recomendacionTest);
        when(recomendacionRepository.findByUsuarioIdOrderByPuntajeDesc(10L)).thenReturn(recomendaciones);
        List<Recomendacion> resultado = recomendacionService.listarPorUsuario(10L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        verify(recomendacionRepository, times(1)).findByUsuarioIdOrderByPuntajeDesc(10L);
    }

    @Test
    void testListarRecomendacionesPorProducto() {
        List<Recomendacion> recomendaciones = Arrays.asList(recomendacionTest);
        when(recomendacionRepository.findByProductoIdOrderByPuntajeDesc(50L)).thenReturn(recomendaciones);
        List<Recomendacion> resultado = recomendacionService.listarPorProducto(50L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(50L, resultado.get(0).getProductoId());
        verify(recomendacionRepository, times(1)).findByProductoIdOrderByPuntajeDesc(50L);
    }

    @Test
    void testListarRecomendacionesPorEstado() {
        List<Recomendacion> recomendaciones = Arrays.asList(recomendacionTest);
        when(recomendacionRepository.findByEstado("activa")).thenReturn(recomendaciones);
        List<Recomendacion> resultado = recomendacionService.listarPorEstado("activa");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("activa", resultado.get(0).getEstado());
        verify(recomendacionRepository, times(1)).findByEstado("activa");
    }

    @Test
    void testDesactivarRecomendacion() {
        recomendacionTest.setEstado("inactiva");
        when(recomendacionRepository.findById(1L)).thenReturn(Optional.of(recomendacionTest));
        when(recomendacionRepository.save(any(Recomendacion.class))).thenReturn(recomendacionTest);
        Recomendacion resultado = recomendacionService.desactivar(1L);
        assertNotNull(resultado);
        assertEquals("inactiva", resultado.getEstado());
        verify(recomendacionRepository, times(1)).save(any(Recomendacion.class));
    }

    @Test
    void testEliminarRecomendacion() {
        when(recomendacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recomendacionRepository).deleteById(1L);
        recomendacionService.eliminar(1L);
        verify(recomendacionRepository, times(1)).deleteById(1L);
    }
}
