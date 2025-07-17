// Pedido.java
package com.shivaishta.prueba20;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
        pedidos.add(this);
    }

    public Pedido(Cliente cliente, List<String> productos, String fecha, boolean estado) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
        this.confirmado = estado;
        pedidos.add(this);
    }

    // Getters
    public Cliente getCliente() { return cliente; }
    public List<String> getProductos() { return productos; }
    public String getFecha() { return fecha; }
    public boolean getConfirmado() {return confirmado;}
    public void confirmar() {
        this.confirmado = true;
        for (String ped : this.productos) {
            String[] partes = ped.split("_");
            if (partes.length != 2) continue;
            int codigoBuscado = Integer.parseInt(partes[0]);
            int cantidad = Integer.parseInt(partes[1]);
            Producto encontrado = null;
            for (Producto p : Producto.productos) {
                if (p.getCodigo() == codigoBuscado) {
                    encontrado = p;
                    break;
                }
            }
            if (encontrado != null) {
            encontrado.setCantidad(encontrado.getCantidad() + cantidad); }
        }
    }

    public static void guardarPed(Context context) {
        File archivoPedidos = new File(context.getFilesDir(), "pedidos.txt");

        try (PrintWriter salida = new PrintWriter(new FileOutputStream(archivoPedidos))) {
            for (int i = Pedido.pedidos.size() - 1; i >= 0; i--) {
                Pedido c = Pedido.pedidos.get(i);
                salida.println(
                        String.join(",", c.getProductos()) + "!" +
                                c.getCliente().getNombre() + "!" +
                                c.getFecha() + "!" +
                                c.getConfirmado()
                );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void cargarPed(Context context) {
        File archivoPedidos = new File(context.getFilesDir(), "pedidos.txt");

        if (!archivoPedidos.exists()) return;

        try (BufferedReader leer = new BufferedReader(new FileReader(archivoPedidos))) {
            Pedido.pedidos.clear();
            String linea;
            while ((linea = leer.readLine()) != null) {
                String[] partes = linea.split("!");
                if (partes.length != 4) continue;

                List<String> productos = new ArrayList<>(Arrays.asList(partes[0].split(",")));
                String cliente = partes[1];
                String fecha = partes[2];
                String confirmado = partes[3];
                boolean conf = Boolean.parseBoolean(confirmado);
                Cliente encontrado = null;
                for (Cliente c : Cliente.getClientes()) {
                    if (c.getNombre().equals(cliente)) {
                        encontrado = c;
                        break;
                    }
                }

                if (encontrado != null) {
                    new Pedido(encontrado, productos, fecha, conf);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

}