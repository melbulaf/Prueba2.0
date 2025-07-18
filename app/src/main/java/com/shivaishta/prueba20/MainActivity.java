package com.shivaishta.prueba20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button registrarPedido;


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

        CardView cardFacturacion = findViewById(R.id.cardFunc2);
        cardFacturacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear cliente de prueba
                Cliente cliente = new Cliente(
                        "Cliente Ejemplo",
                        "3106830641",
                        "Calle 123 #45-67",
                        "Urgente para mañana"
                );

                // Asegúrate de que haya al menos un producto en el inventario
                if (Inventario.productos.isEmpty()) {
                    Inventario.productos.add(new Producto("Galletas", 101, "Snacks", 10, 1000, 1500));
                    Inventario.productos.add(new Producto("Jugo", 102, "Bebidas", 5, 1200, 1800));
                }

                // Crear lista de productos en formato "codigo_cantidad"
                List<String> productos = new ArrayList<>();
                productos.add("101_2"); // 2 unidades del producto con código 101
                productos.add("102_1"); // 1 unidad del producto con código 102

                // Crear fecha actual
                String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Crear pedido con cliente, productos y fecha
                Pedido pedido = new Pedido(cliente, productos, fecha);
                pedido.confirmar();

                // Iniciar FacturaActivity
                Intent intent = new Intent(MainActivity.this, FacturaActivity.class);
                intent.putExtra("pedido", pedido);
                startActivity(intent);
            }
        });
        // Boton Productos
        CardView cardProductos = findViewById(R.id.cardProductos);
        cardProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
                startActivity(intent);
            }
        });

        //Boton Clientes
        CardView cardClientes = findViewById(R.id.cardFunc3);
        cardClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clientes - En desarrollo", Toast.LENGTH_SHORT).show();
            }
        });
        // Otros botones (mantienen Toast temporal)
        CardView cardPedidos = findViewById(R.id.cardFunc4);
        cardPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrarPedido.class);
                startActivity(intent);
            }
        });

        CardView cardConfig = findViewById(R.id.cardFunc5);
        cardConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Configuración - En desarrollo", Toast.LENGTH_SHORT).show();
            }
        });

        CardView cardCompras = findViewById(R.id.cardCompras);
        cardCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComprasActivity.class);
                startActivity(intent);
            }
        });
    }
}