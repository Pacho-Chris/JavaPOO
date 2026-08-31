package com.universidad.cafeteria.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
  private Cliente cliente; // Referencia a la abstracción Cliente
  private List<ItemPedido> items; // Composición: Pedido administra una colección de ItemPedido

  public Pedido(Cliente cliente) {
    this.cliente = cliente;
    this.items = new ArrayList<>();
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void agregarItem(ItemPedido item) {
    items.add(item);
  }

  public double calcularSubtotal() {
    double subtotal = 0;
    for (ItemPedido item : items) {
      subtotal += item.calcularSubtotal();
    }
    return subtotal;
  }

  public double calcularDescuento() {
    // Polimorfismo Java ejecuta la implementación de la subclase real'
    return cliente.calcularDescuento(calcularSubtotal());
  }

  public double calcularTotalFinal() {
    return calcularSubtotal() - calcularDescuento();
  }

  public void mostrarResumen() {
    double subtotal = calcularSubtotal();
    double descuento = calcularDescuento();
    double totalFinal = calcularTotalFinal();

    System.out.println("============================================");
    System.out
        .println("RESUMEN DE PEDIDO PARA: " + cliente.getNombre() + " (" + cliente.getClass().getSimpleName() + ")");
    System.out.println("============================================");
    for (ItemPedido item : items) {
      Producto p = item.getProducto();
      System.out.printf("- %s x%d: $%.2f%n",
          p.getNombre(), item.getCantidad(), item.calcularSubtotal());
    }
    System.out.println("--------------------------------------------");
    System.out.printf("Subtotal:       $%.2f%n", subtotal);
    System.out.printf("Descuento:     -$%.2f%n", descuento);
    System.out.printf("TOTAL A PAGAR:  $%.2f%n", totalFinal);
    System.out.println("============================================");
  }
}
