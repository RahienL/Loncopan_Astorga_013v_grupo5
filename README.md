# Loncopan_Astorga_013v_grupo5
Proyecto Fullstack
 Modelo entidad-relación del sistema. Contiene el script SQL con todas las tablas, índices y relaciones lógicas inter-microservicios.

 ## 🗂️ Modelo de Base de Datos

El modelo completo se encuentra en:

```
ProyectoBookHub/bookhub_modelo_er.sql
```

Cada microservicio opera sobre su propia base de datos aislada. Las relaciones entre microservicios son **lógicas** (declaradas con `DISABLE CONSTRAINTS`) para respetar la independencia del dominio.

---

## 🔗 Relaciones Lógicas Inter-Microservicios

```
USUARIOS ──► COMPRAS, COMPRAS_TEMPORALES, NOTIFICACIONES,
             PAGOS, ENVIOS, RESENAS, RECOMENDACIONES, REPORTES

PRODUCTOS ──► DETALLE_COMPRA, DETALLE_COMPRA_TEMPORAL,
              INVENTARIO, RESENAS, RECOMENDACIONES

COMPRAS ──► DETALLE_COMPRA, PAGOS, ENVIOS
