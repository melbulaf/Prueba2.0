package com.shivaishta.prueba20;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventario implements Serializable {

    public static ArrayList<Producto> productos;
    public static Inventario instancia;
    public Inventario() {
        productos = new ArrayList<>();
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
