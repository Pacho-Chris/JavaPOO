package com.universidad.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.universidad.cafeteria.model.Producto;
import com.universidad.cafeteria.model.ProductoBebida;
import com.universidad.cafeteria.model.ProductoComida;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CatalogoJsonRepository implements CatalogoRepository {
  private static final Path RUTA_POR_DEFECTO = Path.of("datos", "catalogo.json");
  private static final String TIPO_COMIDA = "COMIDA";
  private static final String TIPO_BEBIDA = "BEBIDA";

  private final Path rutaArchivo;
  private final Gson gson;

  public CatalogoJsonRepository() {
    this(RUTA_POR_DEFECTO);
  }

  public CatalogoJsonRepository(Path rutaArchivo) {
    this.rutaArchivo = rutaArchivo;
    this.gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Producto.class, new ProductoSerializer())
        .registerTypeAdapter(Producto.class, new ProductoDeserializer())
        .create();
  }

  @Override
  public List<Producto> cargar() {
    if (!Files.exists(rutaArchivo)) {
      return new ArrayList<>();
    }
    try (Reader lector = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
      Producto[] productos = gson.fromJson(lector, Producto[].class);
      if (productos == null) {
        return new ArrayList<>();
      }
      return new ArrayList<>(List.of(productos));
    } catch (IOException | JsonParseException e) {
      throw new PersistenciaException("No se pudo leer el catálogo desde " + rutaArchivo + ".", e);
    }
  }

  @Override
  public void guardar(List<Producto> productos) {
    try {
      Path directorio = rutaArchivo.getParent();
      if (directorio != null) {
        Files.createDirectories(directorio);
      }
      try (Writer escritor = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
        gson.toJson(productos.toArray(new Producto[0]), Producto[].class, escritor);
      }
    } catch (IOException e) {
      throw new PersistenciaException("No se pudo guardar el catálogo en " + rutaArchivo + ".", e);
    }
  }

  private static class ProductoSerializer implements JsonSerializer<Producto> {
    @Override
    public JsonElement serialize(Producto producto, Type tipo, JsonSerializationContext contexto) {
      JsonObject objeto = new JsonObject();
      objeto.addProperty("tipo", producto instanceof ProductoComida ? TIPO_COMIDA : TIPO_BEBIDA);
      objeto.addProperty("codigo", producto.getCodigo());
      objeto.addProperty("nombre", producto.getNombre());
      objeto.addProperty("precioBase", producto.getPrecioBase());
      objeto.addProperty("stock", producto.getStock());
      if (producto instanceof ProductoComida comida) {
        objeto.addProperty("esAptoCeliacos", comida.isEsAptoCeliacos());
      } else if (producto instanceof ProductoBebida bebida) {
        objeto.addProperty("tamanoMl", bebida.getTamanoMl());
      }
      return objeto;
    }
  }

  private static class ProductoDeserializer implements JsonDeserializer<Producto> {
    @Override
    public Producto deserialize(JsonElement json, Type tipo, JsonDeserializationContext contexto)
        throws JsonParseException {
      JsonObject objeto = json.getAsJsonObject();
      String tipoProducto = leerTexto(objeto, "tipo");
      String codigo = leerTexto(objeto, "codigo");
      String nombre = leerTexto(objeto, "nombre");
      double precioBase = leerDecimal(objeto, "precioBase");
      int stock = leerEntero(objeto, "stock");

      if (TIPO_COMIDA.equals(tipoProducto)) {
        return new ProductoComida(codigo, nombre, precioBase, stock, leerBooleano(objeto, "esAptoCeliacos"));
      }
      if (TIPO_BEBIDA.equals(tipoProducto)) {
        return new ProductoBebida(codigo, nombre, precioBase, stock, leerEntero(objeto, "tamanoMl"));
      }
      throw new JsonParseException("Tipo de producto desconocido en el archivo: " + tipoProducto);
    }

    private String leerTexto(JsonObject objeto, String campo) {
      JsonPrimitive valor = leerCampo(objeto, campo);
      if (!valor.isString()) {
        throw new JsonParseException("El campo \"" + campo + "\" debe ser texto.");
      }
      return valor.getAsString();
    }

    private double leerDecimal(JsonObject objeto, String campo) {
      JsonPrimitive valor = leerCampo(objeto, campo);
      try {
        return valor.getAsDouble();
      } catch (NumberFormatException e) {
        throw new JsonParseException("El campo \"" + campo + "\" debe ser un número.", e);
      }
    }

    private int leerEntero(JsonObject objeto, String campo) {
      JsonPrimitive valor = leerCampo(objeto, campo);
      try {
        return valor.getAsInt();
      } catch (NumberFormatException e) {
        throw new JsonParseException("El campo \"" + campo + "\" debe ser un entero.", e);
      }
    }

    private boolean leerBooleano(JsonObject objeto, String campo) {
      JsonPrimitive valor = leerCampo(objeto, campo);
      if (!valor.isBoolean()) {
        throw new JsonParseException("El campo \"" + campo + "\" debe ser booleano.");
      }
      return valor.getAsBoolean();
    }

    private JsonPrimitive leerCampo(JsonObject objeto, String campo) {
      JsonElement valor = objeto.get(campo);
      if (valor == null || valor.isJsonNull() || !valor.isJsonPrimitive()) {
        throw new JsonParseException("Falta el campo obligatorio \"" + campo + "\" en el catálogo.");
      }
      return valor.getAsJsonPrimitive();
    }
  }
}
