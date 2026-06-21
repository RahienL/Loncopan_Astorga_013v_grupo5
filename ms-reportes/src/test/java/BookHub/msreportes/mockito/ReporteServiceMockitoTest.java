package BookHub.msreportes.mockito;

import BookHub.msreportes.entities.Reporte;
import BookHub.msreportes.repositories.ReporteRepository;
import BookHub.msreportes.services.impl.ReporteServiceImpl;
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

public class ReporteServiceMockitoTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private Reporte reporteTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reporteTest = new Reporte();
        reporteTest.setId(1L);
        reporteTest.setUsuarioId(10L);
        reporteTest.setTipo("ventas_mensuales");
        reporteTest.setContenido("Reporte de ventas del mes");
        reporteTest.setGeneradoEn(LocalDateTime.now());
        reporteTest.setFechaActualizacion(LocalDateTime.now());
    }

    @Test
    void testCrearReporte() {
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteTest);
        Reporte resultado = reporteService.crear(reporteTest);
        assertNotNull(resultado);
        assertEquals("ventas_mensuales", resultado.getTipo());
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void testObtenerReportePorId() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporteTest));
        Reporte resultado = reporteService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    void testListarReportes() {
        List<Reporte> reportes = Arrays.asList(reporteTest);
        when(reporteRepository.findAll()).thenReturn(reportes);
        List<Reporte> resultado = reporteService.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(reporteRepository, times(1)).findAll();
    }

    @Test
    void testListarReportesPorUsuario() {
        List<Reporte> reportes = Arrays.asList(reporteTest);
        when(reporteRepository.findByUsuarioIdOrderByGeneradoEnDesc(10L)).thenReturn(reportes);
        List<Reporte> resultado = reporteService.listarPorUsuario(10L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        verify(reporteRepository, times(1)).findByUsuarioIdOrderByGeneradoEnDesc(10L);
    }

    @Test
    void testListarReportesPorTipo() {
        List<Reporte> reportes = Arrays.asList(reporteTest);
        when(reporteRepository.findByTipoOrderByGeneradoEnDesc("ventas_mensuales")).thenReturn(reportes);
        List<Reporte> resultado = reporteService.listarPorTipo("ventas_mensuales");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ventas_mensuales", resultado.get(0).getTipo());
        verify(reporteRepository, times(1)).findByTipoOrderByGeneradoEnDesc("ventas_mensuales");
    }

    @Test
    void testEliminarReporte() {
        when(reporteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reporteRepository).deleteById(1L);
        reporteService.eliminar(1L);
        verify(reporteRepository, times(1)).deleteById(1L);
    }
}
