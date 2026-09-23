package com.universidad.cafeteria.model;

import java.util.Objects;

public class Producto {
  protected String codigo;
  protected String nombre;
  protected double precioBase;
  protected int stock;

  public Producto(String codigo, String nombre, double precioBase, int stock) {
    setCodigo(codigo);
    this.nombre = nombre;
    setPrecioBase(precioBase); // Aplica validación desde la construcción
    this.stock = stock;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    if (codigo == null || codigo.isBlank()) {
      throw new IllegalArgumentException("El código del producto no puede ser nulo ni vacío.");
    }
    this.codigo = codigo.trim().toUpperCase();
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

  // equals/hashCode por código: permite que HashSet detecte productos duplicados
  @Override
  public boolean equals(Object objeto) {
    if (this == objeto) {
      return true;
    }
    if (!(objeto instanceof Producto)) {
      return false;
    }
    Producto otro = (Producto) objeto;
    return codigo.equals(otro.codigo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigo);
  }

  @Override
  public String toString() {
    return "[" + codigo + "] " + nombre + " - $" + precioBase + " (Stock: " + stock + " u.)";
  }

  public void mostrarInformacion() {
        System.out.println("[" + codigo + "] " + nombre + " - $" + precioBase + " (Stock: " + stock + " u.)");
    }
}
