package com.universidad.cafeteria.repository;

public class PersistenciaException extends RuntimeException {
  public PersistenciaException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }

  public PersistenciaException(String mensaje) {
    super(mensaje);
  }
}
