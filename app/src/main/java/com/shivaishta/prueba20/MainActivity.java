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

        //Crear Objetos de Prueba
        if (Pedido.pedidos.isEmpty() && Producto.productos.isEmpty() && Compra.compras.isEmpty() && Cliente.getClientes().isEmpty()) {
        objPrueba(); }
    }

        static void objPrueba() {
            Producto prod1 = new Producto("Galleta Cuca", "Galletas", 6500, 5000, 0);
            Producto prod2 = new Producto("Pan Tajado", "Panadería", 4500, 3500, 0);
            Producto prod3 = new Producto("Chocorramo", "Snacks", 2800, 2000, 0);
            Producto prod4 = new Producto("Jugo Hit", "Bebidas", 3700, 2900, 0);
            Producto prod5 = new Producto("Leche Alquería", "Lácteos", 5200, 4100, 0);
            Producto prod6 = new Producto("Arequipe Alpina", "Postres", 4600, 3700, 0);
            Producto prod7 = new Producto("Café Sello Rojo", "Bebidas", 8500, 7300, 0);
            Producto prod8 = new Producto("Margarina Rama", "Grasas", 6300, 4900, 0);
            Producto prod9 = new Producto("Atún Van Camp's", "Enlatados", 7200, 6100, 0);
            Producto prod10 = new Producto("Gaseosa Coca-Cola", "Bebidas", 5900, 4700, 0);
            Cliente cli1 = new Cliente("Juan Pérez", "3012345678", "Calle 12 # 34-56", "");
            Cliente cli2 = new Cliente("Ana Gómez", "3123456789", "Carrera 45 # 78-90", "02:00");
            Cliente cli3 = new Cliente("Carlos Ruiz", "3209876543", "Diagonal 67 # 89-01", "");
            Compra comp1 = new Compra(prod1, 5, "2025-07-01");
            Compra comp2 = new Compra(prod4, 3, "2025-07-03");
            Compra comp3 = new Compra(prod7, 2, "2025-07-05");
            ArrayList<String> productosP1 = new ArrayList<>();
            productosP1.add(prod4.getCodigo() + "_1");
            productosP1.add(prod7.getCodigo() + "_2");
            Pedido ped1 = new Pedido(cli1, productosP1, "17/07/2025", false);
            ArrayList<String> productosP2 = new ArrayList<>();
            productosP2.add(prod2.getCodigo() + "_3");
            productosP2.add(prod9.getCodigo() + "_1");
            Pedido ped2 = new Pedido(cli2, productosP2, "17/07/2025", false);
            ArrayList<String> productosP3 = new ArrayList<>();
            productosP3.add(prod10.getCodigo() + "_2");
            Pedido ped3 = new Pedido(cli3, productosP3, "17/07/2025", false);
        }
}