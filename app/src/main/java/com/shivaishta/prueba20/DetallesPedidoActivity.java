package com.shivaishta.prueba20;

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
        btnFacturar.setText("Generar Factura");
        btnFacturar.setBackgroundColor(Color.parseColor("#3F51B5"));
        btnFacturar.setTextColor(Color.WHITE);
        btnFacturar.setOnClickListener(v -> abrirFacturaActivity());

        // Agregar botón al layout
        LinearLayout containerBotones = findViewById(R.id.containerBotones);
        if (containerBotones == null) {
            // Si no existe el contenedor, lo creamos
            containerBotones = new LinearLayout(this);
            containerBotones.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            containerBotones.setOrientation(LinearLayout.VERTICAL);
            containerBotones.setPadding(16, 16, 16, 16);

            // Agregar al layout principal
            LinearLayout mainLayout = findViewById(R.id.mainLayout);
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

        if (pedido.getCliente().esUrgente()) {
            tvInfoUrgencia.setVisibility(View.VISIBLE);
            tvInfoUrgencia.setText("¡PEDIDO URGENTE!");
        }
    }

    private void mostrarProductos() {
        LinearLayout containerProductos = findViewById(R.id.containerProductos);
        double total = 0;

        for (Producto producto : pedido.getProductos()) {
            // Calcular subtotal
            double subtotal = producto.getPrecio() * producto.getCantidad();
            total += subtotal;

            // Crear TextView para el producto
            TextView tvProducto = new TextView(this);
            tvProducto.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            tvProducto.setText(String.format(Locale.getDefault(),
                    "• %s (Cantidad: %d)",
                    producto.getNombre(),
                    producto.getCantidad()));

            tvProducto.setTextSize(16);
            tvProducto.setTextColor(Color.parseColor("#333333"));
            tvProducto.setPadding(8, 12, 8, 12);

            // Separador
            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1));
            separator.setBackgroundColor(Color.parseColor("#E0E0E0"));

            containerProductos.addView(tvProducto);
            containerProductos.addView(separator);
        }
    }
    private void abrirFacturaActivity() {
        Intent intent = new Intent(this, FacturaActivity.class);
        intent.putExtra("pedido", pedido);
        startActivity(intent);
    }
}