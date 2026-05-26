package BookHub.msreportes.dto.response;

import BookHub.msreportes.entities.Reporte;

import java.time.LocalDateTime;

public record ReporteResponse(
    Long id,
    Long usuarioId,
    String tipo,
    String contenido,
    LocalDateTime generadoEn,
    LocalDateTime fechaActualizacion
) {

    public static ReporteResponse from(Reporte reporte) {
        return new ReporteResponse(
            reporte.getId(),
            reporte.getUsuarioId(),
            reporte.getTipo(),
            reporte.getContenido(),
            reporte.getGeneradoEn(),
            reporte.getFechaActualizacion()
        );
    }
}
