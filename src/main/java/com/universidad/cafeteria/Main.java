package com.universidad.cafeteria;

import com.universidad.cafeteria.model.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("--- SISTEMA DE PEDIDOS CAFETERÍA ---\n");

    // 1. Creación de cliente (Semana 1 - Encapsulación)
    Cliente cliente = new Cliente("Carlos Mendoza", "carlos@mail.com", 50.0);
    System.out.println("Cliente registrado: " + cliente.getNombre() + " | Saldo: $" + cliente.getSaldoDisponible());

    // Prueba de validación de encapsulación en Cliente
    cliente.setSaldoDisponible(-10.0); // Debería mostrar mensaje de error

    System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");
    // 2. Creación de productos (Semana 2 - Herencia)
    ProductoComida sandwich = new ProductoComida("Sándwich Jamón y Queso", 4.50, 20, true);
    ProductoBebida cafe = new ProductoBebida("Café Americano", 2.50, 50, 350);
    ProductoBebida jugo = new ProductoBebida("Jugo de Naranja", 3.00, 30, 500);

    // Mostrar información utilizando polimorfismo y sobrescritura
    sandwich.mostrarInformacion();
    cafe.mostrarInformacion();
    jugo.mostrarInformacion();

    System.out.println("\n--- CREACIÓN DEL PEDIDO ---");
    // 3. Creación de Pedido (Semana 2 - Composición)
    Pedido pedido = new Pedido(cliente);
    pedido.agregarItem(new ItemPedido(sandwich, 2));
    pedido.agregarItem(new ItemPedido(cafe, 1));
    pedido.agregarItem(new ItemPedido(jugo, 1));

    // Imprimir resumen final
    pedido.mostrarResumen();
  }
}
