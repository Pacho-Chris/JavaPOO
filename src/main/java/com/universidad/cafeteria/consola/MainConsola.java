package com.universidad.cafeteria.consola;

import com.universidad.cafeteria.model.Cliente;
import com.universidad.cafeteria.model.ClienteMayorista;
import com.universidad.cafeteria.model.ClienteMinorista;
import com.universidad.cafeteria.model.ItemPedido;
import com.universidad.cafeteria.model.Pedido;
import com.universidad.cafeteria.model.Producto;
import com.universidad.cafeteria.model.ProductoBebida;
import com.universidad.cafeteria.model.ProductoComida;
import com.universidad.cafeteria.service.PedidosPendientes;

import java.util.List;
import java.util.Scanner;

public class MainConsola {
  private final Scanner scanner = new Scanner(System.in);
  private final PedidosPendientes pedidosPendientes = new PedidosPendientes();
  private final List<Producto> productos;
  private final List<Cliente> clientes;

  public MainConsola() {
    productos = List.of(
        new ProductoComida("P001", "Sándwich Jamón y Queso", 4.50, 20, true),
        new ProductoComida("P002", "Ensalada César", 5.75, 12, false),
        new ProductoComida("P003", "Brownie de Chocolate", 3.25, 30, true),
        new ProductoBebida("B001", "Café Americano", 2.50, 50, 350),
        new ProductoBebida("B002", "Jugo Natural de Naranja", 3.00, 25, 400),
        new ProductoBebida("B003", "Agua Mineral", 1.50, 100, 500));
    clientes = List.of(
        new ClienteMinorista("Ana López", "ana@mail.com", 30.0, true),
        new ClienteMayorista("Distribuidora Central", "ventas@dist.com", 500.0,
            "Distribuidora Central S.A."));
  }

  public static void main(String[] args) {
    new MainConsola().iniciar();
  }

  public void iniciar() {
    System.out.println("=== CAFETERÍA: COLA DE PEDIDOS PENDIENTES ===");
    boolean salir = false;
    while (!salir) {
      mostrarMenu();
      Integer opcion = leerEntero();
      if (opcion == null) {
        System.out.println("Ingrese un número válido.");
        continue;
      }
      switch (opcion) {
        case 1 -> registrarPedido();
        case 2 -> atenderSiguiente();
        case 3 -> verSiguiente();
        case 4 -> verEstado();
        case 5 -> listarPendientes();
        case 0 -> salir = true;
        default -> System.out.println("Opción inválida.");
      }
    }
    System.out.println("Fin del programa.");
    scanner.close();
  }

  private void mostrarMenu() {
    System.out.println("\n1. Registrar pedido");
    System.out.println("2. Atender siguiente pedido");
    System.out.println("3. Ver siguiente pedido sin atender");
    System.out.println("4. Ver estado de la cola");
    System.out.println("5. Listar pedidos pendientes");
    System.out.println("0. Salir");
    System.out.print("Opción: ");
  }

  private void registrarPedido() {
    System.out.println("\n--- REGISTRAR PEDIDO ---");
    Cliente cliente = elegirCliente();
    if (cliente == null) {
      return;
    }
    Pedido pedido = new Pedido(cliente);
    mostrarProductos();
    while (true) {
      System.out.print("Código del producto (Enter para terminar): ");
      String codigo = scanner.nextLine().trim();
      if (codigo.isEmpty()) {
        break;
      }
      Producto producto = buscarProducto(codigo);
      if (producto == null) {
        System.out.println("Producto no encontrado.");
        continue;
      }
      System.out.print("Cantidad: ");
      Integer cantidad = leerEntero();
      if (cantidad == null || cantidad <= 0) {
        System.out.println("Cantidad inválida.");
        continue;
      }
      try {
        pedido.agregarItem(new ItemPedido(producto, cantidad));
        System.out.println("Agregado: " + producto.getNombre() + " x" + cantidad);
      } catch (IllegalArgumentException e) {
        System.out.println("No se pudo agregar: " + e.getMessage());
      }
    }
    if (pedido.calcularSubtotal() == 0) {
      System.out.println("El pedido no tiene ítems, no se registró.");
      return;
    }
    pedidosPendientes.registrar(pedido);
    System.out.println("Pedido registrado. Pendientes: " + pedidosPendientes.cantidadPendientes());
  }

  private Cliente elegirCliente() {
    System.out.println("Clientes registrados:");
    for (int i = 0; i < clientes.size(); i++) {
      Cliente cliente = clientes.get(i);
      System.out.printf("  %d. %s (%s)%n", i + 1, cliente.getNombre(),
          cliente.getClass().getSimpleName());
    }
    System.out.print("Seleccione cliente: ");
    Integer opcion = leerEntero();
    if (opcion == null || opcion < 1 || opcion > clientes.size()) {
      System.out.println("Selección inválida.");
      return null;
    }
    return clientes.get(opcion - 1);
  }

  private void mostrarProductos() {
    System.out.println("Productos disponibles:");
    for (Producto producto : productos) {
      System.out.printf("  %s - %s ($%.2f, stock %d)%n", producto.getCodigo(),
          producto.getNombre(), producto.getPrecioBase(), producto.getStock());
    }
  }

  private Producto buscarProducto(String codigo) {
    for (Producto producto : productos) {
      if (producto.getCodigo().equalsIgnoreCase(codigo.trim())) {
        return producto;
      }
    }
    return null;
  }

  private void atenderSiguiente() {
    if (!pedidosPendientes.hayPendientes()) {
      System.out.println("\nNo hay pedidos pendientes.");
      return;
    }
    Pedido pedido = pedidosPendientes.atenderSiguiente();
    System.out.println("\nAtendiendo el pedido más antiguo...");
    pedido.mostrarResumen();
    System.out.println("Pendientes restantes: " + pedidosPendientes.cantidadPendientes());
  }

  private void verSiguiente() {
    if (!pedidosPendientes.hayPendientes()) {
      System.out.println("\nNo hay pedidos pendientes.");
      return;
    }
    System.out.println("\nSiguiente pedido en la cola (aún no se atiende):");
    pedidosPendientes.verSiguiente().mostrarResumen();
  }

  private void verEstado() {
    if (pedidosPendientes.hayPendientes()) {
      System.out.println("\nPedidos pendientes: " + pedidosPendientes.cantidadPendientes());
    } else {
      System.out.println("\nLa cola está vacía.");
    }
  }

  private void listarPendientes() {
    if (!pedidosPendientes.hayPendientes()) {
      System.out.println("\nLa cola está vacía.");
      return;
    }
    System.out.println("\nPedidos pendientes en orden de llegada:");
    List<Pedido> pendientes = pedidosPendientes.listarPendientes();
    for (int i = 0; i < pendientes.size(); i++) {
      Pedido pedido = pendientes.get(i);
      System.out.printf("  %d. %s -> $%.2f%n", i + 1, pedido.getCliente().getNombre(),
          pedido.calcularTotalFinal());
    }
  }

  private Integer leerEntero() {
    String linea = scanner.nextLine().trim();
    try {
      return Integer.valueOf(linea);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
