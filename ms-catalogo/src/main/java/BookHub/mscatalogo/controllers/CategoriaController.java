package BookHub.mscatalogo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import BookHub.mscatalogo.dto.request.CategoriaRequest;
import BookHub.mscatalogo.dto.response.CategoriaResponse;
import BookHub.mscatalogo.entities.Categoria;
import BookHub.mscatalogo.services.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria(null, request.nombre(), request.descripcion(), null);
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.crear(categoria)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarTodas().stream().map(CategoriaResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        Categoria categoriaActualizada = new Categoria(null, request.nombre(), request.descripcion(), null);
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.actualizar(id, categoriaActualizada)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
