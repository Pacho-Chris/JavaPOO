package com.universidad.cafeteria.model;

public class ProductoBebida extends Producto {
  private int tamanoMl;

  public ProductoBebida(String nombre, double precioBase, int stock, int tamanoMl) {
    super(nombre, precioBase, stock); // Llama al constructor de la superclase Producto
    setTamanoMl(tamanoMl);
  }

  public int getTamanoMl() {
    return tamanoMl;
  }

  public void setTamanoMl(int tamanoMl) {
    if (tamanoMl <= 0) {
      throw new IllegalArgumentException("El tamaño en ml debe ser mayor a 0.");
    }
    this.tamanoMl = tamanoMl;
  }

  @Override
  public void mostrarInformacion() {
    super.mostrarInformacion(); // Reutiliza la implementación del padre
    System.out.println("   [Bebida] Tamaño: " + tamanoMl + " ml");
  }
}
