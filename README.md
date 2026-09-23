# Sistema de Pedidos Cafetería — Tarea POO

<!--toc:start-->
- [Sistema de Pedidos Cafetería — Tarea POO](#sistema-de-pedidos-cafetería--tarea-poo)
  - [Descripción breve](#descripción-breve)
  - [Objetivo del proyecto](#objetivo-del-proyecto)
  - [Principales funcionalidades](#principales-funcionalidades)
  - [Catálogo y colecciones](#catálogo-y-colecciones)
  - [Estructura de clases](#estructura-de-clases)
  - [Instrucciones básicas para ejecutar](#instrucciones-básicas-para-ejecutar)
  - [Tecnología](#tecnología)
  - [Autor](#autor)
<!--toc:end-->

Sistema de gestión de pedidos para una cafetería desarrollado en Java, creado como tarea para la asignatura de Programación Orientada a Objetos. Permite administrar un catálogo de productos con colecciones genéricas, registrar clientes y armar pedidos calculando el total con descuentos según el tipo de cliente.

## Descripción breve

Aplicación de consola en Java que administra un catálogo de productos usando `ArrayList`, `HashMap` y `HashSet`, y que permite armar pedidos calculando el total a pagar con descuentos aplicados de forma polimórfica según el tipo de cliente: mayorista o minorista.

## Objetivo del proyecto

Aplicar los principios de la Programación Orientada a Objetos —abstracción, polimorfismo, herencia, composición y encapsulación— junto con el uso de colecciones genéricas para almacenar, buscar y evitar duplicados en la información.

## Principales funcionalidades

- Catálogo de productos (comida y bebida) con validaciones de precio y stock.
- Operaciones de catálogo: agregar, buscar, listar, actualizar y eliminar productos.
- Prevención de productos duplicados mediante una clave única (`codigo`) y `HashSet`.
- Registro de clientes mayoristas y minoristas.
- Armado de pedidos con múltiples ítems (producto + cantidad).
- Cálculo de subtotal, descuento y total final mediante polimorfismo:
  - `ClienteMayorista` → 15% de descuento fijo.
  - `ClienteMinorista` → 5% de descuento solo si tiene tarjeta de fidelidad y el subtotal supera los $10.
- Resumen del pedido impreso en consola.

## Catálogo y colecciones

La clase `CatalogoProductos` administra tres colecciones genéricas, cada una con una responsabilidad concreta:

| Colección | Declaración | Uso |
| --- | --- | --- |
| `HashMap` | `Map<String, Producto>` | Acceso y búsqueda directa por código; actualización y eliminación en O(1). |
| `HashSet` | `Set<Producto>` | Evita registros duplicados usando `equals()`/`hashCode()` de `Producto` (basados en `codigo`). |
| `ArrayList` | `List<Producto>` | Mantiene el orden de inserción para listar el catálogo y filtrar por tipo. |

Operaciones disponibles:

| Operación | Método |
| --- | --- |
| Agregar | `agregar(Producto)` — lanza excepción si el código ya existe. |
| Buscar por código | `buscarPorCodigo(String)` |
| Buscar por nombre | `buscarPorNombre(String)` — coincidencia parcial sin distinguir mayúsculas. |
| Listar | `listar()` / `mostrarCatalogo()` |
| Listar por tipo | `listarPorTipo(Class<? extends Producto>)` |
| Actualizar | `actualizar(String codigo, String nombre, double precio, int stock)` |
| Eliminar | `eliminar(String codigo)` |
| Consultas | `cantidad()` / `estaVacio()` |

## Estructura de clases

| Clase | Responsabilidad |
| --- | --- |
| `Main` | Punto de entrada; ejecuta `demoCatalogo()` y `demoPedidos()` con datos de prueba. |
| `Producto` | Atributos comunes `codigo`, `nombre`, `precioBase`, `stock` con validaciones; define `equals`/`hashCode` por código. |
| `ProductoComida` | Extiende `Producto`; agrega `esAptoCeliacos`. |
| `ProductoBebida` | Extiende `Producto`; agrega `tamanoMl`. |
| `CatalogoProductos` | Administra el catálogo con `HashMap`, `HashSet` y `ArrayList`; operaciones CRUD y búsquedas. |
| `Cliente` (abstracta) | Atributos `nombre`, `email`, `saldoDisponible`; define `calcularDescuento`. |
| `ClienteMayorista` | Extiende `Cliente`; agrega `razonSocial`; descuento del 15%. |
| `ClienteMinorista` | Extiende `Cliente`; agrega `tieneTarjetaFidelidad`; descuento del 5%. |
| `ItemPedido` | Contiene un `Producto` y su `cantidad`; calcula su subtotal. |
| `Pedido` | Compone una lista de `ItemPedido` y un `Cliente`; calcula totales y muestra el resumen. |

## Instrucciones básicas para ejecutar

Requisitos previos: **Java** y **Maven**.

1. Compilar el proyecto:

   ```bash
   mvn clean package
   ```

2. Ejecutar la aplicación:

   ```bash
   mvn exec:java -Dexec.mainClass="com.universidad.cafeteria.Main"
   ```

## Tecnología

- **Java**
- **Maven** (build)

## Autor

Christian Pacho
