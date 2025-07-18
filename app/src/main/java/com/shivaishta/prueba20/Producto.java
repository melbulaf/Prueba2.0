package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.List;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nombre;
    private String categoria;
    private int cantidad;
    private double precioCompra;
    private double precioVenta;

    public Producto(String nombre, int codigo, String categoria, int cantidad, double precioCompra, double precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        Inventario.productos.add(this);
    }

    // Getters
    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioC() {
        return precioCompra;
    }

    public double getPrecio() {
        return precioVenta;
    }

    // Setters
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setCategoria(String Categoria) {
        this.categoria = Categoria;
    }

    // Lista de productos
    public static List<Producto> getProductos() {
        return Inventario.productos;
    }

    // Buscar por código
    public static Producto buscarPorCodigo(int codigo) {
        for (Producto p : Inventario.productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }
    
    // Guardar productos en archivo
    public static void guardarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(archivo))) {
            for (Producto p : Inventario.productos) {
                writer.println(p.getNombre() + "," + p.getCodigo() + "," + p.getCategoria() + "," +
                        p.getCantidad() + "," + p.getPrecioC() + "," + p.getPrecio());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar productos desde archivo
    public static void cargarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");

        if (!archivo.exists()) {
            System.out.println("Archivo productos.txt no encontrado.");
            return;
        }

        Inventario.productos.clear();

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
}
