# Sistema de Pedidos Cafetería — Tarea POO

<!--toc:start-->
- [Sistema de Pedidos Cafetería — Tarea POO](#sistema-de-pedidos-cafetería--tarea-poo)
  - [Descripción breve](#descripción-breve)
  - [Objetivo del proyecto](#objetivo-del-proyecto)
  - [Principales funcionalidades](#principales-funcionalidades)
  - [Catálogo y colecciones](#catálogo-y-colecciones)
  - [Cola de pedidos pendientes (estructura propia)](#cola-de-pedidos-pendientes-estructura-propia)
  - [Patrón Repository](#patrón-repository)
  - [Persistencia en archivo (JSON)](#persistencia-en-archivo-json)
  - [Pruebas unitarias (JUnit 5)](#pruebas-unitarias-junit-5)
  - [Interfaz gráfica (JavaFX)](#interfaz-gráfica-javafx)
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
- Interfaz gráfica JavaFX con CRUD completo del catálogo, validación de datos y mensajes de éxito/error.
- Persistencia del catálogo en JSON (`datos/catalogo.json`): carga al iniciar y guardado automático tras cada operación CRUD.

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

## Cola de pedidos pendientes (estructura propia)

Aplicación de consola (`MainConsola`) que administra los pedidos de la cafetería con una **cola FIFO** implementada manualmente (`Cola<T>`), sin usar `Queue`, `Deque`, `ArrayDeque` ni `LinkedList` de la biblioteca estándar. El primer pedido registrado es el primero en atenderse.

La estructura usa un arreglo circular (`T[] elementos`, índices `frente` y `fin`, más `cantidad`) y **crece automáticamente** al duplicar su capacidad cuando se llena.

| Operación | Método | Comportamiento |
| --- | --- | --- |
| Agregar | `encolar(T)` | Agrega al final; rechaza `null`. |
| Eliminar | `desencolar()` | Retira y devuelve el primero; lanza `ColaVaciaException` si está vacía. |
| Consultar siguiente | `frente()` | Devuelve el primero sin eliminarlo; lanza `ColaVaciaException` si está vacía. |
| ¿Está vacía? | `estaVacia()` | |
| Cantidad | `cantidad()` | |
| Listar | `listar()` | Copia en orden FIFO sin modificar la cola. |

Flujo de la aplicación: registrar pedido (cliente + productos del catálogo), atender el siguiente, ver el siguiente sin atender, consultar el estado y listar los pendientes.

## Patrón Repository

La lógica de consola no accede directamente a la cola: lo hace a través de `Repository<T>`, que separa el acceso a los datos de la aplicación.

| Tipo | Rol |
| --- | --- |
| `Repository<T>` | Interfaz genérica: `guardar`, `eliminar`, `consultar`, `estaVacio`, `cantidad`, `listar`. |
| `ColaRepository<T>` | Implementación en memoria que envuelve una `Cola<T>`. |
| `PedidosPendientes` | Servicio de dominio que usa `Repository<Pedido>` sin conocer la estructura interna. |

Si la estructura cambiara (por ejemplo, a una pila), solo se reemplaza la implementación de `Repository` y la lógica principal no se modifica.

## Persistencia en archivo (JSON)

El catálogo se guarda en `datos/catalogo.json` usando **Gson**. `CatalogoProductos` carga el archivo al construirse y guarda automáticamente después de cada operación CRUD, de modo que los cambios sobreviven al cierre de la aplicación.

| Tipo | Rol |
| --- | --- |
| `CatalogoRepository` | Interfaz de persistencia del catálogo: `cargar()` y `guardar(List<Producto>)`. |
| `CatalogoJsonRepository` | Implementación con Gson; escribe un campo `tipo` (`COMIDA`/`BEBIDA`) para conservar el subtipo y sus atributos propios al deserializar. |
| `PersistenciaException` | Excepción no verificada para errores de lectura/escritura del archivo. |

Comportamiento:

- Archivo inexistente → catálogo vacío; la GUI carga los productos de ejemplo y los persiste (primer arranque).
- Archivo corrupto o con tipos desconocidos → se informa con una alerta y se regenera con datos de ejemplo.
- El archivo se crea en la raíz del proyecto al ejecutar la GUI; la carpeta `datos/` está excluida de Git.

## Pruebas unitarias (JUnit 5)

| Clase | Qué comprueba |
| --- | --- |
| `ColaTest` | Orden FIFO, `frente()` no elimina, cola vacía lanza excepción, `estaVacia`/`cantidad`, rechazo de `null`, crecimiento automático y orden tras crecer. |
| `ColaRepositoryTest` | `guardar`, `eliminar`, `consultar`, `estaVacio`, `cantidad`, `listar` y eliminación en orden de llegada con objetos `Pedido`. |

## Interfaz gráfica (JavaFX)

Ventana de escritorio (`CatalogoApp` + `CatalogoController`) construida en código Java con JavaFX 21. Se integra directamente con `CatalogoProductos`: cada operación de la interfaz modifica las colecciones (`HashMap`, `HashSet`, `ArrayList`) y la tabla se refresca con `listar()`.

| Zona | Contenido |
| --- | --- |
| Formulario | Código, nombre, precio, stock, tipo (Comida/Bebida) y detalle dinámico (apto celíacos o tamaño en ml). |
| Botones | Agregar, Actualizar, Eliminar (con confirmación) y Limpiar. |
| Búsqueda | Criterio Código/Nombre, campo de texto, Buscar y Mostrar todo. |
| Tabla | Código, nombre, precio, stock, tipo y detalle de cada producto. |
| Barra de estado | Mensajes de éxito, advertencia o error; además se muestran alertas emergentes. |

Eventos y validaciones:

- Botones con `setOnAction`, Enter en el campo de búsqueda, selección de fila que carga el formulario (el código queda bloqueado porque es la clave inmutable) y cambio de tipo que alterna el campo de detalle.
- Se validan campos obligatorios, precio numérico ≥ 0, stock entero ≥ 0 y tamaño en ml > 0 para bebidas; los duplicados se rechazan desde el `HashSet` del catálogo.
- Cada error o éxito se informa con una `Alert` y con la barra de estado.

## Estructura de clases

| Clase | Responsabilidad |
| --- | --- |
| `Main` | Punto de entrada; ejecuta `demoCatalogo()` y `demoPedidos()` con datos de prueba. |
| `Producto` | Atributos comunes `codigo`, `nombre`, `precioBase`, `stock` con validaciones; define `equals`/`hashCode` por código. |
| `ProductoComida` | Extiende `Producto`; agrega `esAptoCeliacos`. |
| `ProductoBebida` | Extiende `Producto`; agrega `tamanoMl`. |
| `CatalogoProductos` | Administra el catálogo con `HashMap`, `HashSet` y `ArrayList`; operaciones CRUD y búsquedas. |
| `CatalogoApp` | Aplicación JavaFX: crea la ventana principal del catálogo. |
| `CatalogoController` | Construye la interfaz, maneja los eventos y ejecuta las operaciones CRUD con validaciones. |
| `Cliente` (abstracta) | Atributos `nombre`, `email`, `saldoDisponible`; define `calcularDescuento`. |
| `ClienteMayorista` | Extiende `Cliente`; agrega `razonSocial`; descuento del 15%. |
| `ClienteMinorista` | Extiende `Cliente`; agrega `tieneTarjetaFidelidad`; descuento del 5%. |
| `ItemPedido` | Contiene un `Producto` y su `cantidad`; calcula su subtotal. |
| `Pedido` | Compone una lista de `ItemPedido` y un `Cliente`; calcula totales y muestra el resumen. |
| `Cola<T>` | Estructura FIFO implementada manualmente con arreglo circular y crecimiento automático. |
| `ColaVaciaException` | Excepción propia para operaciones inválidas sobre una cola vacía. |
| `Repository<T>` | Interfaz genérica que desacopla la lógica del almacenamiento. |
| `ColaRepository<T>` | Implementación del repositorio sobre `Cola<T>`. |
| `CatalogoRepository` | Interfaz que desacopla la persistencia del catálogo (`cargar`/`guardar`). |
| `CatalogoJsonRepository` | Persistencia del catálogo en JSON con Gson, conservando los subtipos de `Producto`. |
| `CatalogoRepositoryEnMemoria` | Implementación sin persistencia para demos y pruebas. |
| `PersistenciaException` | Error de lectura/escritura del archivo del catálogo. |
| `PedidosPendientes` | Servicio que registra, atiende y consulta pedidos usando `Repository<Pedido>`. |
| `MainConsola` | Menú de consola para administrar la cola de pedidos (sin GUI). |

## Instrucciones básicas para ejecutar

Requisitos previos: **Java** y **Maven**.

1. Compilar el proyecto:

   ```bash
   mvn clean package
   ```

2. Ejecutar la aplicación de consola con los demos (catálogo y pedidos):

   ```bash
   mvn exec:java -Dexec.mainClass="com.universidad.cafeteria.Main"
   ```

3. Ejecutar el menú de consola de la cola de pedidos:

   ```bash
   mvn exec:java -Dexec.mainClass="com.universidad.cafeteria.consola.MainConsola"
   ```

4. Ejecutar las pruebas unitarias:

   ```bash
   mvn test
   ```

5. Ejecutar la interfaz gráfica JavaFX:

   ```bash
   mvn javafx:run
   ```

## Tecnología

- **Java** 17+
- **JavaFX** 21 (interfaz gráfica)
- **Gson** 2.14.0 (persistencia del catálogo en JSON)
- **JUnit** 5 (pruebas unitarias)
- **Maven** (build)

## Autor

Christian Pacho
