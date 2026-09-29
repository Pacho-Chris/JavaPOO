package com.universidad.cafeteria.estructuras;

import java.util.ArrayList;
import java.util.List;

public class Cola<T> {
  private static final int CAPACIDAD_INICIAL = 10;

  private T[] elementos;
  private int frente;
  private int fin;
  private int cantidad;

  @SuppressWarnings("unchecked")
  public Cola() {
    this.elementos = (T[]) new Object[CAPACIDAD_INICIAL];
    this.frente = 0;
    this.fin = 0;
    this.cantidad = 0;
  }

  public void encolar(T elemento) {
    if (elemento == null) {
      throw new IllegalArgumentException("La cola no admite elementos nulos.");
    }
    if (cantidad == elementos.length) {
      crecer();
    }
    elementos[fin] = elemento;
    fin = (fin + 1) % elementos.length;
    cantidad++;
  }

  public T desencolar() {
    if (estaVacia()) {
      throw new ColaVaciaException("No se puede desencolar: la cola está vacía.");
    }
    T elemento = elementos[frente];
    elementos[frente] = null;
    frente = (frente + 1) % elementos.length;
    cantidad--;
    return elemento;
  }

  public T frente() {
    if (estaVacia()) {
      throw new ColaVaciaException("No se puede consultar el frente: la cola está vacía.");
    }
    return elementos[frente];
  }

  public boolean estaVacia() {
    return cantidad == 0;
  }

  public int cantidad() {
    return cantidad;
  }

  public List<T> listar() {
    List<T> copia = new ArrayList<>(cantidad);
    for (int i = 0; i < cantidad; i++) {
      copia.add(elementos[(frente + i) % elementos.length]);
    }
    return copia;
  }

  @SuppressWarnings("unchecked")
  private void crecer() {
    T[] nuevo = (T[]) new Object[elementos.length * 2];
    for (int i = 0; i < cantidad; i++) {
      nuevo[i] = elementos[(frente + i) % elementos.length];
    }
    elementos = nuevo;
    frente = 0;
    fin = cantidad;
  }
}
