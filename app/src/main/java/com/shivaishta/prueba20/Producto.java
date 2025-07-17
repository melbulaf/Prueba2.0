// Producto.java
package com.shivaishta.prueba20;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {
    private final int codigo;
    private static int contador = 1000;
    private String nombre;
    private int cantidad;
    private double precio;
    private double precioC;
    private String categoria;
    public static ArrayList<Producto> productos = new ArrayList<>();



    public Producto(String nombre, int cantidad, double precio) {
        this.codigo = contador++;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.precioC = precio - (precio*0.15);
        this.categoria = "Sin Categoria";
        productos.add(this);
    }

    public Producto(String nombre, String categoria, double precio,double precioC, int cantidad) {
        this.codigo = contador++;
        this.nombre = nombre;
        this.precio = precio;
        this.precioC = precioC;
        this.cantidad = cantidad;
        this.categoria = categoria;
        productos.add(this);
    }

    // Getters
    public int getCodigo(){
        return this.codigo;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getPrecioC() {return precioC; }
    public String getCategoria() {return categoria;}

    //Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecioC(double precioC) { this.precioC = precioC; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return this.codigo  + " " + this.nombre;
    }
}