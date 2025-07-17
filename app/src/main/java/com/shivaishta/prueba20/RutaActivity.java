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

        imgRuta = findViewById(R.id.imgRuta);
        tvTituloRuta = findViewById(R.id.tvTituloRuta);
        tvFechaRuta = findViewById(R.id.tvFechaRuta);
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        cardPedidos = findViewById(R.id.cardPedidos);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("es", "ES"));
        tvFechaRuta.setText(sdf.format(new Date()));

        // Cargar pedidos desde archivo
        Pedido.cargarPed(this);

        // Obtener pedidos para hoy no confirmados
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        List<Pedido> pedidosHoy = new ArrayList<>();

        for (Pedido p : Pedido.pedidos) {
            if (p.getFecha().equals(fechaHoy) && !p.getConfirmado()) {
                pedidosHoy.add(p);
            }
        }

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

        if (!pedidosHoy.isEmpty()) {
            String diaHoy = new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(new Date());
            tvTituloRuta.setText("Ruta del día: " + diaHoy);

            recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
            recyclerPedidos.setAdapter(new PedidoAdapter(pedidosHoy, pedido -> {
                Intent intent = new Intent(this, DetallesPedidoActivity.class);
                intent.putExtra("pedido", pedido);
                startActivity(intent);
            }));
            cardPedidos.setVisibility(View.VISIBLE);
        } else {
            String diaTexto = new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(new Date());
            tvTituloRuta.setText("NO HAY ENTREGAS HOY (" + diaTexto.toUpperCase() + ")");
            imgRuta.setVisibility(View.GONE);
            cardPedidos.setVisibility(View.GONE);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgHandler.removeCallbacksAndMessages(null);
    }
}