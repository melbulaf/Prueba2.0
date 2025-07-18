package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable{
    private String nombre;
    private String telefono;
    private String direccion;
    private String urgencia;

    private static final List<Cliente> listaClientes = new ArrayList<>();

    // Constructor
    public Cliente(String nombre, String telefono, String direccion, String urgencia) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.urgencia = urgencia;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public String getUr() {
        return urgencia == null ? "" : urgencia;
    }

    // Método requerido en otras clases (como RutaActivity o Factura)
    public String getTelefonoInternacional() {
        // Convierte número nacional colombiano a formato internacional si comienza con '3'
        if (telefono != null && telefono.length() == 10 && telefono.startsWith("3")) {
            return "+57" + telefono;
        }
        return telefono;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    // Métodos estáticos
    public static List<Cliente> getClientes() {
        return listaClientes;
    }

    public static void agregarCliente(Cliente c) {
        listaClientes.add(c);
    }

    public static void eliminarCliente(Cliente c) {
        listaClientes.remove(c);
    }

    // Guardar clientes a archivo
    public static void guardarClientes(Context context) {
        File archivo = new File(context.getFilesDir(), "clientes.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Cliente c : listaClientes) {
                pw.println(c.nombre + "," + c.telefono + "," + c.direccion + "," + c.urgencia);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar clientes desde archivo
    public static void cargarClientes(Context context) {
        listaClientes.clear();
        File archivo = new File(context.getFilesDir(), "clientes.txt");
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length >= 4) {
                    Cliente c = new Cliente(partes[0], partes[1], partes[2], partes[3]);
                    listaClientes.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
