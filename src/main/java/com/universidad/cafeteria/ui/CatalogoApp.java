package com.universidad.cafeteria.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CatalogoApp extends Application {
  @Override
  public void start(Stage stage) {
    CatalogoController controller = new CatalogoController();
    Scene escena = new Scene(controller.getRoot(), 1000, 640);
    stage.setTitle("Catálogo de Productos — Cafetería");
    stage.setScene(escena);
    stage.setMinWidth(880);
    stage.setMinHeight(560);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
