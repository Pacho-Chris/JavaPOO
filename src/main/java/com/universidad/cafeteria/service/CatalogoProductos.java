package com.universidad.cafeteria.service;

import com.universidad.cafeteria.model.Producto;
import com.universidad.cafeteria.repository.CatalogoJsonRepository;
import com.universidad.cafeteria.repository.CatalogoRepository;
import com.universidad.cafeteria.repository.PersistenciaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CatalogoProductos {
  // HashMap: almacena y permite buscar productos por su código (clave única)
  private final Map<String, Producto> productosPorCodigo = new HashMap<>();
  // HashSet: evita registros duplicados apoyándose en equals/hashCode de Producto
  private final Set<Producto> productosUnicos = new HashSet<>();
  // ArrayList: conserva el orden de inserción para listar el catálogo
  private final List<Producto> productosOrdenados = new ArrayList<>();
  // Repositorio de archivo: carga al iniciar y guarda tras cada cambio
  private final CatalogoRepository repositorio;

  public CatalogoProductos() {
    this(new CatalogoJsonRepository(), true);
  }

  public CatalogoProductos(CatalogoRepository repositorio) {
    this(repositorio, true);
  }

  public CatalogoProductos(CatalogoRepository repositorio, boolean cargarAlIniciar) {
    if (repositorio == null) {
      throw new IllegalArgumentException("El repositorio no puede ser nulo.");
    }
    this.repositorio = repositorio;
    if (cargarAlIniciar) {
      cargarDesdeRepositorio();
    }
  }

  private void cargarDesdeRepositorio() {
    for (Producto producto : repositorio.cargar()) {
      try {
        agregarSinPersistir(producto);
      } catch (IllegalArgumentException e) {
        throw new PersistenciaException(
            "El archivo del catálogo contiene productos inválidos o duplicados: " + e.getMessage(), e);
      }
    }
  }

  private void persistir() {
    repositorio.guardar(listar());
  }

  public void agregar(Producto producto) {
    agregarSinPersistir(producto);
    persistir();
  }

  private void agregarSinPersistir(Producto producto) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo.");
    }
    String codigo = normalizar(producto.getCodigo());
    // HashSet.add devuelve false si el producto ya existe (mismo código)
    if (!productosUnicos.add(producto)) {
      throw new IllegalArgumentException("Ya existe un producto con el código " + codigo + ".");
    }
    productosPorCodigo.put(codigo, producto);
    productosOrdenados.add(producto);
  }

  public Producto buscarPorCodigo(String codigo) {
    return productosPorCodigo.get(normalizar(codigo));
  }

  public List<Producto> buscarPorNombre(String texto) {
    List<Producto> coincidencias = new ArrayList<>();
    if (texto == null) {
      return coincidencias;
    }
    String busqueda = texto.trim().toLowerCase();
    for (Producto producto : productosOrdenados) {
      if (producto.getNombre().toLowerCase().contains(busqueda)) {
        coincidencias.add(producto);
      }
    }
    return coincidencias;
  }

  public List<Producto> listar() {
    return Collections.unmodifiableList(productosOrdenados);
  }

  public List<Producto> listarPorTipo(Class<? extends Producto> tipo) {
    List<Producto> filtrados = new ArrayList<>();
    for (Producto producto : productosOrdenados) {
      if (tipo.isInstance(producto)) {
        filtrados.add(producto);
      }
    }
    return filtrados;
  }

  public boolean actualizar(String codigo, String nuevoNombre, double nuevoPrecio, int nuevoStock) {
    Producto producto = buscarPorCodigo(codigo);
    if (producto == null) {
      return false;
    }
    // El código no cambia: sigue siendo la clave en HashMap y HashSet
    producto.setNombre(nuevoNombre);
    producto.setPrecioBase(nuevoPrecio);
    producto.setStock(nuevoStock);
    persistir();
    return true;
  }

  public boolean eliminar(String codigo) {
    Producto producto = productosPorCodigo.remove(normalizar(codigo));
    if (producto == null) {
      return false;
    }
    productosUnicos.remove(producto);
    productosOrdenados.remove(producto);
    persistir();
    return true;
  }

  public int cantidad() {
    return productosPorCodigo.size();
  }

  public boolean estaVacio() {
    return productosPorCodigo.isEmpty();
  }

  public void mostrarCatalogo() {
    if (estaVacio()) {
      System.out.println("El catálogo está vacío.");
      return;
    }
    System.out.println("Catálogo (" + cantidad() + " productos):");
    for (Producto producto : productosOrdenados) {
      producto.mostrarInformacion();
    }
  }

  private String normalizar(String codigo) {
    if (codigo == null) {
      throw new IllegalArgumentException("El código no puede ser nulo.");
    }
    return codigo.trim().toUpperCase();
  }
}
