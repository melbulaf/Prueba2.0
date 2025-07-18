package com.shivaishta.prueba20;

import android.os.Bundle;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;

import java.util.List;

public class VentasActivity extends AppCompatActivity {

    Button bfiltros;
    private int dia;
    private int mes;
    private int ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //configura recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerViewVentas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //llamar al adaptador
        VentaAdapter adapter = new VentaAdapter(this);
        recyclerView.setAdapter(adapter);

        //Boton volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        //Boton filtros
        bfiltros = findViewById(R.id.filtrarventa);
        bfiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFiltros();
            }
        });
    }

//dialog filtros
    void showDialogFiltros() {
        final Dialog dialog = new Dialog(VentasActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewVentas);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filtrosvdialog);

        //Initializing the views of the dialog.
        final EditText idnombre = dialog.findViewById(R.id.idnombrefiltros);
        final EditText edfecha = dialog.findViewById(R.id.etfecha);
        Button fecha = dialog.findViewById(R.id.fecha);
        Button filtrar = dialog.findViewById(R.id.filtrar);
        Button limpiar = dialog.findViewById(R.id.limpiar);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH); // enero = 0
                int ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        VentasActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Sumar 1 al mes (porque enero es 0)
                                String fechaFormateada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                edfecha.setText(fechaFormateada);
                            }
                        },
                        ano, mes, dia
                );

                datePickerDialog.show();
            }
        });

        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoFiltro = idnombre.getText().toString().trim().toLowerCase();
                String fechaFiltro = edfecha.getText().toString().trim();

                List<Pedido> listaFiltrada = new java.util.ArrayList<>();
                boolean seEncontraronCoincidencias = false;

                for (Pedido p : Pedido.pedidos) {
                    if (!p.getConfirmado()) continue;

                    boolean coincideFecha = fechaFiltro.isEmpty() || p.getFecha().equals(fechaFiltro);
                    boolean coincideTexto = true;

                    if (!textoFiltro.isEmpty()) {
                        coincideTexto = p.getCliente().getNombre().toLowerCase().contains(textoFiltro);
                    }

                    if (coincideFecha && coincideTexto) {
                        listaFiltrada.add(p);
                        seEncontraronCoincidencias = true;
                    }
                }

                if (!seEncontraronCoincidencias) {
                    android.widget.Toast.makeText(VentasActivity.this,
                            "Cliente no encontrado. Verifica el nombre o la fecha.",
                            android.widget.Toast.LENGTH_LONG).show();
                }
                VentaAdapter adapter = new VentaAdapter(VentasActivity.this, listaFiltrada);
                recyclerView.setAdapter(adapter);

                dialog.dismiss();
            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idnombre.setText("");
                edfecha.setText("");
                VentaAdapter adapter = new VentaAdapter(VentasActivity.this);
                recyclerView.setAdapter(adapter);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}