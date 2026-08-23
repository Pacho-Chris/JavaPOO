package com.universidad.cafeteria.model;

public class Producto {
  protected String nombre;
  protected double precioBase;
  protected int stock;

  public Producto(String nombre, double precioBase, int stock) {
    this.nombre = nombre;
    setPrecioBase(precioBase); // Aplica validación desde la construcción
    this.stock = stock;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public double getPrecioBase() {
    return precioBase;
  }

  public void setPrecioBase(double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo.");
        }
        this.precioBase = precioBase;
    }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    if (stock < 0) {
      throw new IllegalArgumentException("El stock no puede ser negativo.");
    }
    this.stock = stock;
  }

  public void mostrarInformacion() {
        System.out.println(nombre + " - $" + precioBase + " (Stock: " + stock + " u.)");
    }
}
