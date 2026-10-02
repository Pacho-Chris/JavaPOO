package com.universidad.cafeteria.repository;

import com.universidad.cafeteria.model.Producto;
import com.universidad.cafeteria.model.ProductoBebida;
import com.universidad.cafeteria.model.ProductoComida;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoJsonRepositoryTest {
  @TempDir
  Path directorio;

  @Test
  void cargarArchivoInexistenteDevuelveListaVacia() {
    CatalogoJsonRepository repositorio = new CatalogoJsonRepository(directorio.resolve("catalogo.json"));

    assertTrue(repositorio.cargar().isEmpty());
  }

  @Test
  void guardarYCargarConservaSubtiposYDetalles() {
    Path archivo = directorio.resolve("catalogo.json");
    CatalogoJsonRepository repositorio = new CatalogoJsonRepository(archivo);
    List<Producto> originales = List.of(
        new ProductoComida("P001", "Sándwich Jamón y Queso", 4.50, 20, true),
        new ProductoBebida("B001", "Café Americano", 2.50, 50, 350));

    repositorio.guardar(originales);
    List<Producto> cargados = repositorio.cargar();

    assertEquals(2, cargados.size());
    ProductoComida comida = assertInstanceOf(ProductoComida.class, cargados.get(0));
    assertEquals("P001", comida.getCodigo());
    assertEquals("Sándwich Jamón y Queso", comida.getNombre());
    assertEquals(4.50, comida.getPrecioBase());
    assertEquals(20, comida.getStock());
    assertTrue(comida.isEsAptoCeliacos());

    ProductoBebida bebida = assertInstanceOf(ProductoBebida.class, cargados.get(1));
    assertEquals("B001", bebida.getCodigo());
    assertEquals(2.50, bebida.getPrecioBase());
    assertEquals(50, bebida.getStock());
    assertEquals(350, bebida.getTamanoMl());
  }

  @Test
  void guardarListaVaciaYVolverACargarlaDevuelveListaVacia() {
    Path archivo = directorio.resolve("catalogo.json");
    CatalogoJsonRepository repositorio = new CatalogoJsonRepository(archivo);

    repositorio.guardar(List.of());

    assertTrue(Files.exists(archivo));
    assertTrue(repositorio.cargar().isEmpty());
  }

  @Test
  void cargarJsonCorruptoLanzaPersistenciaException() throws IOException {
    Path archivo = directorio.resolve("catalogo.json");
    Files.writeString(archivo, "{ esto no es un catálogo válido");
    CatalogoJsonRepository repositorio = new CatalogoJsonRepository(archivo);

    assertThrows(PersistenciaException.class, repositorio::cargar);
  }

  @Test
  void cargarJsonConTipoDesconocidoLanzaPersistenciaException() throws IOException {
    Path archivo = directorio.resolve("catalogo.json");
    Files.writeString(archivo, """
        [
          { "tipo": "POSTRE", "codigo": "X001", "nombre": "Flan", "precioBase": 2.0, "stock": 5 }
        ]
        """);
    CatalogoJsonRepository repositorio = new CatalogoJsonRepository(archivo);

    assertThrows(PersistenciaException.class, repositorio::cargar);
  }
}
