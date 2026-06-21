package BookHub.msinventario.mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import BookHub.msinventario.entities.Inventario;
import BookHub.msinventario.repositories.InventarioRepository;
import BookHub.msinventario.services.impl.InventarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class InventarioServiceMockitoTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    @Test
    void crear_cuandoExisteProducto_debeLanzarExcepcion() {
        Inventario existente = new Inventario();
        existente.setProductoId(100L);

        Inventario nuevo = new Inventario();
        nuevo.setProductoId(100L);

        when(inventarioRepository.findByProductoId(100L)).thenReturn(Optional.of(existente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventarioService.crear(nuevo));

        assertTrue(ex.getMessage().contains("Ya existe inventario"));
    }

    @Test
    void actualizarStock_conValorNegativo_debeLanzarExcepcion() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.actualizarStock(1L, -1));

        assertEquals("El stock no puede ser negativo", ex.getMessage());
    }

    @Test
    void verificarYDescontarStock_conDatosInvalidos_debeRetornarFalse() {
        assertFalse(inventarioService.verificarYDescontarStock(null, 1));
        assertFalse(inventarioService.verificarYDescontarStock(1L, null));
        assertFalse(inventarioService.verificarYDescontarStock(0L, 1));
        assertFalse(inventarioService.verificarYDescontarStock(1L, 0));

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void verificarYDescontarStock_conStockSuficiente_debeDescontarYRetornarTrue() {
        Inventario inventario = new Inventario();
        inventario.setProductoId(55L);
        inventario.setStock(10);
        inventario.setActivo(true);

        when(inventarioRepository.findByProductoId(55L)).thenReturn(Optional.of(inventario));

        boolean ok = inventarioService.verificarYDescontarStock(55L, 3);

        assertTrue(ok);
        assertEquals(7, inventario.getStock());
        verify(inventarioRepository).save(inventario);
    }
}
