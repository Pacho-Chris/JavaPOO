package com.universidad.cafeteria.model;

public class ProductoComida extends Producto {
  private boolean esAptoCeliacos;

  public ProductoComida(String codigo, String nombre, double precioBase, int stock, boolean esAptoCeliacos) {
    super(codigo, nombre, precioBase, stock); // Llama al constructor de la superclase Producto
    this.esAptoCeliacos = esAptoCeliacos;
  }

  public boolean isEsAptoCeliacos() {
    return esAptoCeliacos;
  }

  public void setEsAptoCeliacos(boolean esAptoCeliacos) {
    this.esAptoCeliacos = esAptoCeliacos;
  }

  @Override
  public void mostrarInformacion() {
    super.mostrarInformacion(); // Reutiliza la implementación del padre
    String apto = esAptoCeliacos ? "Sí" : "No";
    System.out.println("   [Comida] Apto Celiacos: " + apto);
  }
}
