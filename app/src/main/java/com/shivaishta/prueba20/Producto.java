package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String nombre;
    private int cantidad;
    private double precioV;
    private double precioC;
    private String categoria;
    private String codigo;
    private String urgente;

    public static final List<Producto> productos = new ArrayList<>();

    // Constructor
    public Producto(String nombre, int cantidad, double precioV, double precioC, String categoria, String codigo, String urgente) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioV = precioV;
        this.precioC = precioC;
        this.categoria = categoria;
        this.codigo = codigo;
        this.urgente = urgente;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioV() {
        return precioV;
    }

    public double getPrecioC() {
        return precioC;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getUrgente() {
        return urgente;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioV(double precioV) {
        this.precioV = precioV;
    }

    public void setPrecioC(double precioC) {
        this.precioC = precioC;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    // Métodos estáticos
    public static List<Producto> getProductos() {
        return productos;
    }

    public static void agregarProducto(Producto p) {
        productos.add(p);
    }

    public static void eliminarProducto(Producto p) {
        productos.remove(p);
    }

    // Guardar productos a archivo
    public static void guardarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Producto p : productos) {
                pw.println(p.nombre + "," + p.cantidad + "," + p.precioV + "," + p.precioC + "," + p.categoria + "," + p.codigo + "," + p.urgente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar productos desde archivo
    public static void cargarProductos(Context context) {
        productos.clear();
        File archivo = new File(context.getFilesDir(), "productos.txt");
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length >= 7) {
                    try {
                        String nombre = partes[0];
                        int cantidad = Integer.parseInt(partes[1]);
                        double precioV = Double.parseDouble(partes[2]);
                        double precioC = Double.parseDouble(partes[3]);
                        String categoria = partes[4];
                        String codigo = partes[5];
                        String urgente = partes[6];
                        Producto p = new Producto(nombre, cantidad, precioV, precioC, categoria, codigo, urgente);
                        productos.add(p);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
