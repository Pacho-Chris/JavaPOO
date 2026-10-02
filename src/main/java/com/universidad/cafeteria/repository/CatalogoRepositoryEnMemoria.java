package com.universidad.cafeteria.repository;

import com.universidad.cafeteria.model.Producto;

import java.util.ArrayList;
import java.util.List;

public class CatalogoRepositoryEnMemoria implements CatalogoRepository {
  @Override
  public List<Producto> cargar() {
    return new ArrayList<>();
  }

  @Override
  public void guardar(List<Producto> productos) {
    // Sin persistencia: se usa en demos y pruebas que no deben tocar el archivo.
  }
}
