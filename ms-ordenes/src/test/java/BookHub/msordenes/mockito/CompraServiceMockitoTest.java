package BookHub.msordenes.mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import BookHub.msordenes.dto.request.CompraTemporalRequest;
import BookHub.msordenes.entities.Compra;
import BookHub.msordenes.entities.CompraTemporal;
import BookHub.msordenes.entities.DetalleCompraTemporal;
import BookHub.msordenes.repositories.CompraRepository;
import BookHub.msordenes.repositories.CompraTemporalRepository;
import BookHub.msordenes.services.impl.CompraServiceImpl;

@ExtendWith(MockitoExtension.class)
class CompraServiceMockitoTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private CompraTemporalRepository compraTemporalRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CompraServiceImpl compraService;

    @Test
    void crearCompraTemporal_conUsuarioNulo_debeLanzarExcepcion() {
        CompraTemporalRequest request = new CompraTemporalRequest(null, "", new ArrayList<>());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.crearCompratemporal(request));

        assertTrue(ex.getMessage().contains("Usuario inválido"));
    }

    @Test
    void agregarItem_carritoInactivo_debeLanzarExcepcion() {
        CompraTemporal carrito = new CompraTemporal();
        carrito.setId(1L);
        carrito.setEstado("confirmado");

        CompraTemporalRequest.ItemRequest item = new CompraTemporalRequest.ItemRequest(
                10L,
                "Libro X",
                null,
                BigDecimal.valueOf(15000),
                1);

        when(compraTemporalRepository.findById(1L)).thenReturn(Optional.of(carrito));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.agregarItemAlCarrito(1L, item));

        assertTrue(ex.getMessage().contains("carrito activo"));
    }

    @Test
    void confirmarCompra_conCarritoVacio_debeLanzarExcepcion() {
        CompraTemporal carrito = new CompraTemporal();
        carrito.setId(2L);
        carrito.setEstado("activo");
        carrito.setDetalles(new ArrayList<>());

        when(compraTemporalRepository.findById(2L)).thenReturn(Optional.of(carrito));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.confirmarCompra(2L, 11L, "Ana", "ana@test.com", "tarjeta"));

        assertTrue(ex.getMessage().contains("carrito vacío"));
    }

    @Test
    void confirmarCompra_conDatosValidos_debeGuardarCompra() {
        ReflectionTestUtils.setField(compraService, "msCatalogoUrl", "http://ms-catalogo:8084");

        CompraTemporal carrito = new CompraTemporal();
        carrito.setId(3L);
        carrito.setEstado("activo");

        DetalleCompraTemporal detalle = new DetalleCompraTemporal();
        detalle.setProductoId(101L);
        detalle.setProductoNombre("Libro Mockito");
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(BigDecimal.valueOf(12000));
        carrito.getDetalles().add(detalle);

        when(compraTemporalRepository.findById(3L)).thenReturn(Optional.of(carrito));
        @SuppressWarnings("unchecked")
        ResponseEntity<Map> okResp = (ResponseEntity<Map>) (ResponseEntity<?>) ResponseEntity.ok(Map.of("success", true));
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(okResp);
        when(compraTemporalRepository.save(any(CompraTemporal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(compraRepository.save(any(Compra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Compra compra = compraService.confirmarCompra(3L, 20L, "Juan", "juan@test.com", "tarjeta");

        assertNotNull(compra);
        assertEquals("confirmada", compra.getEstado());
        assertEquals(BigDecimal.valueOf(24000), compra.getTotal());
        verify(compraRepository).save(any(Compra.class));
        verify(compraTemporalRepository).save(any(CompraTemporal.class));
    }
}
