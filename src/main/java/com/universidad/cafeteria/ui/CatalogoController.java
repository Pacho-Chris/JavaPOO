package com.universidad.cafeteria.ui;

import com.universidad.cafeteria.model.Producto;
import com.universidad.cafeteria.model.ProductoBebida;
import com.universidad.cafeteria.model.ProductoComida;
import com.universidad.cafeteria.service.CatalogoProductos;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class CatalogoController {
  private final CatalogoProductos catalogo = new CatalogoProductos();

  private final BorderPane root = new BorderPane();
  private final Label barraEstado = new Label("Listo. Use el formulario para gestionar el catálogo.");

  private final TextField campoCodigo = new TextField();
  private final TextField campoNombre = new TextField();
  private final TextField campoPrecio = new TextField();
  private final TextField campoStock = new TextField();
  private final ComboBox<String> comboTipo = new ComboBox<>();
  private final CheckBox checkAptoCeliacos = new CheckBox();
  private final TextField campoTamanoMl = new TextField();
  private final Label etiquetaExtra = new Label("Apto celíacos:");

  private final Button btnAgregar = new Button("Agregar");
  private final Button btnActualizar = new Button("Actualizar");
  private final Button btnEliminar = new Button("Eliminar");
  private final Button btnLimpiar = new Button("Limpiar");

  private final ComboBox<String> comboCriterio = new ComboBox<>();
  private final TextField campoBusqueda = new TextField();
  private final Button btnBuscar = new Button("Buscar");
  private final Button btnMostrarTodo = new Button("Mostrar todo");

  private final TableView<Producto> tabla = new TableView<>();
  private final ObservableList<Producto> datosTabla = FXCollections.observableArrayList();

  public CatalogoController() {
    cargarDatosIniciales();
    construirInterfaz();
    conectarEventos();
    refrescarTabla();
  }

  public BorderPane getRoot() {
    return root;
  }

  private void cargarDatosIniciales() {
    catalogo.agregar(new ProductoComida("P001", "Sándwich Jamón y Queso", 4.50, 20, true));
    catalogo.agregar(new ProductoComida("P002", "Ensalada César", 5.75, 12, false));
    catalogo.agregar(new ProductoComida("P003", "Brownie de Chocolate", 3.25, 30, true));
    catalogo.agregar(new ProductoBebida("B001", "Café Americano", 2.50, 50, 350));
    catalogo.agregar(new ProductoBebida("B002", "Jugo Natural de Naranja", 3.00, 25, 400));
    catalogo.agregar(new ProductoBebida("B003", "Agua Mineral", 1.50, 100, 500));
  }

  private void construirInterfaz() {
    root.setPadding(new Insets(16));

    Label titulo = new Label("Catálogo de Productos");
    titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    Label subtitulo = new Label("CRUD conectado a CatalogoProductos (ArrayList, HashMap y HashSet)");
    VBox encabezado = new VBox(4, titulo, subtitulo);
    encabezado.setPadding(new Insets(0, 0, 12, 0));
    root.setTop(encabezado);

    root.setLeft(construirFormulario());
    root.setCenter(construirSeccionTabla());

    barraEstado.setPadding(new Insets(10, 0, 0, 0));
    barraEstado.setStyle("-fx-text-fill: #555555;");
    root.setBottom(barraEstado);
  }

  private VBox construirFormulario() {
    campoCodigo.setPromptText("Ej: P004");
    campoNombre.setPromptText("Ej: Torta de Zanahoria");
    campoPrecio.setPromptText("Ej: 3.50");
    campoStock.setPromptText("Ej: 10");
    campoTamanoMl.setPromptText("Ej: 350");

    comboTipo.setItems(FXCollections.observableArrayList("Comida", "Bebida"));
    comboTipo.setValue("Comida");
    comboTipo.setMaxWidth(Double.MAX_VALUE);

    GridPane grid = new GridPane();
    grid.setHgap(8);
    grid.setVgap(8);

    grid.add(new Label("Código:"), 0, 0);
    grid.add(campoCodigo, 1, 0);
    grid.add(new Label("Nombre:"), 0, 1);
    grid.add(campoNombre, 1, 1);
    grid.add(new Label("Precio ($):"), 0, 2);
    grid.add(campoPrecio, 1, 2);
    grid.add(new Label("Stock:"), 0, 3);
    grid.add(campoStock, 1, 3);
    grid.add(new Label("Tipo:"), 0, 4);
    grid.add(comboTipo, 1, 4);
    grid.add(etiquetaExtra, 0, 5);
    grid.add(new VBox(4, checkAptoCeliacos, campoTamanoMl), 1, 5);

    configurarBoton(btnAgregar);
    configurarBoton(btnActualizar);
    configurarBoton(btnEliminar);
    configurarBoton(btnLimpiar);

    HBox filaUno = new HBox(8, btnAgregar, btnActualizar);
    HBox filaDos = new HBox(8, btnEliminar, btnLimpiar);

    VBox contenedor = new VBox(14, grid, filaUno, filaDos);
    contenedor.setPadding(new Insets(0, 18, 0, 0));
    contenedor.setPrefWidth(340);
    return contenedor;
  }

  private void configurarBoton(Button boton) {
    boton.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(boton, Priority.ALWAYS);
  }

  private VBox construirSeccionTabla() {
    campoBusqueda.setPromptText("Texto a buscar...");
    HBox.setHgrow(campoBusqueda, Priority.ALWAYS);
    comboCriterio.setItems(FXCollections.observableArrayList("Código", "Nombre"));
    comboCriterio.setValue("Nombre");

    HBox barraBusqueda = new HBox(8, new Label("Buscar:"), comboCriterio, campoBusqueda, btnBuscar, btnMostrarTodo);
    barraBusqueda.setAlignment(Pos.CENTER_LEFT);

    configurarTabla();
    VBox.setVgrow(tabla, Priority.ALWAYS);

    return new VBox(10, barraBusqueda, tabla);
  }

  private void configurarTabla() {
    TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
    colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
    colCodigo.setPrefWidth(80);

    TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
    colNombre.setPrefWidth(220);

    TableColumn<Producto, String> colPrecio = new TableColumn<>("Precio");
    colPrecio.setCellValueFactory(c -> new SimpleStringProperty(String.format("$%.2f", c.getValue().getPrecioBase())));
    colPrecio.setPrefWidth(90);

    TableColumn<Producto, String> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getStock())));
    colStock.setPrefWidth(70);

    TableColumn<Producto, String> colTipo = new TableColumn<>("Tipo");
    colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClass().getSimpleName()));
    colTipo.setPrefWidth(130);

    TableColumn<Producto, String> colDetalle = new TableColumn<>("Detalle");
    colDetalle.setCellValueFactory(c -> new SimpleStringProperty(obtenerDetalle(c.getValue())));
    colDetalle.setPrefWidth(160);

    tabla.getColumns().addAll(List.of(colCodigo, colNombre, colPrecio, colStock, colTipo, colDetalle));
    tabla.setItems(datosTabla);
    tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    tabla.setPlaceholder(new Label("No hay productos para mostrar."));
  }

  private String obtenerDetalle(Producto producto) {
    if (producto instanceof ProductoComida comida) {
      return comida.isEsAptoCeliacos() ? "Apto celíacos" : "No apto celíacos";
    }
    if (producto instanceof ProductoBebida bebida) {
      return bebida.getTamanoMl() + " ml";
    }
    return "";
  }

  private void conectarEventos() {
    btnAgregar.setOnAction(evento -> agregarProducto());
    btnActualizar.setOnAction(evento -> actualizarProducto());
    btnEliminar.setOnAction(evento -> eliminarProducto());
    btnLimpiar.setOnAction(evento -> {
      limpiarFormulario();
      mostrarEstado("Formulario limpio. Puede registrar un producto nuevo.");
    });
    btnBuscar.setOnAction(evento -> buscarProductos());
    btnMostrarTodo.setOnAction(evento -> {
      refrescarTabla();
      mostrarEstado("Mostrando los " + catalogo.cantidad() + " productos del catálogo.");
    });
    campoBusqueda.setOnAction(evento -> buscarProductos());

    comboTipo.valueProperty().addListener((observable, anterior, tipo) -> {
      boolean esComida = "Comida".equals(tipo);
      etiquetaExtra.setText(esComida ? "Apto celíacos:" : "Tamaño (ml):");
      checkAptoCeliacos.setVisible(esComida);
      checkAptoCeliacos.setManaged(esComida);
      campoTamanoMl.setVisible(!esComida);
      campoTamanoMl.setManaged(!esComida);
    });

    tabla.getSelectionModel().selectedItemProperty().addListener((observable, anterior, seleccionado) -> {
      if (seleccionado != null) {
        cargarFormulario(seleccionado);
      }
    });
  }

  private void agregarProducto() {
    if (tabla.getSelectionModel().getSelectedItem() != null) {
      mostrarError("Hay un producto seleccionado. Presione \"Limpiar\" para registrar uno nuevo.");
      return;
    }
    try {
      Producto producto = construirProductoDesdeFormulario();
      catalogo.agregar(producto);
      refrescarTabla();
      limpiarFormulario();
      mostrarExito("Producto \"" + producto.getNombre() + "\" agregado correctamente.");
    } catch (IllegalArgumentException e) {
      mostrarError(e.getMessage());
    }
  }

  private Producto construirProductoDesdeFormulario() {
    String codigo = campoCodigo.getText();
    String nombre = campoNombre.getText();
    if (codigo == null || codigo.isBlank()) {
      throw new IllegalArgumentException("El código es obligatorio.");
    }
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre es obligatorio.");
    }
    double precio = parsearPrecio();
    int stock = parsearStock();
    if ("Comida".equals(comboTipo.getValue())) {
      return new ProductoComida(codigo.trim(), nombre.trim(), precio, stock, checkAptoCeliacos.isSelected());
    }
    return new ProductoBebida(codigo.trim(), nombre.trim(), precio, stock, parsearTamanoMl());
  }

  private double parsearPrecio() {
    String texto = campoPrecio.getText();
    if (texto == null || texto.isBlank()) {
      throw new IllegalArgumentException("El precio es obligatorio.");
    }
    try {
      double precio = Double.parseDouble(texto.trim().replace(",", "."));
      if (precio < 0) {
        throw new IllegalArgumentException("El precio no puede ser negativo.");
      }
      return precio;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El precio debe ser un número válido (ej: 4.50).");
    }
  }

  private int parsearStock() {
    String texto = campoStock.getText();
    if (texto == null || texto.isBlank()) {
      throw new IllegalArgumentException("El stock es obligatorio.");
    }
    try {
      int stock = Integer.parseInt(texto.trim());
      if (stock < 0) {
        throw new IllegalArgumentException("El stock no puede ser negativo.");
      }
      return stock;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El stock debe ser un número entero válido.");
    }
  }

  private int parsearTamanoMl() {
    String texto = campoTamanoMl.getText();
    if (texto == null || texto.isBlank()) {
      throw new IllegalArgumentException("El tamaño en ml es obligatorio para las bebidas.");
    }
    try {
      int tamano = Integer.parseInt(texto.trim());
      if (tamano <= 0) {
        throw new IllegalArgumentException("El tamaño en ml debe ser mayor a 0.");
      }
      return tamano;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El tamaño en ml debe ser un número entero válido.");
    }
  }

  private void actualizarProducto() {
    Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
    if (seleccionado == null) {
      mostrarError("Seleccione un producto de la tabla para actualizar.");
      return;
    }
    try {
      String nombre = campoNombre.getText();
      if (nombre == null || nombre.isBlank()) {
        throw new IllegalArgumentException("El nombre es obligatorio.");
      }
      double precio = parsearPrecio();
      int stock = parsearStock();
      catalogo.actualizar(seleccionado.getCodigo(), nombre.trim(), precio, stock);
      refrescarTabla();
      limpiarFormulario();
      mostrarExito("Producto \"" + nombre.trim() + "\" actualizado correctamente.");
    } catch (IllegalArgumentException e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminarProducto() {
    Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
    if (seleccionado == null) {
      mostrarError("Seleccione un producto de la tabla para eliminar.");
      return;
    }
    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
        "¿Está seguro de eliminar \"" + seleccionado.getNombre() + "\" (código " + seleccionado.getCodigo() + ")?",
        ButtonType.YES, ButtonType.NO);
    confirmacion.setTitle("Confirmar eliminación");
    confirmacion.setHeaderText(null);
    Optional<ButtonType> respuesta = confirmacion.showAndWait();
    if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
      catalogo.eliminar(seleccionado.getCodigo());
      refrescarTabla();
      limpiarFormulario();
      mostrarExito("Producto \"" + seleccionado.getNombre() + "\" eliminado correctamente.");
    }
  }

  private void buscarProductos() {
    String texto = campoBusqueda.getText();
    if (texto == null || texto.isBlank()) {
      mostrarError("Ingrese un código o nombre para buscar.");
      return;
    }
    if ("Código".equals(comboCriterio.getValue())) {
      Producto encontrado = catalogo.buscarPorCodigo(texto);
      if (encontrado == null) {
        datosTabla.clear();
        mostrarAdvertencia("No existe un producto con el código \"" + texto.trim() + "\".");
      } else {
        datosTabla.setAll(encontrado);
        mostrarEstado("Producto encontrado por código.");
      }
      return;
    }
    List<Producto> encontrados = catalogo.buscarPorNombre(texto);
    if (encontrados.isEmpty()) {
      datosTabla.clear();
      mostrarAdvertencia("No se encontraron productos que contengan \"" + texto.trim() + "\".");
    } else {
      datosTabla.setAll(encontrados);
      mostrarEstado("Se encontraron " + encontrados.size() + " producto(s) por nombre.");
    }
  }

  private void cargarFormulario(Producto producto) {
    campoCodigo.setText(producto.getCodigo());
    campoCodigo.setDisable(true);
    campoNombre.setText(producto.getNombre());
    campoPrecio.setText(String.valueOf(producto.getPrecioBase()));
    campoStock.setText(String.valueOf(producto.getStock()));
    comboTipo.setDisable(true);
    checkAptoCeliacos.setDisable(true);
    campoTamanoMl.setDisable(true);
    if (producto instanceof ProductoComida comida) {
      comboTipo.setValue("Comida");
      checkAptoCeliacos.setSelected(comida.isEsAptoCeliacos());
    } else if (producto instanceof ProductoBebida bebida) {
      comboTipo.setValue("Bebida");
      campoTamanoMl.setText(String.valueOf(bebida.getTamanoMl()));
    }
    mostrarEstado(
        "Producto seleccionado: " + producto.getNombre() + ". Edite nombre, precio o stock y presione Actualizar.");
  }

  private void limpiarFormulario() {
    tabla.getSelectionModel().clearSelection();
    campoCodigo.clear();
    campoNombre.clear();
    campoPrecio.clear();
    campoStock.clear();
    campoTamanoMl.clear();
    checkAptoCeliacos.setSelected(false);
    comboTipo.setValue("Comida");
    campoCodigo.setDisable(false);
    comboTipo.setDisable(false);
    checkAptoCeliacos.setDisable(false);
    campoTamanoMl.setDisable(false);
    campoCodigo.requestFocus();
  }

  private void refrescarTabla() {
    datosTabla.setAll(catalogo.listar());
  }

  private void mostrarEstado(String mensaje) {
    barraEstado.setStyle("-fx-text-fill: #555555;");
    barraEstado.setText(mensaje);
  }

  private void mostrarExito(String mensaje) {
    barraEstado.setStyle("-fx-text-fill: #1b7f3b;");
    barraEstado.setText(mensaje);
    mostrarAlerta(Alert.AlertType.INFORMATION, "Operación exitosa", mensaje);
  }

  private void mostrarError(String mensaje) {
    barraEstado.setStyle("-fx-text-fill: #c0392b;");
    barraEstado.setText("Error: " + mensaje);
    mostrarAlerta(Alert.AlertType.ERROR, "Error", mensaje);
  }

  private void mostrarAdvertencia(String mensaje) {
    barraEstado.setStyle("-fx-text-fill: #b9770e;");
    barraEstado.setText(mensaje);
    mostrarAlerta(Alert.AlertType.WARNING, "Sin resultados", mensaje);
  }

  private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
  }
}
