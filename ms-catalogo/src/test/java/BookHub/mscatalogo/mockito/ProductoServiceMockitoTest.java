package BookHub.mscatalogo.mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import BookHub.mscatalogo.entities.Producto;
import BookHub.mscatalogo.repositories.ProductoRepository;
import BookHub.mscatalogo.services.impl.ProductoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductoServiceMockitoTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void crear_debeAsignarEstadoActivoYFechaCreacion() {
        Producto producto = new Producto();
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto guardado = productoService.crear(producto);

        assertEquals("activo", guardado.getEstado());
        assertNotNull(guardado.getFechaCreacion());
        verify(productoRepository).save(producto);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productoService.obtenerPorId(99L));

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void actualizarStock_conValorNegativo_debeLanzarExcepcion() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> productoService.actualizarStock(1L, -1));

        assertEquals("El stock no puede ser negativo", ex.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.existsById(7L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productoService.eliminar(7L));

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void verificarYDescontarStock_conDatosInvalidos_debeRetornarFalse() {
        assertFalse(productoService.verificarYDescontarStock(null, 1));
        assertFalse(productoService.verificarYDescontarStock(1L, null));
        assertFalse(productoService.verificarYDescontarStock(0L, 1));
        assertFalse(productoService.verificarYDescontarStock(1L, 0));

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void verificarYDescontarStock_conStockSuficiente_debeDescontarYRetornarTrue() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(10);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        boolean ok = productoService.verificarYDescontarStock(1L, 4);

        assertTrue(ok);
        assertEquals(6, producto.getStock());
        verify(productoRepository).save(producto);
    }
}
