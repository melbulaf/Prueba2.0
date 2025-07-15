package com.shivaishta.prueba20;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class registro_ped extends AppCompatActivity {

    Button bnueva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_ped);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Boton agregar producto
        TableLayout tabla = findViewById(R.id.tablaProductos);

        TableRow fila = new TableRow(this);
        TextView producto = new TextView(this);
        producto.setText("Producto A");
        TextView cantidad = new TextView(this);
        cantidad.setText("3");

        // Botón eliminar
        Button btnEliminar = new Button(this);
        btnEliminar.setText("X");
        btnEliminar.setOnClickListener(v -> tabla.removeView(fila));

        // Agregar a la fila
        fila.addView(producto);
        fila.addView(cantidad);
        fila.addView(btnEliminar);

        // Agregar a la tabla
        tabla.addView(fila);


    }

}