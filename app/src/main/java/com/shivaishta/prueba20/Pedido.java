package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.*;

public class Pedido implements Serializable {
    public static ArrayList<Pedido> pedidos = new ArrayList<>();
    private Cliente cliente;
    private List<String> productos; // Ej: {"1000_10","1001_7"}
    private String fecha;
    private boolean confirmado;

    private void descontarInventario() {
        for (String ped : this.productos) {
            String[] partes = ped.split("_");
            if (partes.length != 2) continue;
            int codigoBuscado = Integer.parseInt(partes[0]);
            int cantidad = Integer.parseInt(partes[1]);

            Producto encontrado = Producto.buscarPorCodigo(codigoBuscado);
            if (encontrado != null) {
                encontrado.setCantidad(encontrado.getCantidad() - cantidad);
            }
        }
    }



    public Pedido(Cliente cliente, List<String> productos, String fecha) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
        this.confirmado = false;
        descontarInventario();
        Pedido.pedidos.add(this);
    }

    public Pedido(Cliente cliente, List<String> productos, String fecha, boolean estado) {
        this.cliente = cliente;
        this.productos = productos;
        this.fecha = fecha;
        this.confirmado = estado;
        if (!estado) {
            descontarInventario();
        }
        Pedido.pedidos.add(this);
    }

    public Cliente getCliente() { return cliente; }
    public List<String> getProductos() { return productos; }
    public String getFecha() { return fecha; }
    public boolean getConfirmado() { return confirmado; }

    public void confirmar() {
        this.confirmado = true;
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
                String nombreCliente = partes[1];
                String fecha = partes[2];
                boolean conf = Boolean.parseBoolean(partes[3]);

                Cliente encontrado = null;
                for (Cliente c : Cliente.getClientes()) {
                    if (c.getNombre().equals(nombreCliente)) {
                        encontrado = c;
                        break;
                    }
                }

                if (encontrado != null) {
                    new Pedido(encontrado, productos, fecha, conf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
