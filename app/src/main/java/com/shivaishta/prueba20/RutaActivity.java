package com.shivaishta.prueba20;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RutaActivity extends AppCompatActivity {

    private ImageView imgRuta;
    private TextView tvTituloRuta, tvFechaRuta;
    private RecyclerView recyclerPedidos;
    private CardView cardPedidos;
    private Handler imgHandler = new Handler();
    private int imgIndex = 0;
    private int imgCount = 2;
    private String diaAbrev = "lun";
    private int[] imgResIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        Pedido.cargarPed(this);

        imgRuta = findViewById(R.id.imgRuta);
        tvTituloRuta = findViewById(R.id.tvTituloRuta);
        tvFechaRuta = findViewById(R.id.tvFechaRuta);
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        cardPedidos = findViewById(R.id.cardPedidos);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("es", "ES"));
        tvFechaRuta.setText(sdf.format(new Date()));

        List<Ruta> rutas = Ruta.rutasPorDefecto();
        Ruta rutaDeHoy = Ruta.rutaDeHoy(rutas);


        Log.d("RutaDebug", "Ruta de hoy: " + (rutaDeHoy != null ? rutaDeHoy.nombre : "null"));

        if (rutaDeHoy != null) {
            diaAbrev = abrevDiaHoy();
            imgCount = getCantidadImagenes(diaAbrev);
            imgResIds = getImgResIds(diaAbrev, imgCount);
            mostrarImagen();

            imgRuta.setOnClickListener(v -> {
                imgIndex = (imgIndex + 1) % imgCount;
                mostrarImagen();
            });

            imgHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgIndex = (imgIndex + 1) % imgCount;
                    mostrarImagen();
                    imgHandler.postDelayed(this, 4000);
                }
            }, 4000);

            List<Pedido> pedidosHoy = obtenerPedidosPorDia(rutaDeHoy.dia);
            List<Pedido> pedidosGuardados = obtenerPedidosGuardadosPorDia(rutaDeHoy.dia);


            pedidosHoy.addAll(pedidosGuardados);

            Log.d("RutaDebug", "Pedidos encontrados: " + pedidosHoy.size());

            if (!pedidosHoy.isEmpty()) {
                tvTituloRuta.setText("Ruta del día: " + rutaDeHoy.nombre);
                recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
                recyclerPedidos.setAdapter(new PedidoAdapter(pedidosHoy, pedido -> {
                    Intent intent = new Intent(this, DetallesPedidoActivity.class);
                    intent.putExtra("pedido", pedido);
                    startActivity(intent);
                }));
                cardPedidos.setVisibility(View.VISIBLE);
            } else {
                tvTituloRuta.setText("NO HAY ENTREGAS");
                recyclerPedidos.setAdapter(null);
                cardPedidos.setVisibility(View.VISIBLE);
            }
        } else {
            String diaTexto = new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(new Date());
            tvTituloRuta.setText("NO HAY ENTREGAS HOY (" + diaTexto.toUpperCase() + ")");
            imgRuta.setVisibility(View.GONE);
            cardPedidos.setVisibility(View.GONE);
        }
    }

    private List<Pedido> obtenerPedidosGuardadosPorDia(String dia) {
        List<Pedido> pedidosFiltrados = new ArrayList<>();
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String diaNormalizado = Ruta.normalizaDia(dia);

        for (Pedido pedido : Pedido.pedidos) {
            // Verificar si el pedido es para hoy
            if (pedido.getFecha().equals(fechaActual)) {
                pedidosFiltrados.add(pedido);
            }
        }

        return pedidosFiltrados;
    }

    private String abrevDiaHoy() {
        String hoy = new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(new Date());
        switch (Ruta.normalizaDia(hoy)) {
            case "lunes": return "lun";
            case "martes": return "mar";
            case "miercoles": return "mie";
            case "viernes": return "vie";
            case "sabado": return "sab";
            default: return "";
        }
    }

    private int getCantidadImagenes(String diaAbrev) {
        switch (diaAbrev) {
            case "lun": return 1;
            case "mar": return 2;
            case "mie": return 3;
            case "vie": return 1;
            case "sab": return 1;
            default: return 0;
        }
    }

    private int[] getImgResIds(String diaAbrev, int count) {
        int[] ids = new int[count];
        for (int i = 0; i < count; i++) {
            String name = "img_" + diaAbrev + (i + 1);
            int resId = getResources().getIdentifier(name, "drawable", getPackageName());
            ids[i] = resId != 0 ? resId : android.R.color.transparent;
        }
        return ids;
    }

    private void mostrarImagen() {
        try {
            if (imgResIds != null && imgIndex < imgResIds.length && imgResIds[imgIndex] != 0) {
                imgRuta.setVisibility(View.VISIBLE);
                imgRuta.setImageResource(imgResIds[imgIndex]);
            } else {
                imgRuta.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("RutaActivity", "Error al mostrar imagen", e);
        }
    }

    private List<Pedido> obtenerPedidosPorDia(String dia) {
        List<Pedido> pedidos = new ArrayList<>();
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String diaNormalizado = Ruta.normalizaDia(dia);

        switch (diaNormalizado) {
            case "lunes":
                pedidos.add(new Pedido(
                        new Cliente("Lucía Díaz", "3101112233", "Calle 1 #23-45", "Entrega rápida"),
                        List.of(String.valueOf(new Producto("Chocolatina", 10, 1.0).getCodigo()) + "_" + "10"),
                        fecha));
                break;

            case "martes":
                pedidos.add(new Pedido(
                        new Cliente("Marco Pérez", "3122674001", "Cra 12 #30-10", ""),
                        List.of(String.valueOf(new Producto("Galletas", 5, 2.0).getCodigo()) + "_" + "10"),
                        fecha));
                pedidos.add(new Pedido(
                        new Cliente("Sara Gómez", "3009876543", "Transv 19 #6-45", null),
                        List.of(String.valueOf(new Producto("Caramelos", 50, 0.2).getCodigo()) + "_" + "10"),
                        fecha));
                break;

            case "miercoles":
                pedidos.add(new Pedido(
                        new Cliente("Luis Torres", "3112223333", "Cl. 100 #20-30", "Urgente"),
                        List.of(String.valueOf(new Producto("Paletas", 30, 1.2).getCodigo()) + "_" + "10"),
                        fecha));
                break;

            case "sabado":
                pedidos.add(new Pedido(
                        new Cliente("Mariana Castro", "3119991111", "Cll 50 #10-22", null),
                        List.of(String.valueOf(new Producto("Chocorramo", 12, 1.5).getCodigo()) + "_" + "10"),
                        fecha));
                break;
        }
        return pedidos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgHandler.removeCallbacksAndMessages(null);
    }
}