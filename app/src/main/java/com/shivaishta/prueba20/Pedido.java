// Pedido.java
package com.shivaishta.prueba20;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    public static ArrayList<Pedido> pedidos = new ArrayList<>();
    private Cliente cliente;
    private List<String> productos; //formato string para codigo y cantidad separados por _
                                    //Ej: {"1000_10","1001_7"}
    private String fecha;
    private boolean confirmado;

    public Pedido(Cliente cliente, List<String> productos, String fecha) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
        this.confirmado = false;;
        pedidos.add(this);;
    }

    public Pedido(Cliente cliente, List<String> productos, String fecha, boolean estado) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
        this.confirmado = estado;
        pedidos.add(this);;
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public List<String> getProductos() { return productos; }
    public String getFecha() { return fecha; }
    public boolean getConfirmado() {return confirmado;}
    public void setConfirmado(boolean estado) {this.confirmado = estado;}
}