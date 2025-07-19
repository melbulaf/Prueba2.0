// DetallesPedidoActivity.java actualizado
package com.shivaishta.prueba20;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        // Botón volver (arriba)
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Mostrar datos
        mostrarInfoCliente();
        mostrarProductos();

        // Botón volver (abajo)
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Botón confirmar venta
        Button btnFacturar = new Button(this);
        btnFacturar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btnFacturar.setText("Confirmar Venta");
        btnFacturar.setBackgroundColor(Color.parseColor("#3F51B5"));
        btnFacturar.setTextColor(Color.WHITE);
        btnFacturar.setOnClickListener(v -> confirmarVenta());

        // Agregar botón al layout
        LinearLayout containerBotones = findViewById(R.id.containerBotones);
        containerBotones.addView(btnFacturar);
    }

    private void mostrarInfoCliente() {
        TextView tvCliente = findViewById(R.id.tvCliente);
        TextView tvDireccion = findViewById(R.id.tvDireccion);
        TextView tvInfoUrgencia = findViewById(R.id.tvInfoUrgencia);

        tvCliente.setText("Cliente: " + pedido.getCliente().getNombre());
        tvDireccion.setText("Dirección: " + pedido.getCliente().getDireccion());

        String urgencia = pedido.getCliente().getUrgencia();
        if (urgencia != null && !urgencia.isEmpty()) {
            tvInfoUrgencia.setVisibility(View.VISIBLE);
            tvInfoUrgencia.setTextColor(Color.RED);
            tvInfoUrgencia.setText("\u00a1URGENTE! (Hora de cierre: " + urgencia + ")");
        } else {
            tvInfoUrgencia.setVisibility(View.GONE);
        }
    }

    private void mostrarProductos() {
        LinearLayout containerProductos = findViewById(R.id.containerProductos);
        double total = 0;

        for (String p : pedido.getProductos()) {
            String[] partes = p.split("_");
            if (partes.length != 2) continue;

            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);

            Producto producto = Producto.buscarPorCodigo(codigo);

            if (producto != null) {
                double subtotal = producto.getPrecio() * cant;
                total += subtotal;

                TextView tvProducto = new TextView(this);
                tvProducto.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                tvProducto.setText(String.format(Locale.getDefault(),
                        "• %s (Cantidad: %d)", producto.getNombre(), cant));
                tvProducto.setTextSize(16);
                tvProducto.setTextColor(Color.parseColor("#333333"));
                tvProducto.setPadding(8, 12, 8, 12);

                View separator = new View(this);
                separator.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));
                separator.setBackgroundColor(Color.parseColor("#E0E0E0"));

                containerProductos.addView(tvProducto);
                containerProductos.addView(separator);
            }
        }
    }

    private void confirmarVenta() {
        // Buscar el pedido real en la lista y confirmar ese
        for (Pedido p : Pedido.pedidos) {
            if (p == pedido || pedidosIguales(p, pedido)) {
                p.confirmar();
                break;
            }
        }

        Pedido.guardarPed(this);  // Guardar los cambios en el archivo

        new AlertDialog.Builder(this)
                .setTitle("Venta confirmada")
                .setMessage("¿Qué deseas hacer ahora?")
                .setPositiveButton("Generar Factura", (dialog, which) -> {
                    Intent intent = new Intent(this, FacturaActivity.class);
                    intent.putExtra("pedido", pedido);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Salir", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private boolean pedidosIguales(Pedido a, Pedido b) {
        return a.getCliente().getNombre().equals(b.getCliente().getNombre())
                && a.getFecha().equals(b.getFecha())
                && a.getProductos().equals(b.getProductos());
    }

}