# Sistema de Pedidos Cafetería — Tarea POO (Semana 3)

<!--toc:start-->
- [Sistema de Pedidos Cafetería — Tarea POO (Semana 3)](#sistema-de-pedidos-cafetería-tarea-poo-semana-3)
  - [Descripción breve](#descripción-breve)
  - [Objetivo del proyecto](#objetivo-del-proyecto)
  - [Principales funcionalidades](#principales-funcionalidades)
  - [Estructura de clases](#estructura-de-clases)
  - [Instrucciones básicas para ejecutar](#instrucciones-básicas-para-ejecutar)
  - [Tecnología](#tecnología)
  - [Autor](#autor)
<!--toc:end-->

Sistema de gestión de pedidos para una cafetería desarrollado en Java, creado como tarea para la asignatura de Programación Orientada a Objetos. Permite registrar productos, clientes y armar pedidos calculando el total con descuentos según el tipo de cliente.

## Descripción breve

Aplicación de consola en Java que permite armar pedidos de una cafetería y calcular el total a pagar aplicando descuentos de forma polimórfica según el tipo de cliente: mayorista o minorista.

## Objetivo del proyecto

Aplicar los principios de la Programación Orientada a Objetos —abstracción, polimorfismo, herencia, composición y encapsulación— modelando un sistema de pedidos extensible para distintos tipos de cliente y producto.

## Principales funcionalidades

- Catálogo de productos (comida y bebida) con validaciones de precio y stock.
- Registro de clientes mayoristas y minoristas.
- Armado de pedidos con múltiples ítems (producto + cantidad).
- Cálculo de subtotal, descuento y total final mediante polimorfismo:
  - `ClienteMayorista` → 15% de descuento fijo.
  - `ClienteMinorista` → 5% de descuento solo si tiene tarjeta de fidelidad y el subtotal supera los $10.
- Resumen del pedido impreso en consola.

## Estructura de clases

| Clase | Responsabilidad |
| --- | --- |
| `Main` | Punto de entrada; crea productos, clientes y pedidos de ejemplo. |
| `Producto` (abstracta) | Atributos comunes `nombre`, `precioBase`, `stock` con validaciones. |
| `ProductoComida` | Extiende `Producto`; agrega `esAptoCeliacos`. |
| `ProductoBebida` | Extiende `Producto`; agrega `tamanoMl`. |
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
