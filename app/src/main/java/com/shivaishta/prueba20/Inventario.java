package com.shivaishta.prueba20;

import java.util.ArrayList;

/**
 *
 * @author Samuel
 */
public class Inventario {

    // Inicializada para que nunca sea null
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
}