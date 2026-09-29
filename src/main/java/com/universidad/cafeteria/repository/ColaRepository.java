package com.universidad.cafeteria.repository;

import com.universidad.cafeteria.estructuras.Cola;

import java.util.List;

public class ColaRepository<T> implements Repository<T> {
  private final Cola<T> cola = new Cola<>();

  @Override
  public void guardar(T elemento) {
    cola.encolar(elemento);
  }

  @Override
  public T eliminar() {
    return cola.desencolar();
  }

  @Override
  public T consultar() {
    return cola.frente();
  }

  @Override
  public boolean estaVacio() {
    return cola.estaVacia();
  }

  @Override
  public int cantidad() {
    return cola.cantidad();
  }

  @Override
  public List<T> listar() {
    return cola.listar();
  }
}
