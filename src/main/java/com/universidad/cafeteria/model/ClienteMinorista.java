package com.universidad.cafeteria.model;

public class ClienteMinorista extends Cliente {
  private boolean tieneTarjetaFidelidad;

  public ClienteMinorista(String nombre, String email, double saldoDisponible, boolean tieneTarjetaFidelidad) {
    super(nombre, email, saldoDisponible);
    this.tieneTarjetaFidelidad = tieneTarjetaFidelidad;
  }

  public boolean isTieneTarjetaFidelidad() {
    return tieneTarjetaFidelidad;
  }

  public void setTieneTarjetaFidelidad(boolean tieneTarjetaFidelidad) {
    this.tieneTarjetaFidelidad = tieneTarjetaFidelidad;
  }

  @Override
  public double calcularDescuento(double montoTotal) {
    // Regla de negocio minorista: 5% de descuento solo si tiene tarjeta y la compra
    // supera $10
    if (tieneTarjetaFidelidad && montoTotal > 10.0) {
      return montoTotal * 0.05;
    }
    return 0.0;
  }
}
