package com.shivaishta.prueba20;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {
    private String nombre;
    private String telefono;
    private String direccion;
    private String motivoUrgencia;

    // Lista estática para mantener los clientes en memoria
    public static final List<Cliente> clientes = new ArrayList<>();
    private static final String ARCHIVO = "clientes.txt";

    // Constructor
    public Cliente(String nombre, String telefono, String direccion, String motivoUrgencia) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.motivoUrgencia = motivoUrgencia;
    }

    // === Getters ===
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getMotivoUrgencia() { return motivoUrgencia; }
    public boolean esUrgente() {
        return motivoUrgencia != null && !motivoUrgencia.isEmpty();
    }

    public String getTelefonoInternacional() {
        if (telefono != null && !telefono.isEmpty()) {
            if (telefono.startsWith("+57")) return telefono.substring(1);
            if (telefono.startsWith("57")) return telefono;
            if (telefono.startsWith("0")) return "57" + telefono.substring(1);
            return "57" + telefono;
        }
        return "";
    }
    // === Setters ===
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setMotivoUrgencia(String motivoUrgencia) { this.motivoUrgencia = motivoUrgencia; }


    // === Acceso a la lista temporal ===
    public static List<Cliente> getClientes() {
        return clientes;
    }

    public static void agregarCliente(Cliente c) {
        clientes.add(c);
    }

    public static boolean existeCliente(String telefono, String nombre) {
        for (Cliente c : clientes) {
            if (c.getTelefono().equals(telefono) || c.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // === Persistencia (guardar) ===
    public static void guardarClientes(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(ARCHIVO, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            for (Cliente c : clientes) {
                writer.write(c.nombre + "%%" + c.telefono + "%%" + c.direccion + "%%" + c.motivoUrgencia);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // === Carga desde archivo ===
    public static void cargarClientes(Context context) {
        clientes.clear();
        try {
            FileInputStream fis = context.openFileInput(ARCHIVO);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("%%");
                if (partes.length == 4) {
                    Cliente c = new Cliente(partes[0], partes[1], partes[2], partes[3]);
                    clientes.add(c);
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            // El archivo aún no existe, no hay problema
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
