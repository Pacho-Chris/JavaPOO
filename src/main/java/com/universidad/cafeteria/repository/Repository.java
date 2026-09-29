package com.universidad.cafeteria.repository;

import java.util.List;

public interface Repository<T> {
  void guardar(T elemento);

  T eliminar();

  T consultar();

  boolean estaVacio();

  int cantidad();

  List<T> listar();
}
