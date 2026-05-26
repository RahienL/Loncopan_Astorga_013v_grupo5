package BookHub.msenvios.controllers;

import BookHub.msenvios.dto.request.EnvioRequest;
import BookHub.msenvios.dto.response.EnvioResponse;
import BookHub.msenvios.entities.Envio;
import BookHub.msenvios.services.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PostMapping
    public ResponseEntity<EnvioResponse> crear(@Valid @RequestBody EnvioRequest request) {
        return ResponseEntity.ok(EnvioResponse.from(envioService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EnvioResponse.from(envioService.obtenerPorId(id)));
    }

    @GetMapping("/compra/{compraId}")
    public ResponseEntity<EnvioResponse> obtenerPorCompraId(@PathVariable Long compraId) {
        return ResponseEntity.ok(EnvioResponse.from(envioService.obtenerPorCompraId(compraId)));
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponse>> listarTodos() {
        return ResponseEntity.ok(envioService.listarTodos().stream().map(EnvioResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnvioResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(envioService.listarPorUsuario(usuarioId).stream().map(EnvioResponse::from).toList());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EnvioResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(envioService.listarPorEstado(estado).stream().map(EnvioResponse::from).toList());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(EnvioResponse.from(envioService.actualizarEstado(id, body.get("estado"))));
    }

    @PatchMapping("/{id}/guia")
    public ResponseEntity<EnvioResponse> registrarGuia(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(EnvioResponse.from(envioService.registrarGuia(id, body.get("numeroGuia"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Envio toEntity(EnvioRequest request) {
        Envio envio = new Envio();
        envio.setCompraId(request.compraId());
        envio.setUsuarioId(request.usuarioId());
        envio.setDireccionEntrega(request.direccionEntrega());
        envio.setCiudad(request.ciudad());
        envio.setCodigoPostal(request.codigoPostal());
        envio.setEmpresaTransporte(request.empresaTransporte());
        envio.setFechaEntregaEstimada(request.fechaEntregaEstimada());
        return envio;
    }
}
