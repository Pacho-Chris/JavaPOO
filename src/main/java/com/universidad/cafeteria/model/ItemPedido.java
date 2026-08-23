package com.universidad.cafeteria.model;

public class ItemPedido {
  private Producto producto; // Agregación/Composición: ItemPedido contiene un Producto
  private int cantidad;

  public ItemPedido(Producto producto, int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
    }
    this.producto = producto;
    this.cantidad = cantidad;
  }

  public Producto getProducto() {
    return producto;
  }

  public int getCantidad() {
    return cantidad;
  }

  public double calcularSubtotal() {
        return producto.getPrecioBase() * cantidad;
    }
}
