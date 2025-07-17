package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Producto {
    public static Producto[] productos;
    private String nombre;
    private int cantidad;
    private double precioV;
    private double precioC;
    private String categoria;
    private int codigo;
    private String urgente;

    public static final List<Producto> listaProductos = new ArrayList<>();

    // Constructor
    public Producto(String nombre, String categoria, double precioV, double precioC, int cantidad, int codigo, String urgente) {
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

    public int getCodigo() {
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

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    // Métodos estáticos
    public static List<Producto> getProductos() {
        return listaProductos;
    }

    public static void agregarProducto(Producto p) {
        listaProductos.add(p);
    }

    public static void eliminarProducto(Producto p) {
        listaProductos.remove(p);
    }

    // Guardar productos a archivo
    public static void guardarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Producto p : listaProductos) {
                pw.println(p.nombre + "," + p.cantidad + "," + p.precioV + "," + p.precioC + "," + p.categoria + "," + p.codigo + "," + p.urgente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar productos desde archivo
    public static void cargarProductos(Context context) {
        listaProductos.clear();
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
                        int codigo = Integer.parseInt(partes[5]);
                        String urgente = partes[6];
                        Producto p = new Producto(nombre, categoria, precioV, precioC, cantidad, codigo, urgente);
                        listaProductos.add(p);
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
