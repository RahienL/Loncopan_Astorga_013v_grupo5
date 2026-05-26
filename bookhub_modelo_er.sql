-- ============================================================
--  BookHub - Modelo Entidad Relacion
--  Compatible con Oracle SQL Developer - Vista Lógica e Inter-MS
-- ============================================================

-- ------------------------------------------------------------
--  bbdd_usuarios  |  ms-usuarios
-- ------------------------------------------------------------
CREATE TABLE USUARIOS (
    id                NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre            VARCHAR2(100)  NOT NULL,
    email             VARCHAR2(150)  NOT NULL UNIQUE,
    password          VARCHAR2(255)  NOT NULL,
    rol               VARCHAR2(50)   NOT NULL,
    estado            VARCHAR2(30)   NOT NULL,
    fecha_creacion    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- ------------------------------------------------------------
--  bbdd_catalogo  |  ms-catalogo
-- ------------------------------------------------------------
CREATE TABLE CATEGORIAS (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre       VARCHAR2(100)  NOT NULL UNIQUE,
    descripcion  VARCHAR2(500)
);

CREATE TABLE PRODUCTOS (
    id             NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre         VARCHAR2(200)  NOT NULL,
    descripcion    VARCHAR2(1000),
    precio         NUMBER(12, 2)  NOT NULL,
    stock          NUMBER(10)     NOT NULL,
    categoria_id   NUMBER(19)     NOT NULL,
    imagen_url     VARCHAR2(500),
    estado         VARCHAR2(30)   NOT NULL,
    fecha_creacion TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id)
        REFERENCES CATEGORIAS (id)
);

-- ------------------------------------------------------------
--  bbdd_ordenes  |  ms-ordenes
-- ------------------------------------------------------------
CREATE TABLE COMPRAS (
    id               NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id       NUMBER(19)    NOT NULL,
    usuario_nombre   VARCHAR2(100) NOT NULL,
    usuario_email    VARCHAR2(150) NOT NULL,
    total            NUMBER(12, 2) NOT NULL,
    estado           VARCHAR2(30)  NOT NULL,
    numero_factura   VARCHAR2(50)  NOT NULL UNIQUE,
    metodo_pago      VARCHAR2(50),
    fecha_compra     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE DETALLE_COMPRA (
    id               NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compra_id        NUMBER(19)    NOT NULL,
    producto_id      NUMBER(19)    NOT NULL,
    producto_nombre  VARCHAR2(200) NOT NULL,
    producto_imagen  VARCHAR2(500),
    cantidad         NUMBER(10)    NOT NULL,
    precio_unitario  NUMBER(12, 2) NOT NULL,
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id)
        REFERENCES COMPRAS (id)
);

CREATE TABLE COMPRAS_TEMPORALES (
    id               NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id       NUMBER(19)    NOT NULL,
    usuario_nombre   VARCHAR2(100) NOT NULL,
    total            NUMBER(12, 2) NOT NULL,
    estado           VARCHAR2(30)  NOT NULL,
    session_id       VARCHAR2(100),
    fecha_creacion   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_expiracion TIMESTAMP
);

CREATE TABLE DETALLE_COMPRA_TEMPORAL (
    id                    NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compra_temporal_id    NUMBER(19)    NOT NULL,
    producto_id           NUMBER(19)    NOT NULL,
    producto_nombre       VARCHAR2(200) NOT NULL,
    producto_imagen       VARCHAR2(500),
    cantidad              NUMBER(10)    NOT NULL,
    precio_unitario       NUMBER(12, 2) NOT NULL,
    CONSTRAINT fk_detalle_compra_temp FOREIGN KEY (compra_temporal_id)
        REFERENCES COMPRAS_TEMPORALES (id)
);

-- ------------------------------------------------------------
--  bbdd_inventario  |  ms-inventario
-- ------------------------------------------------------------
CREATE TABLE INVENTARIO (
    id                  NUMBER(19)  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id         NUMBER(19)  NOT NULL UNIQUE,
    stock               NUMBER(10)  NOT NULL,
    stock_minimo        NUMBER(10)  NOT NULL,
    estado              VARCHAR2(30) NOT NULL,
    fecha_creacion      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP
);

-- ------------------------------------------------------------
--  bbdd_notificaciones  |  ms-notificaciones
-- ------------------------------------------------------------
CREATE TABLE NOTIFICACIONES (
    id             NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id     NUMBER(19)    NOT NULL,
    email_destino  VARCHAR2(150) NOT NULL,
    asunto         VARCHAR2(200),
    mensaje        VARCHAR2(2000),
    tipo           VARCHAR2(50)  NOT NULL,
    estado         VARCHAR2(30)  NOT NULL,
    leida          NUMBER(1)     DEFAULT 0 NOT NULL,
    fecha_creacion TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_envio    TIMESTAMP
);

-- ------------------------------------------------------------
--  bbdd_pagos  |  ms-pagos
-- ------------------------------------------------------------
CREATE TABLE PAGOS (
    id                      NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compra_id               NUMBER(19)    NOT NULL UNIQUE,
    usuario_id              NUMBER(19)    NOT NULL,
    monto                   NUMBER(12, 2) NOT NULL,
    metodo_pago             VARCHAR2(50)  NOT NULL,
    estado                  VARCHAR2(30)  NOT NULL,
    referencia_transaccion  VARCHAR2(100) NOT NULL UNIQUE,
    fecha_pago              TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion     TIMESTAMP
);

-- ------------------------------------------------------------
--  bbdd_envios  |  ms-envios
-- ------------------------------------------------------------
CREATE TABLE ENVIOS (
    id                      NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compra_id               NUMBER(19)    NOT NULL UNIQUE,
    usuario_id              NUMBER(19)    NOT NULL,
    direccion_entrega       VARCHAR2(300) NOT NULL,
    ciudad                  VARCHAR2(100) NOT NULL,
    codigo_postal           VARCHAR2(20),
    empresa_transporte      VARCHAR2(100),
    numero_guia             VARCHAR2(100) UNIQUE,
    estado                  VARCHAR2(30)  NOT NULL,
    fecha_creacion          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_entrega_estimada  TIMESTAMP,
    fecha_entrega_real      TIMESTAMP
);

-- ------------------------------------------------------------
--  bbdd_resenas  |  ms-resenas
-- ------------------------------------------------------------
CREATE TABLE RESENAS (
    id                  NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id         NUMBER(19)    NOT NULL,
    usuario_id          NUMBER(19)    NOT NULL,
    titulo              VARCHAR2(200),
    comentario          VARCHAR2(2000),
    calificacion        NUMBER(2)     NOT NULL,
    estado              VARCHAR2(30)  NOT NULL,
    fecha_creacion      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP,
    CONSTRAINT chk_calificacion CHECK (calificacion BETWEEN 1 AND 5)
);

-- ------------------------------------------------------------
--  bbdd_recomendaciones  |  ms-recomendaciones
-- ------------------------------------------------------------
CREATE TABLE RECOMENDACIONES (
    id                  NUMBER(19)     GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id          NUMBER(19)     NOT NULL,
    producto_id         NUMBER(19)     NOT NULL,
    puntaje             BINARY_DOUBLE  NOT NULL,
    motivo              VARCHAR2(300),
    estado              VARCHAR2(30)   NOT NULL,
    fecha_creacion      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP
);

-- ------------------------------------------------------------
--  bbdd_reportes  |  ms-reportes
-- ------------------------------------------------------------
CREATE TABLE REPORTES (
    id                  NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id          NUMBER(19)    NOT NULL,
    tipo                VARCHAR2(50)  NOT NULL,
    contenido           CLOB,
    estado              VARCHAR2(30)  NOT NULL,
    fecha_creacion      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP
);

-- ============================================================
--  Índices de Rendimiento
-- ============================================================
CREATE INDEX idx_productos_categoria   ON PRODUCTOS       (categoria_id);
CREATE INDEX idx_detalle_compra        ON DETALLE_COMPRA     (compra_id);
CREATE INDEX idx_detalle_temp          ON DETALLE_COMPRA_TEMPORAL (compra_temporal_id);
CREATE INDEX idx_compras_usuario       ON COMPRAS            (usuario_id);
CREATE INDEX idx_pagos_usuario         ON PAGOS              (usuario_id);
CREATE INDEX idx_envios_usuario        ON ENVIOS             (usuario_id);
CREATE INDEX idx_notif_usuario         ON NOTIFICACIONES     (usuario_id);
CREATE INDEX idx_resenas_producto      ON RESENAS            (producto_id);
CREATE INDEX idx_resenas_usuario       ON RESENAS            (usuario_id);
CREATE INDEX idx_recom_usuario         ON RECOMENDACIONES    (usuario_id);
CREATE INDEX idx_recom_producto        ON RECOMENDACIONES    (producto_id);
CREATE INDEX idx_reportes_usuario      ON REPORTES           (usuario_id);


-- ============================================================
--  MAPEADO DE RELACIONES LÓGICAS INTER-MICROSERVICIOS
--  (Instrucciones de enlace declarativo para el Data Modeler)
-- ============================================================

-- Relaciones de USUARIOS con otros Microservicios
ALTER TABLE COMPRAS ADD CONSTRAINT fk_logica_compras_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE COMPRAS_TEMPORALES ADD CONSTRAINT fk_logica_comprastmp_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE NOTIFICACIONES ADD CONSTRAINT fk_logica_notif_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE PAGOS ADD CONSTRAINT fk_logica_pagos_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE ENVIOS ADD CONSTRAINT fk_logica_envios_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE RESENAS ADD CONSTRAINT fk_logica_resenas_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE RECOMENDACIONES ADD CONSTRAINT fk_logica_recom_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;
ALTER TABLE REPORTES ADD CONSTRAINT fk_logica_reportes_usuarios FOREIGN KEY (usuario_id) REFERENCES USUARIOS (id) DISABLE CONSTRAINTS;

-- Relaciones de PRODUCTOS con otros Microservicios
ALTER TABLE DETALLE_COMPRA ADD CONSTRAINT fk_logica_detcompra_productos FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (id) DISABLE CONSTRAINTS;
ALTER TABLE DETALLE_COMPRA_TEMPORAL ADD CONSTRAINT fk_logica_dettmp_productos FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (id) DISABLE CONSTRAINTS;
ALTER TABLE INVENTARIO ADD CONSTRAINT fk_logica_inventario_productos FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (id) DISABLE CONSTRAINTS;
ALTER TABLE RESENAS ADD CONSTRAINT fk_logica_resenas_productos FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (id) DISABLE CONSTRAINTS;
ALTER TABLE RECOMENDACIONES ADD CONSTRAINT fk_logica_recom_productos FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (id) DISABLE CONSTRAINTS;

-- Relaciones cruzadas operacionales
ALTER TABLE PAGOS ADD CONSTRAINT fk_logica_pagos_compras FOREIGN KEY (compra_id) REFERENCES COMPRAS (id) DISABLE CONSTRAINTS;
ALTER TABLE ENVIOS ADD CONSTRAINT fk_logica_envios_compras FOREIGN KEY (compra_id) REFERENCES COMPRAS (id) DISABLE CONSTRAINTS;