package BookHub.msenvios.mockito;

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

import BookHub.msenvios.entities.Envio;
import BookHub.msenvios.repositories.EnvioRepository;
import BookHub.msenvios.services.impl.EnvioServiceImpl;

public class EnvioServiceMockitoTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envioTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        envioTest = new Envio();
        envioTest.setId(1L);
        envioTest.setCompraId(100L);
        envioTest.setUsuarioId(10L);
        envioTest.setDireccionEntrega("Calle Principal 123");
        envioTest.setCiudad("Santiago");
        envioTest.setCodigoPostal("8320000");
        envioTest.setEmpresaTransporte("DHL");
        envioTest.setNumeroGuia("DHL123456");
        envioTest.setEstado("en_transito");
    }

    @Test
    void testCrearEnvio() {
        when(envioRepository.save(any(Envio.class))).thenReturn(envioTest);
        Envio resultado = envioService.crear(envioTest);
        assertNotNull(resultado);
        assertEquals("DHL", resultado.getEmpresaTransporte());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void testObtenerEnvioPorId() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envioTest));
        Envio resultado = envioService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(envioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerEnvioPorCompraId() {
        when(envioRepository.findByCompraId(100L)).thenReturn(Optional.of(envioTest));
        Envio resultado = envioService.obtenerPorCompraId(100L);
        assertNotNull(resultado);
        assertEquals(100L, resultado.getCompraId());
        verify(envioRepository, times(1)).findByCompraId(100L);
    }

    @Test
    void testListarEnvios() {
        List<Envio> envios = Arrays.asList(envioTest);
        when(envioRepository.findAll()).thenReturn(envios);
        List<Envio> resultado = envioService.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(envioRepository, times(1)).findAll();
    }

    @Test
    void testListarEnviosPorEstado() {
        List<Envio> envios = Arrays.asList(envioTest);
        when(envioRepository.findByEstado("en_transito")).thenReturn(envios);
        List<Envio> resultado = envioService.listarPorEstado("en_transito");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("en_transito", resultado.get(0).getEstado());
        verify(envioRepository, times(1)).findByEstado("en_transito");
    }

    @Test
    void testActualizarEstadoEnvio() {
        envioTest.setEstado("entregado");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envioTest));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioTest);
        Envio resultado = envioService.actualizarEstado(1L, "entregado");
        assertNotNull(resultado);
        assertEquals("entregado", resultado.getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void testEliminarEnvio() {
        when(envioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(envioRepository).deleteById(1L);
        envioService.eliminar(1L);
        verify(envioRepository, times(1)).deleteById(1L);
    }
}
