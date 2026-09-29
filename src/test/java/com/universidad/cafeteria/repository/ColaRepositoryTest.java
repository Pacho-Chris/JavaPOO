package com.universidad.cafeteria.repository;

import com.universidad.cafeteria.estructuras.ColaVaciaException;
import com.universidad.cafeteria.model.ClienteMinorista;
import com.universidad.cafeteria.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColaRepositoryTest {
  private ColaRepository<String> repositorio;

  @BeforeEach
  void setUp() {
    repositorio = new ColaRepository<>();
  }

  @Test
  void repositorioNuevoEstaVacio() {
    assertTrue(repositorio.estaVacio());
    assertEquals(0, repositorio.cantidad());
  }

  @Test
  void guardarYConsultarDevuelvenElPrimerElementoSinEliminarlo() {
    repositorio.guardar("primero");
    repositorio.guardar("segundo");

    assertEquals("primero", repositorio.consultar());
    assertEquals(2, repositorio.cantidad());
  }

  @Test
  void eliminarRespetaElOrdenDeLlegada() {
    repositorio.guardar("primero");
    repositorio.guardar("segundo");
    repositorio.guardar("tercero");

    assertEquals("primero", repositorio.eliminar());
    assertEquals("segundo", repositorio.eliminar());
    assertEquals("tercero", repositorio.eliminar());
    assertTrue(repositorio.estaVacio());
  }

  @Test
  void listarDevuelveLosElementosEnOrdenFifo() {
    repositorio.guardar("primero");
    repositorio.guardar("segundo");

    assertEquals(List.of("primero", "segundo"), repositorio.listar());
  }

  @Test
  void eliminarEnRepositorioVacioLanzaExcepcion() {
    assertThrows(ColaVaciaException.class, () -> repositorio.eliminar());
  }

  @Test
  void guardarPedidosYAtenderlosEnOrdenDeLlegada() {
    Repository<Pedido> pedidos = new ColaRepository<>();
    Pedido pedidoAna = new Pedido(new ClienteMinorista("Ana López", "ana@mail.com", 30.0, true));
    Pedido pedidoMayorista = new Pedido(new ClienteMinorista("Luis Pérez", "luis@mail.com", 20.0, false));

    pedidos.guardar(pedidoAna);
    pedidos.guardar(pedidoMayorista);

    assertEquals(2, pedidos.cantidad());
    assertSame(pedidoAna, pedidos.consultar());
    assertSame(pedidoAna, pedidos.eliminar());
    assertSame(pedidoMayorista, pedidos.eliminar());
    assertTrue(pedidos.estaVacio());
    assertFalse(pedidos.cantidad() > 0);
  }
}
