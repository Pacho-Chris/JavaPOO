package com.universidad.cafeteria.model;

public class ClienteMayorista extends Cliente {
  private String razonSocial;

  public ClienteMayorista(String nombre, String email, double saldoDisponible, String razonSocial) {
    super(nombre, email, saldoDisponible);
    this.razonSocial = razonSocial;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  @Override
  public double calcularDescuento(double montoTotal) {
    // Regla de negocio mayorista: 15% de descuento fijo sobre la compra
    return montoTotal * 0.15;
  }
}
