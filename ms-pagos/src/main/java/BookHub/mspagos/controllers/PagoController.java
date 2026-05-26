package BookHub.mspagos.controllers;

import BookHub.mspagos.dto.request.PagoRequest;
import BookHub.mspagos.dto.response.PagoResponse;
import BookHub.mspagos.entities.Pago;
import BookHub.mspagos.services.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.obtenerPorId(id)));
    }

    @GetMapping("/compra/{compraId}")
    public ResponseEntity<PagoResponse> obtenerPorCompraId(@PathVariable Long compraId) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.obtenerPorCompraId(compraId)));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos().stream().map(PagoResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.listarPorUsuario(usuarioId).stream().map(PagoResponse::from).toList());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.listarPorEstado(estado).stream().map(PagoResponse::from).toList());
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<PagoResponse> aprobar(@PathVariable Long id,
                                                @RequestParam(required = false) String referencia) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.aprobarPago(id, referencia)));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<PagoResponse> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.rechazarPago(id)));
    }

    @PatchMapping("/{id}/reembolsar")
    public ResponseEntity<PagoResponse> reembolsar(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.from(pagoService.reembolsarPago(id)));
    }

    // Endpoint interno para ms-ordenes.
    @PostMapping("/procesar")
    public ResponseEntity<Map<String, Object>> procesarPago(@Valid @RequestBody PagoRequest request) {
        Pago pago = pagoService.crear(toEntity(request));
        Pago aprobado = pagoService.aprobarPago(pago.getId(), null);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "pagoId", aprobado.getId(),
            "estado", aprobado.getEstado(),
            "referencia", aprobado.getReferenciaTransaccion()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Pago toEntity(PagoRequest request) {
        Pago pago = new Pago();
        pago.setCompraId(request.compraId());
        pago.setUsuarioId(request.usuarioId());
        pago.setMonto(request.monto());
        pago.setMetodoPago(request.metodoPago());
        return pago;
    }
}
