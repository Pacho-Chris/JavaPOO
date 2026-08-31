package com.universidad.cafeteria;

import com.universidad.cafeteria.model.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("--- SISTEMA DE PEDIDOS CAFETERÍA (SEMANA 3) ---\n");

    // 1. Catálogo de Productos
    ProductoComida sandwich = new ProductoComida("Sándwich Jamón y Queso", 4.50, 20, true);
    ProductoBebida cafe = new ProductoBebida("Café Americano", 2.50, 50, 350);

    // 2. Instanciación Polimórfica (Declarados como 'Cliente')
    Cliente clienteMayo = new ClienteMayorista("Distribuidora Central", "ventas@dist.com", 500.0,
        "Distribuidora Central S.A.");
    Cliente clienteMino = new ClienteMinorista("Ana López", "ana@mail.com", 30.0, true);

    // 3. Pedido 1 (Mayorista) -> Aplicará 15% de descuento
    Pedido pedido1 = new Pedido(clienteMayo);
    pedido1.agregarItem(new ItemPedido(sandwich, 10)); // $45.00
    pedido1.agregarItem(new ItemPedido(cafe, 5)); // $12.50 (Subtotal = $57.50)

    // 4. Pedido 2 (Minorista con tarjeta) -> Aplicará 5% de descuento
    Pedido pedido2 = new Pedido(clienteMino);
    pedido2.agregarItem(new ItemPedido(sandwich, 2)); // $9.00
    pedido2.agregarItem(new ItemPedido(cafe, 1)); // $2.50 (Subtotal = $11.50)

    // 5. Salida por Consola
    pedido1.mostrarResumen();
    System.out.println();
    pedido2.mostrarResumen();
  }
}
