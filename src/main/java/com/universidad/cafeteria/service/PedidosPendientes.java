package com.universidad.cafeteria.service;

import com.universidad.cafeteria.model.Pedido;
import com.universidad.cafeteria.repository.ColaRepository;
import com.universidad.cafeteria.repository.Repository;

import java.util.List;

public class PedidosPendientes {
  private final Repository<Pedido> repositorio;

  public PedidosPendientes() {
    this(new ColaRepository<>());
  }

  public PedidosPendientes(Repository<Pedido> repositorio) {
    this.repositorio = repositorio;
  }

  public void registrar(Pedido pedido) {
    repositorio.guardar(pedido);
  }

  public Pedido atenderSiguiente() {
    return repositorio.eliminar();
  }

  public Pedido verSiguiente() {
    return repositorio.consultar();
  }

  public boolean hayPendientes() {
    return !repositorio.estaVacio();
  }

  public int cantidadPendientes() {
    return repositorio.cantidad();
  }

  public List<Pedido> listarPendientes() {
    return repositorio.listar();
  }
}
