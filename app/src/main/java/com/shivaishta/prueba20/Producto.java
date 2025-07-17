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
<<<<<<< HEAD

    public String getCategoria() {
        return categoria;
    }

    public int getCodigo() {
        return codigo;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioV(double precioV) {
        this.precioV = precioV;
    }

    public void setPrecioC(double precioC) {
        this.precioC = precioC;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Métodos estáticos
    public static List<Producto> getProductos() {
        return listaProductos;
    }

    public static void agregarProducto(Producto p) {
        listaProductos.add(p);
    }

    public static void eliminarProducto(Producto p) {
        listaProductos.remove(p);
    }

    // Guardar productos a archivo
    public static void guardarProductos(Context context) {
        File archivo = new File(context.getFilesDir(), "productos.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Producto p : listaProductos) {
                pw.println(p.nombre + "," + p.cantidad + "," + p.precioV + "," + p.precioC + "," + p.categoria + "," + p.codigo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar productos desde archivo
    public static void cargarProductos(Context context) {
        listaProductos.clear();
        File archivo = new File(context.getFilesDir(), "productos.txt");
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length >= 7) {
                    try {
                        String nombre = partes[0];
                        int cantidad = Integer.parseInt(partes[1]);
                        double precioV = Double.parseDouble(partes[2]);
                        double precioC = Double.parseDouble(partes[3]);
                        String categoria = partes[4];
                        String urgente = partes[6];
                        Producto p = new Producto(nombre, cantidad, precioV, precioC, categoria);
                        listaProductos.add(p);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
=======
}
>>>>>>> parent of 7166499 (push)
