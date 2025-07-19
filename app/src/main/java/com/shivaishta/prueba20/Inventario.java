package com.shivaishta.prueba20;

import android.content.Context;
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

        if (!archivo.exists()) {
            System.out.println("Archivo productos.txt no encontrado.");
            return;
        }

        productos.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 6) {
                    String nombre = partes[0];
                    int codigo = Integer.parseInt(partes[1]);
                    String categoria = partes[2];
                    int cantidad = Integer.parseInt(partes[3]);
                    int precioCompra = Integer.parseInt(partes[4]);
                    int precioVenta = Integer.parseInt(partes[5]);

                    new Producto(nombre, codigo, categoria, cantidad, precioCompra, precioVenta);
                } else {
                    System.err.println("Línea inválida en productos.txt: " + linea);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public static void guardarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(archivo))) {
            for (Producto p : productos) {
                writer.println(p.getNombre() + "," + p.getCodigo() + "," + p.getCategoria() + "," +
                        p.getCantidad() + "," + p.getPrecioC() + "," + p.getPrecio());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}