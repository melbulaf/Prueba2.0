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
        Inventario.cargarProductos(this);
        Pedido.cargarPed(this);
        Cliente.cargarClientes(this);



        // Botón Ruta
        CardView cardRuta = findViewById(R.id.cardRuta);
        cardRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RutaActivity.class);
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
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);

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

        CardView cardConfig = findViewById(R.id.cardInventario);
        cardConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
                startActivity(intent);
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