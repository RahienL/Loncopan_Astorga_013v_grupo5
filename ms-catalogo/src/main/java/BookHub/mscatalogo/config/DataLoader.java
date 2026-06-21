package BookHub.mscatalogo.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import BookHub.mscatalogo.entities.Categoria;
import BookHub.mscatalogo.entities.Producto;
import BookHub.mscatalogo.repositories.CategoriaRepository;
import BookHub.mscatalogo.repositories.ProductoRepository;

@Component
@ConditionalOnProperty(name = "app.data-loader.enabled", havingValue = "true", matchIfMissing = true)
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final Faker faker;
    private final Random random;

    public DataLoader(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.faker = new Faker(Locale.forLanguageTag("es-CL"));
        this.random = new Random();
    }

    @Override
    public void run(String... args) {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            categorias = crearCategoriasIniciales();
        }

        if (productoRepository.count() == 0 && !categorias.isEmpty()) {
            crearProductosIniciales(categorias, 30);
        }
    }

    private List<Categoria> crearCategoriasIniciales() {
        List<String> nombres = List.of(
                "Novela",
                "Fantasia",
                "Ciencia Ficcion",
                "Historia",
                "Tecnologia",
                "Negocios",
                "Infantil",
                "Misterio"
        );

        List<Categoria> categorias = new ArrayList<>();
        for (String nombre : nombres) {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(truncate(faker.lorem().sentence(12), 500));
            categorias.add(categoria);
        }

        return categoriaRepository.saveAll(categorias);
    }

    private void crearProductosIniciales(List<Categoria> categorias, int cantidad) {
        List<Producto> productos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            Producto producto = new Producto();
            producto.setNombre(generarNombreProducto());
            producto.setDescripcion(generarDescripcionProducto());
            producto.setPrecio(generarPrecio());
            producto.setStock(faker.number().numberBetween(0, 120));
            producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
            producto.setImagenUrl("https://picsum.photos/seed/" + UUID.randomUUID() + "/600/900");
            producto.setEstado(faker.bool().bool() ? "activo" : "inactivo");
            productos.add(producto);
        }

        productoRepository.saveAll(productos);
    }

    private String generarNombreProducto() {
        String base = faker.book().title();
        if (base == null || base.isBlank()) {
            base = "Libro de prueba";
        }

        String nombre = base.trim();
        if (nombre.length() < 2) {
            nombre = nombre + " libro";
        }

        return truncate(nombre, 200);
    }

    private String generarDescripcionProducto() {
        String descripcion = faker.lorem().paragraph(3);
        if (descripcion == null || descripcion.isBlank()) {
            descripcion = "Descripcion de producto de prueba generada automaticamente.";
        }

        if (descripcion.length() < 10) {
            descripcion = descripcion + " contenido adicional para cumplir longitud minima.";
        }

        return truncate(descripcion, 2000);
    }

    private BigDecimal generarPrecio() {
        double valor = faker.number().randomDouble(2, 3990, 59990);
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }

        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
}
