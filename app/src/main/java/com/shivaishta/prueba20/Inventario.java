package com.shivaishta.prueba20;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Inventario {

    public static ArrayList<Producto> productos = new ArrayList<>();
    public static Inventario instancia;

    public Inventario() {
        // Ya está inicializada, así que aquí no es necesario reasignar
    }

    public static Inventario getInstancia() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    public ArrayList<Producto> obtenerProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }


    public static void cargarProductos(Context context) {

        File archivo = new File(context.getFilesDir(), "productos.txt");
        Log.d("DEBUG_CARGAR", "Cargando productos...");
        Log.d("DEBUG_CARGAR", "Archivo existe: " + archivo.exists());

        if (!archivo.exists()) {
            Log.d("DEBUG_CARGAR", "Archivo productos.txt no encontrado.");
            return;
        }

        productos.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            StringBuilder contenido = new StringBuilder();
            BufferedReader readerDebug = new BufferedReader(new FileReader(archivo));
            String lineaDebug;
            while ((lineaDebug = readerDebug.readLine()) != null) {
                Log.d("DEBUG_ARCHIVO", "Línea: " + lineaDebug);
                contenido.append(lineaDebug).append("\n");
            }
            readerDebug.close();
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 6) {
                    String nombre = partes[0];
                    int codigo = Integer.parseInt(partes[1]);
                    String categoria = partes[2];
                    int cantidad = Integer.parseInt(partes[3]);
                    double precioCompra = Double.parseDouble(partes[4]);
                    double precioVenta = Double.parseDouble(partes[5]);

                    Producto nuevo = new Producto(nombre, codigo, categoria, cantidad, precioCompra, precioVenta);
                    productos.add(nuevo);
                    Log.d("DEBUG_CARGAR", "Producto cargado: " + nuevo.getNombre());
                } else {
                    Log.e("DEBUG_CARGAR", "Línea inválida: " + linea);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        Log.d("DEBUG_CARGAR", "TOTAL productos cargados: " + productos.size());
    }

    public static void guardarProductos(Context context) {
        Log.d("DEBUG_GUARDAR", "Guardando productos: tamaño = " + productos.size());
        File archivo = new File(context.getFilesDir(), "productos.txt");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(archivo))) {
            for (Producto p : productos) {
                Log.d("DEBUG_GUARDAR", p.getNombre() + " - Cantidad: " + p.getCantidad());
                writer.println(p.getNombre() + "," + p.getCodigo() + "," + p.getCategoria() + "," +
                        p.getCantidad() + "," + p.getPrecioC() + "," + p.getPrecio());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}