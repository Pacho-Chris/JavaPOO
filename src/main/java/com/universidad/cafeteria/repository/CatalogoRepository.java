package com.universidad.cafeteria.repository;

import com.universidad.cafeteria.model.Producto;

import java.util.List;

public interface CatalogoRepository {
  List<Producto> cargar();

  void guardar(List<Producto> productos);
}
