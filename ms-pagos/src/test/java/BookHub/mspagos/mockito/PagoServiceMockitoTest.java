package BookHub.mspagos.mockito;

import BookHub.mspagos.entities.Pago;
import BookHub.mspagos.repositories.PagoRepository;
import BookHub.mspagos.services.impl.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PagoServiceMockitoTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pagoTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pagoTest = new Pago();
        pagoTest.setId(1L);
        pagoTest.setCompraId(100L);
        pagoTest.setUsuarioId(10L);
        pagoTest.setMonto(new BigDecimal("150000.00"));
        pagoTest.setMetodoPago("tarjeta");
        pagoTest.setEstado("pendiente");
        pagoTest.setReferenciaTransaccion("TXN123456");
        pagoTest.setFechaPago(LocalDateTime.now());
        pagoTest.setFechaActualizacion(LocalDateTime.now());
    }

    @Test
    void testCrearPago() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoTest);
        Pago resultado = pagoService.crear(pagoTest);
        assertNotNull(resultado);
        assertEquals(new BigDecimal("150000.00"), resultado.getMonto());
        assertEquals("pendiente", resultado.getEstado());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testObtenerPagoPorId() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoTest));
        Pago resultado = pagoService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPagoPorCompraId() {
        when(pagoRepository.findByCompraId(100L)).thenReturn(Optional.of(pagoTest));
        Pago resultado = pagoService.obtenerPorCompraId(100L);
        assertNotNull(resultado);
        assertEquals(100L, resultado.getCompraId());
        verify(pagoRepository, times(1)).findByCompraId(100L);
    }

    @Test
    void testListarPagos() {
        List<Pago> pagos = Arrays.asList(pagoTest);
        when(pagoRepository.findAll()).thenReturn(pagos);
        List<Pago> resultado = pagoService.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testListarPagosPorUsuario() {
        List<Pago> pagos = Arrays.asList(pagoTest);
        when(pagoRepository.findByUsuarioIdOrderByFechaPagoDesc(10L)).thenReturn(pagos);
        List<Pago> resultado = pagoService.listarPorUsuario(10L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findByUsuarioIdOrderByFechaPagoDesc(10L);
    }

    @Test
    void testListarPagosPorEstado() {
        List<Pago> pagos = Arrays.asList(pagoTest);
        when(pagoRepository.findByEstado("pendiente")).thenReturn(pagos);
        List<Pago> resultado = pagoService.listarPorEstado("pendiente");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("pendiente", resultado.get(0).getEstado());
        verify(pagoRepository, times(1)).findByEstado("pendiente");
    }

    @Test
    void testAprobarPago() {
        pagoTest.setEstado("aprobado");
        pagoTest.setReferenciaTransaccion("TXN789012");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoTest));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoTest);
        Pago resultado = pagoService.aprobarPago(1L, "TXN789012");
        assertNotNull(resultado);
        assertEquals("aprobado", resultado.getEstado());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testRechazarPago() {
        pagoTest.setEstado("rechazado");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoTest));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoTest);
        Pago resultado = pagoService.rechazarPago(1L);
        assertNotNull(resultado);
        assertEquals("rechazado", resultado.getEstado());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testEliminarPago() {
        when(pagoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(1L);
        pagoService.eliminar(1L);
        verify(pagoRepository, times(1)).deleteById(1L);
    }
}
