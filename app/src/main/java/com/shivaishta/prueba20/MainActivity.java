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

        // Botón Inventario
        CardView cardInventario = findViewById(R.id.cardInventario);
        cardInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar InventarioActivity
                Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
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

        // Botón Clientes
        CardView cardClientes = findViewById(R.id.cardFunc3);
        cardClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);
            }
        });

        // Botón Pedidos
        CardView cardPedidos = findViewById(R.id.cardFunc4);
        cardPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrarPedido.class);
                startActivity(intent);
            }
        });

        // Botón Compras
        CardView cardCompras = findViewById(R.id.cardCompras);
        cardCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComprasActivity.class);
                startActivity(intent);
            }
        });

        // Botón Ruta
        CardView cardRuta = findViewById(R.id.cardRuta);
        cardRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RutaActivity.class);
                startActivity(intent);
            }
        });

        Pedido.cargarPed(this);
        Compra.cargarC(this);
        Cliente.cargarClientes(this);

    }
    
}