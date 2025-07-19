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

}
