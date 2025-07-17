package com.shivaishta.prueba20;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import android.content.Context;
import java.io.*;

public class Compra {
    public Producto producto;
    public int cantidad;
    public double total;
    public String fecha;
    public static ArrayList<Compra> compras = new ArrayList<>();

    public Compra(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = (producto.getPrecioC() * cantidad);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fecha = LocalDate.now().format(formatter);
        compras.add(0,this);
    }

    //constructor para cargar
    public Compra(Producto producto, int cantidad, String fecha) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = (producto.getPrecioC() * cantidad);
        this.fecha = fecha; // usar la que viene del archivo
        compras.add(0,this);
    }

    public static void guardarC(Context context) {
        File archivoCompras = new File(context.getFilesDir(), "compras.txt");

        try (PrintWriter salida = new PrintWriter(new FileOutputStream(archivoCompras))) {
            for (int i = Compra.compras.size() - 1; i >= 0; i--) {
                Compra c = Compra.compras.get(i);
                salida.println(
                        c.producto.getCodigo() + "," +
                                c.producto.getPrecioC() + "," +
                                c.cantidad + "," +
                                c.fecha + "," +
                                c.total
                );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void cargarC(Context context) {
        File archivoCompras = new File(context.getFilesDir(), "compras.txt");

        if (!archivoCompras.exists()) return;

        try (BufferedReader leer = new BufferedReader(new FileReader(archivoCompras))) {
            Compra.compras.clear();
            String linea;
            while ((linea = leer.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length < 5) continue;

                int codigo = Integer.parseInt(partes[0]);
                double precioC = Double.parseDouble(partes[1]);
                int cantidad = Integer.parseInt(partes[2]);
                String fecha = partes[3];
                double total = Double.parseDouble(partes[4]);

                Producto encontrado = null;
                for (Producto p : Producto.getProductos()) {
                    if (p.getCodigo().equals(codigo)) {
                        encontrado = p;
                        break;
                    }
                }

                if (encontrado != null) {
                    Compra nueva = new Compra(encontrado, cantidad, fecha);
                    nueva.total = total;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


}