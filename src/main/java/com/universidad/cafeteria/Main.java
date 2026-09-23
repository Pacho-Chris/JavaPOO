package com.universidad.cafeteria;

import com.universidad.cafeteria.model.*;
import com.universidad.cafeteria.service.CatalogoProductos;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    System.out.println("--- SISTEMA DE PEDIDOS CAFETERÍA ---\n");
    demoCatalogo();
    System.out.println();
    demoPedidos();
  }

  private static void demoCatalogo() {
    System.out.println("========== DEMO 1: CATÁLOGO DE PRODUCTOS ==========\n");

    CatalogoProductos catalogo = new CatalogoProductos();

    // 1. AGREGAR: productos de prueba (comida y bebida)
    catalogo.agregar(new ProductoComida("P001", "Sándwich Jamón y Queso", 4.50, 20, true));
    catalogo.agregar(new ProductoComida("P002", "Ensalada César", 5.75, 12, false));
    catalogo.agregar(new ProductoComida("P003", "Brownie de Chocolate", 3.25, 30, true));
    catalogo.agregar(new ProductoBebida("B001", "Café Americano", 2.50, 50, 350));
    catalogo.agregar(new ProductoBebida("B002", "Jugo Natural de Naranja", 3.00, 25, 400));
    catalogo.agregar(new ProductoBebida("B003", "Agua Mineral", 1.50, 100, 500));
    System.out.println("Se agregaron " + catalogo.cantidad() + " productos al catálogo.\n");

    // 2. EVITAR DUPLICADOS: el HashSet rechaza un código ya registrado
    try {
      catalogo.agregar(new ProductoBebida("B001", "Café Duplicado", 2.75, 10, 350));
    } catch (IllegalArgumentException e) {
      System.out.println("Duplicado rechazado: " + e.getMessage() + "\n");
    }

    // 3. LISTAR: recorrido del catálogo en orden de inserción
    catalogo.mostrarCatalogo();
    System.out.println();

    // 4. BUSCAR por código (HashMap)
    System.out.println("Búsqueda por código \"B002\":");
    Producto porCodigo = catalogo.buscarPorCodigo("B002");
    if (porCodigo != null) {
      porCodigo.mostrarInformacion();
    } else {
      System.out.println("Producto no encontrado.");
    }
    System.out.println("Búsqueda por código \"P999\": "
        + (catalogo.buscarPorCodigo("P999") == null ? "no encontrado" : "encontrado") + "\n");

    // 5. BUSCAR por nombre (coincidencia parcial, sin distinguir mayúsculas)
    System.out.println("Búsqueda por nombre \"café\":");
    List<Producto> porNombre = catalogo.buscarPorNombre("café");
    for (Producto producto : porNombre) {
      producto.mostrarInformacion();
    }
    System.out.println();

    // 6. LISTAR por tipo (polimorfismo + genéricos)
    System.out.println("Comidas registradas: " + catalogo.listarPorTipo(ProductoComida.class).size());
    System.out.println("Bebidas registradas: " + catalogo.listarPorTipo(ProductoBebida.class).size() + "\n");

    // 7. ACTUALIZAR: cambia nombre, precio y stock sin alterar el código
    System.out.println("Actualización de \"P001\":");
    boolean actualizado = catalogo.actualizar("P001", "Sándwich Jamón y Queso Premium", 5.25, 15);
    System.out.println(actualizado ? "Producto actualizado:" : "Producto no encontrado:");
    catalogo.buscarPorCodigo("P001").mostrarInformacion();
    System.out.println();

    // 8. ELIMINAR: se quita de las tres colecciones
    System.out.println("Eliminar \"B003\": " + (catalogo.eliminar("B003") ? "eliminado" : "no existía"));
    System.out.println("Eliminar \"B003\" de nuevo: " + (catalogo.eliminar("B003") ? "eliminado" : "no existía"));
    System.out.println();
    catalogo.mostrarCatalogo();
    System.out.println("Total final: " + catalogo.cantidad() + " productos.");
  }

  private static void demoPedidos() {
    System.out.println("========== DEMO 2: PEDIDOS Y DESCUENTOS ==========\n");

    // 1. Catálogo de Productos
    ProductoComida sandwich = new ProductoComida("P001", "Sándwich Jamón y Queso", 4.50, 20, true);
    ProductoBebida cafe = new ProductoBebida("B001", "Café Americano", 2.50, 50, 350);

    // 2. Instanciación Polimórfica (Declarados como 'Cliente')
    Cliente clienteMayo = new ClienteMayorista("Distribuidora Central", "ventas@dist.com", 500.0,
        "Distribuidora Central S.A.");
    Cliente clienteMino = new ClienteMinorista("Ana López", "ana@mail.com", 30.0, true);

    // 3. Pedido 1 (Mayorista) -> Aplicará 15% de descuento
    Pedido pedido1 = new Pedido(clienteMayo);
    pedido1.agregarItem(new ItemPedido(sandwich, 10)); // $45.00
    pedido1.agregarItem(new ItemPedido(cafe, 5)); // $12.50 (Subtotal = $57.50)

    // 4. Pedido 2 (Minorista con tarjeta) -> Aplicará 5% de descuento
    Pedido pedido2 = new Pedido(clienteMino);
    pedido2.agregarItem(new ItemPedido(sandwich, 2)); // $9.00
    pedido2.agregarItem(new ItemPedido(cafe, 1)); // $2.50 (Subtotal = $11.50)

    // 5. Salida por Consola
    pedido1.mostrarResumen();
    System.out.println();
    pedido2.mostrarResumen();
  }
}
