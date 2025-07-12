// Producto.java
package com.shivaishta.prueba20;

import java.io.Serializable;

public class Producto implements Serializable {
    private String nombre;
    private int cantidad;
    private double precio;

    public Producto(String nombre, int cantidad, double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
}