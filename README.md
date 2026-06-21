# BookHub — Backend de Microservicios

Plataforma de e-commerce de libros construida con arquitectura de microservicios. Cada dominio del negocio es un servicio independiente con su propia base de datos, todos orquestados mediante Docker Compose.

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 25 |
| Framework | Spring Boot 3.5.6 |
| Seguridad | Spring Security + JWT (stateless) |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |
| Contenedores | Docker & Docker Compose |
| Build | Maven (Maven Wrapper incluido) |
| Documentación API | Springdoc OpenAPI / Swagger UI |
| Generación de PDFs | iText |

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    Cliente (Frontend)                   │
│               Vite / React  localhost:5173              │
└───────────────────────┬─────────────────────────────────┘
                        │ HTTP + JWT
        ┌───────────────▼────────────────────────┐
        │           Microservicios               │
        │                                        │
        │  ms-usuarios      :8081                │
        │  ms-ordenes        :8083  ──► ms-catalogo (stock) │
        │  ms-catalogo       :8084                │
        │  ms-pagos          :8085                │
        │  ms-inventario     :8086                │
        │  ms-notificaciones :8087                │
        │  ms-envios         :8088                │
        │  ms-resenas        :8089                │
        │  ms-reportes       :8091                │
        │  ms-recomendaciones:8092                │
        └───────────────┬────────────────────────┘
                        │
        ┌───────────────▼────────────────────────┐
        │         MySQL 8.0   :3306              │
        │  (una base de datos por microservicio) │
        └────────────────────────────────────────┘
```

---

## Microservicios

| Microservicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| `ms-usuarios` | 8081 | `bbdd_usuarios` | Registro, login y gestión de usuarios. Genera tokens JWT. |
| `ms-ordenes` | 8083 | `bbdd_ordenes` | Carrito de compras, confirmación de órdenes y generación de facturas PDF. Se comunica con `ms-catalogo` para verificar y descontar stock. |
| `ms-catalogo` | 8084 | `bbdd_catalogo` | Catálogo de productos y categorías. Permite subida de imágenes de portada. |
| `ms-pagos` | 8085 | `bbdd_pagos` | Registro y consulta de pagos. |
| `ms-inventario` | 8086 | `bbdd_inventario` | Control de inventario y movimientos de stock. |
| `ms-notificaciones` | 8087 | `bbdd_notificaciones` | Gestión y envío de notificaciones internas. |
| `ms-envios` | 8088 | `bbdd_envios` | Seguimiento y gestión de envíos. |
| `ms-resenas` | 8089 | `bbdd_resenas` | Reseñas y valoraciones de productos. |
| `ms-reportes` | 8091 | `bbdd_reportes` | Generación de reportes de negocio. |
| `ms-recomendaciones` | 8092 | `bbdd_recomendaciones` | Motor de recomendaciones de productos. |

---

## Seguridad

- Autenticación mediante **JWT (JSON Web Tokens)** con algoritmo HS256.
- `ms-usuarios` es el único servicio que **genera** tokens (login).
- El resto de servicios **validan** el token en cada request mediante un filtro `JwtAuthenticationFilter`.
- El secreto JWT se comparte entre todos los servicios a través de la variable de entorno `JWT_SECRET`.
- Las contraseñas se almacenan con **BCrypt**.
- Sesiones **stateless** (sin HttpSession).
- CORS configurado para `http://localhost:5173` y `http://127.0.0.1:5173`.

### Endpoints públicos por servicio

| Servicio | Endpoints públicos |
|---|---|
| `ms-usuarios` | `POST /api/auth/**` (login, registro) |
| `ms-catalogo` | `GET /api/productos/**`, `GET /api/categorias/**`, `/uploads/**` |
| `ms-inventario` | `GET /api/inventario/**` |
| `ms-envios` | `GET /api/envios/**` |
| `ms-notificaciones` | `GET /api/notificaciones/**` |
| `ms-resenas` | `GET /api/resenas/**` |
| `ms-recomendaciones` | `GET /api/recomendaciones/**` |
| `ms-reportes` | `GET /api/reportes/**` |
| Todos | `/swagger-ui/**`, `/v3/api-docs/**` |

---

## Estructura del repositorio

```
microservices/
├── docker-compose.yml          # Orquestación de todos los servicios
├── init.sql                    # Script de inicialización de bases de datos
├── postman/
│   └── BookHub-microservices.postman_collection.json
├── ms-usuarios/
├── ms-catalogo/
├── ms-ordenes/
├── ms-pagos/
├── ms-inventario/
├── ms-notificaciones/
├── ms-envios/
├── ms-resenas/
├── ms-recomendaciones/
└── ms-reportes/
```

Cada microservicio sigue la misma estructura interna:

```
ms-xxx/
├── Dockerfile
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/java/BookHub/msxxx/
    ├── config/         # SecurityConfig, CORS, etc.
    ├── controllers/    # REST controllers
    ├── services/       # Lógica de negocio
    ├── repositories/   # Spring Data JPA
    ├── entities/       # Entidades JPA
    └── security/       # JwtUtils, JwtAuthenticationFilter
```

---

## Requisitos previos

- **Docker Desktop** instalado y en ejecución.
- **Docker Compose** v2+ (incluido en Docker Desktop).
- Puerto `3306` libre para MySQL.
- Puertos `8081`, `8083–8092` libres.

---

## Puesta en marcha con Docker Compose

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd microservices
```

### 2. Configurar variables de entorno

Crea un archivo `.env` en la raíz del proyecto (mismo nivel que `docker-compose.yml`):

```env
JWT_SECRET=tu_secreto_base64_muy_seguro_de_al_menos_32_caracteres
```

> **Importante:** El valor de `JWT_SECRET` debe ser idéntico para todos los microservicios. Si lo omites, los servicios fallarán al iniciar.

### 3. Levantar todos los servicios

```bash
docker compose up --build
```

Docker Compose:
1. Construye las imágenes JAR de cada microservicio.
2. Arranca MySQL y espera a que el healthcheck pase.
3. Inicia todos los microservicios en paralelo.
4. Ejecuta `init.sql` para crear las bases de datos y datos semilla.

### 4. Verificar que todo esté corriendo

```bash
docker compose ps
```

Para ver los logs de un servicio específico:

```bash
docker compose logs -f ms-catalogo
```

### 5. Detener los servicios

```bash
docker compose down
```

Para eliminar también los volúmenes (datos de MySQL):

```bash
docker compose down -v
```

---

## Ejecución local (sin Docker)

Requiere: **JDK 21**, **Maven 3.9+** y una instancia de **MySQL 8** en `localhost:3306`.

```bash
cd ms-catalogo
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-DJWT_SECRET=tu_secreto"
```

Repite para cada microservicio en terminales separadas.

---

## Documentación de la API (Swagger)

Cada microservicio expone su propia documentación OpenAPI. Una vez levantados:

| Servicio | Swagger UI |
|---|---|
| ms-usuarios | http://localhost:8081/swagger-ui/index.html |
| ms-ordenes | http://localhost:8083/swagger-ui/index.html |
| ms-catalogo | http://localhost:8084/swagger-ui/index.html |
| ms-pagos | http://localhost:8085/swagger-ui/index.html |
| ms-inventario | http://localhost:8086/swagger-ui/index.html |
| ms-notificaciones | http://localhost:8087/swagger-ui/index.html |
| ms-envios | http://localhost:8088/swagger-ui/index.html |
| ms-resenas | http://localhost:8089/swagger-ui/index.html |
| ms-reportes | http://localhost:8091/swagger-ui/index.html |
| ms-recomendaciones | http://localhost:8092/swagger-ui/index.html |

Endpoints OpenAPI (JSON):

| Servicio | OpenAPI JSON |
|---|---|
| ms-usuarios | http://localhost:8081/v3/api-docs |
| ms-ordenes | http://localhost:8083/v3/api-docs |
| ms-catalogo | http://localhost:8084/v3/api-docs |
| ms-pagos | http://localhost:8085/v3/api-docs |
| ms-inventario | http://localhost:8086/v3/api-docs |
| ms-notificaciones | http://localhost:8087/v3/api-docs |
| ms-envios | http://localhost:8088/v3/api-docs |
| ms-resenas | http://localhost:8089/v3/api-docs |
| ms-reportes | http://localhost:8091/v3/api-docs |
| ms-recomendaciones | http://localhost:8092/v3/api-docs |

Para endpoints protegidos por JWT:
1. Inicia sesión en ms-usuarios y obtén el token.
2. Abre Swagger UI del microservicio.
3. Presiona Authorize e ingresa: Bearer TU_TOKEN.

---

## Colección Postman

En la carpeta `postman/` se incluye la colección **BookHub Microservices** lista para importar en Postman.

Configura la variable de entorno de Postman:

| Variable | Valor |
|---|---|
| `catalogoUrl` | `http://localhost:8084` |
| `usuariosUrl` | `http://localhost:8081` |
| `ordenesUrl` | `http://localhost:8083` |

Flujo básico de prueba:
1. `POST /api/auth/login` en `ms-usuarios` → obtener el token JWT.
2. Copiar el token en el header `Authorization: Bearer <token>`.
3. Realizar las demás peticiones autenticadas.

---

## Comunicación entre servicios

`ms-ordenes` se comunica de forma síncrona con `ms-catalogo` mediante **RestTemplate** para:
- Verificar disponibilidad de stock antes de confirmar una compra.
- Descontar el stock una vez confirmada la orden.

El endpoint interno es:
```
POST http://ms-catalogo:8084/api/productos/{id}/descontar-stock
```

La URL base de `ms-catalogo` se inyecta mediante la variable de entorno `MS_CATALOGO_URL`.

---

## Variables de entorno relevantes

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `JWT_SECRET` | Secreto HMAC-SHA256 para firmar/validar JWT | — (obligatorio) |
| `JWT_EXPIRATION` | Duración del token en ms | `86400000` (24 h) |
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos | configurado en docker-compose |
| `SPRING_DATASOURCE_USERNAME` | Usuario de MySQL | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de MySQL | `root123` |
| `MS_CATALOGO_URL` | URL base de ms-catalogo (solo en ms-ordenes) | `http://ms-catalogo:8084` |

> En producción, reemplaza las credenciales por defecto y usa un gestor de secretos.

---

## Notas de desarrollo

- El frontend esperado corre en `http://localhost:5173` (proyecto Vite/React).
- Las imágenes de portada de productos se almacenan en el volumen Docker `catalogo_uploads` y se sirven en `/uploads/**`.
- Las facturas se generan en PDF usando iText y se devuelven como `application/pdf`.
- Todos los servicios usan zona horaria UTC.
