package com.shivaishta.prueba20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class DetallesPedidoActivity extends AppCompatActivity {

    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        // Obtener pedido del Intent
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        if (pedido == null) {
            finish();
            return;
        }

        // Configurar botón de volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configurar información del cliente
        mostrarInfoCliente();

        // Configurar lista de productos
        mostrarProductos();

        // Configurar botón de volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Agregar botón de facturación
        Button btnFacturar = new Button(this);
        btnFacturar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btnFacturar.setText("Confirmar Venta");
        btnFacturar.setBackgroundColor(Color.parseColor("#3F51B5"));
        btnFacturar.setTextColor(Color.WHITE);
        btnFacturar.setOnClickListener(v -> abrirVentaActivity());

        // Agregar botón al layout
        LinearLayout containerBotones = findViewById(R.id.containerBotones);
        if (containerBotones == null) {
            containerBotones = new LinearLayout(this);
            containerBotones.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            containerBotones.setOrientation(LinearLayout.VERTICAL);
            containerBotones.setPadding(16, 16, 16, 16);

            @SuppressLint("WrongViewCast") LinearLayout mainLayout = findViewById(R.id.mainLayout);
            mainLayout.addView(containerBotones);
        }
        containerBotones.addView(btnFacturar);
    }

    private void mostrarInfoCliente() {
        TextView tvCliente = findViewById(R.id.tvCliente);
        TextView tvDireccion = findViewById(R.id.tvDireccion);
        TextView tvInfoUrgencia = findViewById(R.id.tvInfoUrgencia);

        tvCliente.setText("Cliente: " + pedido.getCliente().getNombre());
        tvDireccion.setText("Dirección: " + pedido.getCliente().getDireccion());

        if (!pedido.getCliente().getUrgencia().isEmpty()) {
            tvInfoUrgencia.setVisibility(View.VISIBLE);
            tvInfoUrgencia.setText("¡PEDIDO URGENTE!");
        } else {
            tvInfoUrgencia.setVisibility(View.GONE);
        }
    }

    private void mostrarProductos() {
        LinearLayout containerProductos = findViewById(R.id.containerProductos);
        double total = 0;

        for (String p : pedido.getProductos()) {
            String[] partes = p.split("_");
            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);
            Producto producto = null;

            for (Producto pp : Inventario.productos) {
                if (pp.getCodigo() == codigo) {
                    producto = pp;
                    break;
                }
            }

            if (producto != null) {
                double subtotal = producto.getPrecio() * cant; // Usar precio de venta
                total += subtotal;

                TextView tvProducto = new TextView(this);
                tvProducto.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                tvProducto.setText(String.format(Locale.getDefault(),
                        "• %s (Cantidad: %d)",
                        producto.getNombre(),
                        cant));

                tvProducto.setTextSize(16);
                tvProducto.setTextColor(Color.parseColor("#333333"));
                tvProducto.setPadding(8, 12, 8, 12);

                View separator = new View(this);
                separator.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1));
                separator.setBackgroundColor(Color.parseColor("#E0E0E0"));

                containerProductos.addView(tvProducto);
                containerProductos.addView(separator);
            }
        }
    }

    private void abrirVentaActivity() {
        Intent intent = new Intent(this, VentasActivity.class);
        intent.putExtra("pedido", pedido);
        startActivity(intent);
    }
}
