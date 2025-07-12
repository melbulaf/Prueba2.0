package com.shivaishta.prueba20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botón Ruta
        CardView cardRuta = findViewById(R.id.cardRuta);
        cardRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RutaActivity.class);
                startActivity(intent);
            }
        });

        // Botón Facturación
        CardView cardFacturacion = findViewById(R.id.cardFunc2);
        cardFacturacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear pedido de ejemplo con datos de prueba
                Cliente cliente = new Cliente(
                        "Cliente Ejemplo",
                        "3106830641",
                        "Calle 123 #45-67",
                        "Urgente para mañana"
                );

                List<Producto> productos = new ArrayList<>();
                productos.add(new Producto("Producto A", 2, 25000));
                productos.add(new Producto("Producto B", 1, 50000));

                String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                Pedido pedido = new Pedido(cliente, productos, fecha);

                // Abrir FacturaActivity con el pedido
                Intent intent = new Intent(MainActivity.this, FacturaActivity.class);
                intent.putExtra("pedido", pedido);
                startActivity(intent);
            }
        });

        // Otros botones (mantienen Toast temporal)
        CardView cardClientes = findViewById(R.id.cardFunc3);
        cardClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clientes - En desarrollo", Toast.LENGTH_SHORT).show();
            }
        });

        CardView cardReportes = findViewById(R.id.cardFunc4);
        cardReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Reportes - En desarrollo", Toast.LENGTH_SHORT).show();
            }
        });

        CardView cardConfig = findViewById(R.id.cardFunc5);
        cardConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Configuración - En desarrollo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}