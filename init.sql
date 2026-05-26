-- ============================================================
--  MICROSERVICIOS - Script de inicialización de bases de datos
-- ============================================================

-- ─────────────────────────────────────────
--  1. bbdd_usuarios
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_usuarios;

CREATE TABLE IF NOT EXISTS usuarios (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100)  NOT NULL,
    email          VARCHAR(150)  NOT NULL UNIQUE,
    password       VARCHAR(255)  NOT NULL,
    rol            VARCHAR(20)   NOT NULL DEFAULT 'cliente',
    estado         VARCHAR(10)   NOT NULL DEFAULT 'activo',
    fecha_creacion DATETIME      NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos semilla (no invasivos) para ms-usuarios.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera nada.
INSERT INTO usuarios (id, nombre, email, password, rol, estado, fecha_creacion)
SELECT seed.id,
       seed.nombre,
       seed.email,
       seed.password,
       seed.rol,
       seed.estado,
       NOW()
FROM (
    SELECT 1 AS id,
           'Admin BookHub' AS nombre,
           'admin@bookhub.local' AS email,
           '$2a$10$S2Ff1cl3D8gqJQj6y8r4XO2x1/1ApjWQn8x1A6dM6Wq0TQf9c1G9K' AS password,
           'admin' AS rol,
           'activo' AS estado
    UNION ALL
    SELECT 2,
           'Cliente Demo',
           'cliente@bookhub.local',
           '$2a$10$S2Ff1cl3D8gqJQj6y8r4XO2x1/1ApjWQn8x1A6dM6Wq0TQf9c1G9K',
           'cliente',
           'activo'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios LIMIT 1
);

-- ─────────────────────────────────────────
--  2. bbdd_catalogo
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_catalogo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_catalogo;

CREATE TABLE IF NOT EXISTS categorias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)  NOT NULL UNIQUE,
    descripcion VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS productos (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(200)   NOT NULL,
    descripcion    VARCHAR(2000)  NOT NULL,
    precio         DECIMAL(12,2)  NOT NULL,
    stock          INT            NOT NULL,
    categoria_id   BIGINT         NOT NULL,
    imagen_url     VARCHAR(500),
    estado         VARCHAR(10)    NOT NULL DEFAULT 'activo',
    fecha_creacion DATETIME       NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- Datos semilla (no invasivos) para ms-catalogo.
-- Solo se insertan si la tabla esta vacia; si ya hay datos persistidos, no se toca nada.
INSERT INTO categorias (id, nombre, descripcion)
SELECT seed.id, seed.nombre, seed.descripcion
FROM (
    SELECT 1 AS id, 'Manga' AS nombre, 'Comics de origen japones' AS descripcion
    UNION ALL
    SELECT 2, 'Superheroes', 'Comics de Marvel y DC' AS descripcion
    UNION ALL
    SELECT 3, 'Novela Grafica', 'Historias en formato novela grafica' AS descripcion
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM categorias LIMIT 1
);

INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id, imagen_url, estado, fecha_creacion)
SELECT seed.id,
       seed.nombre,
       seed.descripcion,
       seed.precio,
       seed.stock,
       seed.categoria_id,
       seed.imagen_url,
       seed.estado,
       NOW()
FROM (
    SELECT 1 AS id,
        'One Piece Vol. 1' AS nombre,
        'Inicio de la aventura de Luffy y los Sombrero de Paja.' AS descripcion,
        12.90 AS precio,
        15 AS stock,
        1 AS categoria_id,
        'uploads/productos/one-piece-vol1.jpg' AS imagen_url,
           'activo' AS estado
    UNION ALL
    SELECT 2,
           'Spider-Man Blue',
        'Historia emotiva de Spider-Man con arte premium y tono clasico.',
           24.50,
        8,
        2,
           'uploads/productos/spiderman-blue.jpg',
           'activo'
    UNION ALL
    SELECT 3,
        'Watchmen',
        'Novela grafica fundamental con una historia oscura y adulta.',
        21.90,
        5,
        3,
        'uploads/productos/watchmen.jpg',
           'activo'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM productos LIMIT 1
);

-- ─────────────────────────────────────────
--  3. bbdd_ordenes
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_ordenes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_ordenes;

CREATE TABLE IF NOT EXISTS compras (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id     BIGINT         NOT NULL,
    usuario_nombre VARCHAR(200)   NOT NULL,
    usuario_email  VARCHAR(200)   NOT NULL,
    total          DECIMAL(12,2)  NOT NULL,
    estado         VARCHAR(30)    NOT NULL DEFAULT 'pendiente',
    numero_factura VARCHAR(50)    UNIQUE,
    metodo_pago    VARCHAR(50),
    fecha_compra   DATETIME       NOT NULL
);

CREATE TABLE IF NOT EXISTS detalle_compra (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_id        BIGINT         NOT NULL,
    producto_id      BIGINT         NOT NULL,
    producto_nombre  VARCHAR(200)   NOT NULL,
    producto_imagen  VARCHAR(500),
    cantidad         INT            NOT NULL,
    precio_unitario  DECIMAL(12,2)  NOT NULL,
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compras(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compras_temporales (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       BIGINT         NOT NULL,
    usuario_nombre   VARCHAR(200)   NOT NULL,
    total            DECIMAL(12,2)  DEFAULT 0.00,
    estado           VARCHAR(30)    NOT NULL DEFAULT 'activo',
    session_id       VARCHAR(100),
    fecha_creacion   DATETIME       NOT NULL,
    fecha_expiracion DATETIME
);

CREATE TABLE IF NOT EXISTS detalle_compra_temporal (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_temporal_id  BIGINT         NOT NULL,
    producto_id         BIGINT         NOT NULL,
    producto_nombre     VARCHAR(200)   NOT NULL,
    producto_imagen     VARCHAR(500),
    cantidad            INT            NOT NULL,
    precio_unitario     DECIMAL(12,2)  NOT NULL,
    CONSTRAINT fk_detalle_compra_temporal FOREIGN KEY (compra_temporal_id) REFERENCES compras_temporales(id) ON DELETE CASCADE
);

-- Datos semilla (no invasivos) para ms-ordenes.
-- Solo se insertan si las tablas estan vacias; no se modifican registros existentes.
INSERT INTO compras (id, usuario_id, usuario_nombre, usuario_email, total, estado, numero_factura, metodo_pago, fecha_compra)
SELECT seed.id,
       seed.usuario_id,
       seed.usuario_nombre,
       seed.usuario_email,
       seed.total,
       seed.estado,
       seed.numero_factura,
       seed.metodo_pago,
       NOW()
FROM (
    SELECT 1 AS id,
           2 AS usuario_id,
           'Cliente Demo' AS usuario_nombre,
           'cliente@bookhub.local' AS usuario_email,
           57.30 AS total,
           'confirmada' AS estado,
           'FAC-000001' AS numero_factura,
           'tarjeta' AS metodo_pago
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM compras LIMIT 1
);

INSERT INTO detalle_compra (id, compra_id, producto_id, producto_nombre, producto_imagen, cantidad, precio_unitario)
SELECT seed.id,
       seed.compra_id,
       seed.producto_id,
       seed.producto_nombre,
       seed.producto_imagen,
       seed.cantidad,
       seed.precio_unitario
FROM (
    SELECT 1 AS id,
           1 AS compra_id,
           1 AS producto_id,
           'Batman Year One' AS producto_nombre,
           'uploads/productos/batman-year-one.jpg' AS producto_imagen,
           1 AS cantidad,
           19.90 AS precio_unitario
    UNION ALL
    SELECT 2,
           1,
           2,
           'Spider-Man Blue',
           'uploads/productos/spiderman-blue.jpg',
           1,
           24.50
    UNION ALL
    SELECT 3,
           1,
           3,
           'One Piece Vol. 1',
           'uploads/productos/one-piece-vol1.jpg',
           1,
           12.90
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM detalle_compra LIMIT 1
)
AND EXISTS (
    SELECT 1 FROM compras WHERE id = 1
);

INSERT INTO compras_temporales (id, usuario_id, usuario_nombre, total, estado, session_id, fecha_creacion, fecha_expiracion)
SELECT seed.id,
       seed.usuario_id,
       seed.usuario_nombre,
       seed.total,
       seed.estado,
       seed.session_id,
       NOW(),
       DATE_ADD(NOW(), INTERVAL 1 DAY)
FROM (
    SELECT 1 AS id,
           2 AS usuario_id,
           'Cliente Demo' AS usuario_nombre,
           0.00 AS total,
           'activo' AS estado,
           'SESSION-DEMO-001' AS session_id
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM compras_temporales LIMIT 1
);

INSERT INTO detalle_compra_temporal (id, compra_temporal_id, producto_id, producto_nombre, producto_imagen, cantidad, precio_unitario)
SELECT seed.id,
       seed.compra_temporal_id,
       seed.producto_id,
       seed.producto_nombre,
       seed.producto_imagen,
       seed.cantidad,
       seed.precio_unitario
FROM (
    SELECT 1 AS id,
           1 AS compra_temporal_id,
           2 AS producto_id,
           'Spider-Man Blue' AS producto_nombre,
           'uploads/productos/spiderman-blue.jpg' AS producto_imagen,
           1 AS cantidad,
           24.50 AS precio_unitario
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM detalle_compra_temporal LIMIT 1
)
AND EXISTS (
    SELECT 1 FROM compras_temporales WHERE id = 1
);

-- ─────────────────────────────────────────
--  4. bbdd_inventario
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_inventario;

CREATE TABLE IF NOT EXISTS inventario (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id        BIGINT        NOT NULL UNIQUE,
    stock              INT           NOT NULL DEFAULT 0,
    stock_minimo       INT           NOT NULL DEFAULT 5,
    estado             VARCHAR(10)   NOT NULL DEFAULT 'activo',
    fecha_creacion     DATETIME      NOT NULL,
    fecha_actualizacion DATETIME     NOT NULL
);

-- Datos semilla (no invasivos) para pruebas locales de ms-inventario.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera ningun registro.
INSERT INTO inventario (producto_id, stock, stock_minimo, estado, fecha_creacion, fecha_actualizacion)
SELECT seed.producto_id,
       seed.stock,
       seed.stock_minimo,
       seed.estado,
       NOW(),
       NOW()
FROM (
    SELECT 1 AS producto_id, 20 AS stock, 5 AS stock_minimo, 'activo' AS estado
    UNION ALL
    SELECT 2, 3, 5, 'activo'
    UNION ALL
    SELECT 3, 0, 5, 'activo'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM inventario LIMIT 1
);

-- ─────────────────────────────────────────
--  5. bbdd_notificaciones
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_notificaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_notificaciones;

CREATE TABLE IF NOT EXISTS notificaciones (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id      BIGINT         NOT NULL,
    email_destino   VARCHAR(150)   NOT NULL,
    asunto          VARCHAR(200)   NOT NULL,
    mensaje         VARCHAR(2000)  NOT NULL,
    tipo            VARCHAR(20)    NOT NULL DEFAULT 'email',
    estado          VARCHAR(20)    NOT NULL DEFAULT 'pendiente',
    leida           BOOLEAN        NOT NULL DEFAULT FALSE,
    fecha_creacion  DATETIME       NOT NULL,
    fecha_envio     DATETIME
);

-- Datos semilla (no invasivos) para ms-notificaciones.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera nada.
INSERT INTO notificaciones (id, usuario_id, email_destino, asunto, mensaje, tipo, estado, leida, fecha_creacion, fecha_envio)
SELECT seed.id,
       seed.usuario_id,
       seed.email_destino,
       seed.asunto,
       seed.mensaje,
       seed.tipo,
       seed.estado,
       seed.leida,
       NOW(),
       seed.fecha_envio
FROM (
    SELECT 1 AS id,
           2 AS usuario_id,
           'cliente@bookhub.local' AS email_destino,
           'Compra confirmada' AS asunto,
           'Tu compra FAC-000001 fue confirmada correctamente.' AS mensaje,
           'email' AS tipo,
           'enviada' AS estado,
           FALSE AS leida,
           NOW() AS fecha_envio
    UNION ALL
    SELECT 2,
           2,
           'cliente@bookhub.local',
           'Producto con stock bajo',
           'El producto Spider-Man Blue tiene stock bajo en inventario.',
           'sistema',
           'pendiente',
           FALSE,
           NULL
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM notificaciones LIMIT 1
);

-- ─────────────────────────────────────────
--  6. bbdd_pagos
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_pagos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_pagos;

CREATE TABLE IF NOT EXISTS pagos (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_id               BIGINT         NOT NULL UNIQUE,
    usuario_id              BIGINT         NOT NULL,
    monto                   DECIMAL(12,2)  NOT NULL,
    metodo_pago             VARCHAR(20)    NOT NULL,
    estado                  VARCHAR(20)    NOT NULL DEFAULT 'pendiente',
    referencia_transaccion  VARCHAR(100)   UNIQUE,
    fecha_pago              DATETIME       NOT NULL,
    fecha_actualizacion     DATETIME       NOT NULL
);

-- Datos semilla (no invasivos) para ms-pagos.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera nada.
INSERT INTO pagos (id, compra_id, usuario_id, monto, metodo_pago, estado, referencia_transaccion, fecha_pago, fecha_actualizacion)
SELECT seed.id,
       seed.compra_id,
       seed.usuario_id,
       seed.monto,
       seed.metodo_pago,
       seed.estado,
       seed.referencia_transaccion,
       NOW(),
       NOW()
FROM (
    SELECT 1 AS id,
           1 AS compra_id,
           2 AS usuario_id,
           57.30 AS monto,
           'tarjeta' AS metodo_pago,
           'aprobado' AS estado,
           'TX-DEMO-0001' AS referencia_transaccion
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM pagos LIMIT 1
);

-- ─────────────────────────────────────────
--  7. bbdd_envios
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_envios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_envios;

CREATE TABLE IF NOT EXISTS envios (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_id               BIGINT         NOT NULL UNIQUE,
    usuario_id              BIGINT         NOT NULL,
    direccion_entrega       VARCHAR(255)   NOT NULL,
    ciudad                  VARCHAR(120)   NOT NULL,
    codigo_postal           VARCHAR(20)    NOT NULL,
    empresa_transporte      VARCHAR(80)    NOT NULL,
    numero_guia             VARCHAR(100)   UNIQUE,
    estado                  VARCHAR(20)    NOT NULL DEFAULT 'preparando',
    fecha_creacion          DATETIME       NOT NULL,
    fecha_entrega_estimada  DATETIME       NOT NULL,
    fecha_entrega_real      DATETIME
);

-- Datos semilla (no invasivos) para ms-envios.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera nada.
INSERT INTO envios (id, compra_id, usuario_id, direccion_entrega, ciudad, codigo_postal, empresa_transporte, numero_guia, estado, fecha_creacion, fecha_entrega_estimada, fecha_entrega_real)
SELECT seed.id,
       seed.compra_id,
       seed.usuario_id,
       seed.direccion_entrega,
       seed.ciudad,
       seed.codigo_postal,
       seed.empresa_transporte,
       seed.numero_guia,
       seed.estado,
       NOW(),
       DATE_ADD(NOW(), INTERVAL 3 DAY),
       NULL
FROM (
    SELECT 1 AS id,
           1 AS compra_id,
           2 AS usuario_id,
           'Av. Central 123' AS direccion_entrega,
           'Lima' AS ciudad,
           '15001' AS codigo_postal,
           'BookHub Express' AS empresa_transporte,
           'GUIA-0001' AS numero_guia,
           'en_camino' AS estado
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM envios LIMIT 1
);

-- ─────────────────────────────────────────
--  8. bbdd_resenas
-- ─────────────────────────────────────────
CREATE DATABASE IF NOT EXISTS bbdd_resenas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bbdd_resenas;

CREATE TABLE IF NOT EXISTS resenas (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id         BIGINT         NOT NULL,
    usuario_id          BIGINT         NOT NULL,
    titulo              VARCHAR(200)   NOT NULL,
    comentario          VARCHAR(2000)  NOT NULL,
    calificacion        INT            NOT NULL,
    estado              VARCHAR(20)    NOT NULL DEFAULT 'publicada',
    fecha_creacion      DATETIME       NOT NULL,
    fecha_actualizacion DATETIME       NOT NULL
);

-- Datos semilla (no invasivos) para ms-resenas.
-- Solo se insertan si la tabla esta vacia; si ya hay persistencia, no se altera nada.
INSERT INTO resenas (id, producto_id, usuario_id, titulo, comentario, calificacion, estado, fecha_creacion, fecha_actualizacion)
SELECT seed.id,
       seed.producto_id,
       seed.usuario_id,
       seed.titulo,
       seed.comentario,
       seed.calificacion,
       seed.estado,
       NOW(),
       NOW()
FROM (
    SELECT 1 AS id,
           1 AS producto_id,
           2 AS usuario_id,
           'Excelente comic' AS titulo,
            'Muy buena calidad de impresion y excelente historia.' AS comentario,
           5 AS calificacion,
           'publicada' AS estado
    UNION ALL
    SELECT 2,
           2,
           2,
           'Muy recomendado',
            'Gran narrativa y buen ritmo para leer en una tarde.' AS comentario,
           4,
           'publicada'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM resenas LIMIT 1
);
