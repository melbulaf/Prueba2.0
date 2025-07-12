// Pedido.java
package com.shivaishta.prueba20;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private Cliente cliente;
    private List<Producto> productos;
    private String fecha;

    public Pedido(Cliente cliente, List<Producto> productos, String fecha) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public String getFecha() { return fecha; }
}