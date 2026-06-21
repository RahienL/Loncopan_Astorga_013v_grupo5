package BookHub.msresenas.mockito;

import BookHub.msresenas.entities.Resena;
import BookHub.msresenas.repositories.ResenaRepository;
import BookHub.msresenas.services.impl.ResenaServiceImpl;
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

public class ResenaServiceMockitoTest {

    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaServiceImpl resenaService;

    private Resena resenaTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resenaTest = new Resena();
        resenaTest.setId(1L);
        resenaTest.setUsuarioId(10L);
        resenaTest.setProductoId(50L);
        resenaTest.setCalificacion(5);
        resenaTest.setTitulo("Excelente libro");
        resenaTest.setComentario("Recomiendo este libro, muy interesante.");
        resenaTest.setEstado("publicada");
        resenaTest.setFechaCreacion(LocalDateTime.now());
        resenaTest.setFechaActualizacion(LocalDateTime.now());
    }

    @Test
    void testCrearResena() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaTest);
        Resena resultado = resenaService.crear(resenaTest);
        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        assertEquals("Excelente libro", resultado.getTitulo());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void testObtenerResenaPorId() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaTest));
        Resena resultado = resenaService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(resenaRepository, times(1)).findById(1L);
    }

    @Test
    void testListarResenas() {
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaRepository.findAll()).thenReturn(resenas);
        List<Resena> resultado = resenaService.listarTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    void testListarResenasPublicadasPorProducto() {
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaRepository.findByProductoIdAndEstadoOrderByFechaCreacionDesc(50L, "publicada")).thenReturn(resenas);
        List<Resena> resultado = resenaService.listarPublicadasPorProducto(50L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("publicada", resultado.get(0).getEstado());
        verify(resenaRepository, times(1)).findByProductoIdAndEstadoOrderByFechaCreacionDesc(50L, "publicada");
    }

    @Test
    void testListarResenasPorUsuario() {
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaRepository.findByUsuarioIdOrderByFechaCreacionDesc(10L)).thenReturn(resenas);
        List<Resena> resultado = resenaService.listarPorUsuario(10L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        verify(resenaRepository, times(1)).findByUsuarioIdOrderByFechaCreacionDesc(10L);
    }

    @Test
    void testActualizarResena() {
        resenaTest.setComentario("Contenido actualizado");
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaTest));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaTest);
        Resena resultado = resenaService.actualizar(1L, resenaTest);
        assertNotNull(resultado);
        assertEquals("Contenido actualizado", resultado.getComentario());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void testCambiarEstadoResena() {
        resenaTest.setEstado("oculta");
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaTest));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaTest);
        Resena resultado = resenaService.cambiarEstado(1L, "oculta");
        assertNotNull(resultado);
        assertEquals("oculta", resultado.getEstado());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void testEliminarResena() {
        when(resenaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resenaRepository).deleteById(1L);
        resenaService.eliminar(1L);
        verify(resenaRepository, times(1)).deleteById(1L);
    }
}
