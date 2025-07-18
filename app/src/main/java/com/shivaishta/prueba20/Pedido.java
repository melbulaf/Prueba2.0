package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    public static ArrayList<Pedido> pedidos = new ArrayList<>();

    private Cliente cliente;
    private List<String> productos; // Formato "codigo_cantidad" ej: {"1000_10", "1001_7"}
    private String fecha;
    private boolean confirmado;

    public Pedido(Cliente cliente, List<String> productos, String fecha) {
        this.cliente = cliente;
        this.productos = (productos != null) ? productos : new ArrayList<>();
        this.fecha = fecha;
        this.confirmado = false;
        pedidos.add(this);
    }

    public Pedido(Cliente cliente, List<String> productos, String fecha, boolean estado) {
        this.cliente = cliente;
        this.productos = (productos != null) ? productos : new ArrayList<>();
        this.fecha = fecha;
        this.confirmado = estado;
        pedidos.add(this);
    }

    // Getters
    public Cliente getCliente() {
        return cliente;
    }

    public List<String> getProductos() {
        if (productos == null) productos = new ArrayList<>();
        return productos;
    }

    public String getFecha() {
        return fecha;
    }

    public boolean getConfirmado() {
        return confirmado;
    }

    public void confirmar() {
        for (String ped : this.getProductos()) {
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
                this.confirmado = true;
                encontrado.setCantidad(encontrado.getCantidad() - cantidad);
            }
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

        if (!archivoPedidos.exists()) {
            System.out.println("Archivo pedidos.txt no existe.");
            return;
        }

        try (BufferedReader leer = new BufferedReader(new FileReader(archivoPedidos))) {
            Pedido.pedidos.clear();
            String linea;
            while ((linea = leer.readLine()) != null) {
                try {
                    String[] partes = linea.split("!");
                    if (partes.length != 4) {
                        System.err.println("Línea malformada: " + linea);
                        continue;
                    }

                    List<String> productos = new ArrayList<>(Arrays.asList(partes[0].split(",")));
                    String nombreCliente = partes[1].trim();
                    String fecha = partes[2].trim();
                    boolean confirmado = Boolean.parseBoolean(partes[3].trim());

                    Cliente clienteEncontrado = null;
                    for (Cliente c : Cliente.getClientes()) {
                        if (c.getNombre().equals(nombreCliente)) {
                            clienteEncontrado = c;
                            break;
                        }
                    }

                    if (clienteEncontrado == null) {
                        System.err.println("Cliente no encontrado: " + nombreCliente);
                        continue;
                    }

                    new Pedido(clienteEncontrado, productos, fecha, confirmado);

                } catch (Exception e) {
                    System.err.println("Error procesando línea: " + linea);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}