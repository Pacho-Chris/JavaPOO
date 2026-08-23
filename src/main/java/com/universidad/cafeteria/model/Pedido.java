package com.universidad.cafeteria.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
  private Cliente cliente;
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

  public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : items) {
            total += item.calcularSubtotal();
        }
        return total;
    }

  public void mostrarResumen() {
    System.out.println("============================================");
    System.out.println("RESUMEN DE PEDIDO PARA: " + cliente.getNombre());
    System.out.println("============================================");
    for (ItemPedido item : items) {
      Producto p = item.getProducto();
      System.out.printf("- %s x%d: $%.2f%n",
          p.getNombre(), item.getCantidad(), item.calcularSubtotal());
    }
    System.out.println("--------------------------------------------");
    System.out.printf("TOTAL A PAGAR: $%.2f%n", calcularTotal());
    System.out.println("============================================");
  }
}
