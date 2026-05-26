package BookHub.msordenes.dto.response;

public record CompraResponse(
    boolean success,
    String message,
    CompraDto compra
) {}
