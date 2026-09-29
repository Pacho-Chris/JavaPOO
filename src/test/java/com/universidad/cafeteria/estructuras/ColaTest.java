package com.universidad.cafeteria.estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColaTest {
  private Cola<String> cola;

  @BeforeEach
  void setUp() {
    cola = new Cola<>();
  }

  @Test
  void colaNuevaEstaVaciaYSinElementos() {
    assertTrue(cola.estaVacia());
    assertEquals(0, cola.cantidad());
  }

  @Test
  void encolarAgregaElementosYActualizaLaCantidad() {
    cola.encolar("A");
    cola.encolar("B");

    assertFalse(cola.estaVacia());
    assertEquals(2, cola.cantidad());
  }

  @Test
  void desencolarRespetaElOrdenFifo() {
    cola.encolar("A");
    cola.encolar("B");
    cola.encolar("C");

    assertEquals("A", cola.desencolar());
    assertEquals("B", cola.desencolar());
    assertEquals("C", cola.desencolar());
    assertTrue(cola.estaVacia());
    assertEquals(0, cola.cantidad());
  }

  @Test
  void frenteDevuelveElPrimerElementoSinEliminarlo() {
    cola.encolar("A");
    cola.encolar("B");

    assertEquals("A", cola.frente());
    assertEquals("A", cola.frente());
    assertEquals(2, cola.cantidad());
  }

  @Test
  void desencolarEnColaVaciaLanzaExcepcion() {
    assertThrows(ColaVaciaException.class, () -> cola.desencolar());
  }

  @Test
  void consultarFrenteEnColaVaciaLanzaExcepcion() {
    assertThrows(ColaVaciaException.class, () -> cola.frente());
  }

  @Test
  void encolarNuloLanzaExcepcion() {
    assertThrows(IllegalArgumentException.class, () -> cola.encolar(null));
  }

  @Test
  void creceAutomaticamenteAlSuperarLaCapacidadInicial() {
    for (int i = 1; i <= 25; i++) {
      cola.encolar("E" + i);
    }

    assertEquals(25, cola.cantidad());
    for (int i = 1; i <= 25; i++) {
      assertEquals("E" + i, cola.desencolar());
    }
    assertTrue(cola.estaVacia());
  }

  @Test
  void conservaElOrdenTrasCrecerYDesencolarIntercalado() {
    for (int i = 1; i <= 12; i++) {
      cola.encolar("E" + i);
    }
    assertEquals("E1", cola.desencolar());
    assertEquals("E2", cola.desencolar());
    cola.encolar("E13");

    assertEquals(List.of("E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "E11", "E12", "E13"),
        cola.listar());
    assertEquals(11, cola.cantidad());
  }

  @Test
  void listarDevuelveUnaCopiaSinModificarLaCola() {
    cola.encolar("A");
    cola.encolar("B");

    List<String> copia = cola.listar();
    copia.clear();

    assertEquals(2, cola.cantidad());
    assertEquals("A", cola.frente());
  }
}
