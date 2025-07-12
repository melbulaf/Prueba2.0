package com.shivaishta.prueba20;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String telefono;
    private String direccion;
    private String motivoUrgencia;

    public Cliente(String nombre, String telefono, String direccion, String motivoUrgencia) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.motivoUrgencia = motivoUrgencia;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getMotivoUrgencia() { return motivoUrgencia; }
    public boolean esUrgente() { return motivoUrgencia != null && !motivoUrgencia.isEmpty(); }


    public String getTelefonoInternacional() {
        if (telefono != null && !telefono.isEmpty()) {
            if (telefono.startsWith("+57")) return telefono.substring(1);
            if (telefono.startsWith("57")) return telefono;
            if (telefono.startsWith("0")) return "57" + telefono.substring(1);
            return "57" + telefono;
        }
        return "";
    }
}