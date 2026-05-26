package BookHub.msreportes.controllers;

import BookHub.msreportes.dto.request.ReporteRequest;
import BookHub.msreportes.dto.response.ReporteResponse;
import BookHub.msreportes.entities.Reporte;
import BookHub.msreportes.services.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<ReporteResponse> crear(@Valid @RequestBody ReporteRequest request) {
        return ResponseEntity.ok(ReporteResponse.from(reporteService.crear(toEntity(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ReporteResponse.from(reporteService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponse>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos().stream().map(ReporteResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReporteResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reporteService.listarPorUsuario(usuarioId).stream().map(ReporteResponse::from).toList());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ReporteResponse>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(reporteService.listarPorTipo(tipo).stream().map(ReporteResponse::from).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Reporte toEntity(ReporteRequest request) {
        Reporte reporte = new Reporte();
        reporte.setUsuarioId(request.usuarioId());
        reporte.setTipo(request.tipo());
        reporte.setContenido(request.contenido());
        return reporte;
    }
}
