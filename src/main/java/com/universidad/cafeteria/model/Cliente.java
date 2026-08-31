package com.universidad.cafeteria.model;

public abstract class Cliente {
  private String nombre;
  private String email;
  private double saldoDisponible;

  public Cliente(String nombre, String email, double saldoDisponible) {
    this.nombre = nombre;
    this.email = email;
    setSaldoDisponible(saldoDisponible); // Reutiliza validación del setter
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public double getSaldoDisponible() {
    return saldoDisponible;
  }

  public void setSaldoDisponible(double saldoDisponible) {
    if (saldoDisponible < 0) {
      System.out.println("Error: El saldo disponible no puede ser negativo.");
      return;
    }
    this.saldoDisponible = saldoDisponible;
  }

  // Método abstracto que obliga a la implementación polimórfica en subclases
  public abstract double calcularDescuento(double montoTotal);
}
