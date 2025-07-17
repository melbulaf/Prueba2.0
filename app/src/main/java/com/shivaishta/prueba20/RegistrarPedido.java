package com.shivaishta.prueba20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RegistrarPedido extends AppCompatActivity {

    Button btnRegistrarPed;
    Button btnVentas;
    PedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_pedido);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //Configurar el adaptador del RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerPeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PedidosAdapter(this);
        recyclerView.setAdapter(adapter);

        //Boton Registrar Pedido
        btnRegistrarPed = findViewById(R.id.btpedidos);
        btnRegistrarPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarPedido.this, registro_ped.class);
                startActivityForResult(intent, 1);
            }
        });

        //Boton Ventas Confirmadas
        btnVentas = findViewById(R.id.btventas);
        btnVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarPedido.this, VentasActivity.class);
                startActivity(intent);
            }
        });

        //Boton volver
        ImageButton btnBack = findViewById(R.id.btnBackPeds);
        btnBack.setOnClickListener(v -> finish());

    }

    //Actualizaar despues de registrar pedidos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter = new PedidosAdapter(this);
            RecyclerView recyclerView = findViewById(R.id.recyclerPeds);
            recyclerView.setAdapter(adapter);
        }
    }

}