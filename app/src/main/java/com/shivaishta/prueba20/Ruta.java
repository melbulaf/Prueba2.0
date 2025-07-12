package com.shivaishta.prueba20;

import android.os.Build;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Ruta {
    public String nombre;
    public String dia;

    public Ruta(String nombre, String dia) {
        this.nombre = nombre;
        this.dia = dia;
    }

    public static List<Ruta> rutasPorDefecto() {
        List<Ruta> rutas = new ArrayList<>();
        rutas.add(new Ruta("Centro", "lunes"));
        rutas.add(new Ruta("Norte", "martes"));
        rutas.add(new Ruta("Sur", "miércoles"));
        rutas.add(new Ruta("Oeste", "viernes"));
        rutas.add(new Ruta("Especial", "sábado"));
        return rutas;
    }

    public static Ruta rutaDeHoy(List<Ruta> rutas) {
        String hoy = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hoy = LocalDate.now().getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        }
        hoy = normalizaDia(hoy);
        for (Ruta r : rutas) {
            if (normalizaDia(r.dia).equals(hoy)) return r;
        }
        return null;
    }

    public static String normalizaDia(String dia) {
        if (dia == null) return "";
        return Normalizer.normalize(dia, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }
}