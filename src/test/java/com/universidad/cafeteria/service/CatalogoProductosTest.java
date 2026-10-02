package com.universidad.cafeteria.service;

import com.universidad.cafeteria.model.ProductoBebida;
import com.universidad.cafeteria.model.ProductoComida;
import com.universidad.cafeteria.repository.CatalogoJsonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoProductosTest {
  @TempDir
  Path directorio;

  private Path archivo() {
    return directorio.resolve("catalogo.json");
  }

  @Test
  void catalogoNuevoConArchivoInexistenteEstaVacio() {
    CatalogoProductos catalogo = new CatalogoProductos(new CatalogoJsonRepository(archivo()));

    assertTrue(catalogo.estaVacio());
  }

  @Test
  void agregarPersisteYOtroCatalogoLoCarga() {
    CatalogoProductos primero = new CatalogoProductos(new CatalogoJsonRepository(archivo()));
    primero.agregar(new ProductoComida("P001", "Brownie", 3.25, 30, true));
    primero.agregar(new ProductoBebida("B001", "Agua Mineral", 1.50, 100, 500));

    CatalogoProductos segundo = new CatalogoProductos(new CatalogoJsonRepository(archivo()));

    assertEquals(2, segundo.cantidad());
    assertNotNull(segundo.buscarPorCodigo("P001"));
    assertNotNull(segundo.buscarPorCodigo("B001"));
    assertInstanceOf(ProductoComida.class, segundo.buscarPorCodigo("P001"));
    assertInstanceOf(ProductoBebida.class, segundo.buscarPorCodigo("B001"));
  }

  @Test
  void actualizarYPersisteElCambio() {
    CatalogoProductos primero = new CatalogoProductos(new CatalogoJsonRepository(archivo()));
    primero.agregar(new ProductoBebida("B001", "Café", 2.50, 50, 350));

    primero.actualizar("B001", "Café Americano", 3.00, 40);

    CatalogoProductos segundo = new CatalogoProductos(new CatalogoJsonRepository(archivo()));
    assertEquals("Café Americano", segundo.buscarPorCodigo("B001").getNombre());
    assertEquals(3.00, segundo.buscarPorCodigo("B001").getPrecioBase());
    assertEquals(40, segundo.buscarPorCodigo("B001").getStock());
  }

  @Test
  void eliminarYPersisteLaEliminacion() {
    CatalogoProductos primero = new CatalogoProductos(new CatalogoJsonRepository(archivo()));
    primero.agregar(new ProductoComida("P001", "Brownie", 3.25, 30, true));

    primero.eliminar("P001");

    CatalogoProductos segundo = new CatalogoProductos(new CatalogoJsonRepository(archivo()));
    assertTrue(segundo.estaVacio());
  }
}
