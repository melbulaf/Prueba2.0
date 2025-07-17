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

public class ComprasActivity extends AppCompatActivity {

    Button bfiltros,bnueva;
    private int dia;
    private int mes;
    private int ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compras);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Compra.cargarC(this);
        //configura recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCompras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //obtener y adaptar lista
        List<Compra> lista = Compra.compras;
        CompraAdapter adapter = new CompraAdapter(lista);
        recyclerView.setAdapter(adapter);

        //Boton filtros
        bfiltros = findViewById(R.id.filtrar);
        bfiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFiltros();
            }
        });

        //Boton Nueva Compra
        bnueva = findViewById(R.id.addcompra);
        bnueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCompra();
            }
        });

        //Boton volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

    }

    //Dialogo de Filtros
    void showDialogFiltros() {
        final Dialog dialog = new Dialog(ComprasActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filtrosdialog);

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
                        ComprasActivity.this,
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

                List<Compra> listaFiltrada = new java.util.ArrayList<>();
                boolean seEncontraronCoincidencias = false;

                for (int i = 0; i < Compra.compras.size(); i++) {
                    Compra c = Compra.compras.get(i);

                    boolean coincideFecha = fechaFiltro.isEmpty() || c.fecha.equals(fechaFiltro);
                    boolean coincideTexto = true;

                    if (!textoFiltro.isEmpty()) {
                        try {
                            int codigo = Integer.parseInt(textoFiltro);
                            coincideTexto = c.producto.getCodigo() == codigo;
                        } catch (NumberFormatException e) {
                            coincideTexto = c.producto.getNombre().toLowerCase().contains(textoFiltro);
                        }
                    }

                    if (coincideFecha && coincideTexto) {
                        listaFiltrada.add(c);
                        seEncontraronCoincidencias = true;
                    }
                }

                if (!seEncontraronCoincidencias) {
                    android.widget.Toast.makeText(ComprasActivity.this,
                            "Producto no encontrado. Verifica el nombre, código o fecha.",
                            android.widget.Toast.LENGTH_LONG).show();
                }

                // actualizar RecyclerView
                if (seEncontraronCoincidencias) {
                    CompraAdapter adapter = new CompraAdapter(listaFiltrada);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewCompras);
                    recyclerView.setAdapter(adapter);
                }

                dialog.dismiss();
            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idnombre.setText("");
                edfecha.setText("");

                // restaurar RecyclerView
                List<Compra> listaOriginal = new java.util.ArrayList<>(Compra.compras);
                CompraAdapter adapter = new CompraAdapter(listaOriginal);
                RecyclerView recyclerView = findViewById(R.id.recyclerViewCompras);
                recyclerView.setAdapter(adapter);

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    void showDialogCompra() {
        final Dialog dialog = new Dialog(ComprasActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.ncompradialog);

        //Initializing the views of the dialog.
        final EditText idnombre = dialog.findViewById(R.id.idnombrecompra);
        final EditText cantidad = dialog.findViewById(R.id.cantidad);
        Button registrar = dialog.findViewById(R.id.registrar);
        Button cancelar = dialog.findViewById(R.id.cancelar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idnombre.getText().toString().isEmpty() || cantidad.getText().toString().isEmpty()) {
                    android.widget.Toast.makeText(ComprasActivity.this,
                            "Por favor llena todos los campos.",
                            android.widget.Toast.LENGTH_LONG).show();
                }
                else {
                    String nombreCodigo = idnombre.getText().toString();
                    int cant;
                    try {
                        cant = Integer.parseInt(cantidad.getText().toString());
                        if (cant <= 0) {
                            android.widget.Toast.makeText(ComprasActivity.this,
                                    "La cantidad debe ser mayor que cero.",
                                    android.widget.Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        android.widget.Toast.makeText(ComprasActivity.this,
                                "La cantidad debe ser un numero entero valido.",
                                android.widget.Toast.LENGTH_LONG).show();
                        return;
                    }
                    Producto encontrado = null;
                    try {
                        int codigoBuscado = Integer.parseInt(nombreCodigo); // evaluar si es código
                        for (Producto p : Inventario.productos) {
                            if (p.getCodigo() == codigoBuscado) {
                                encontrado = p;
                                break;
                            }
                        }
                    } catch (NumberFormatException e) { // si no es número, buscar por nombre
                        for (Producto p : Inventario.productos) {
                            if (p.getNombre().equalsIgnoreCase(nombreCodigo)) {
                                encontrado = p;
                                break;
                            }
                        }
                    }

                    if (encontrado != null){
                        encontrado.setCantidad(encontrado.getCantidad() + cant);
                        TextView precioUnd = dialog.findViewById(R.id.pund);
                        precioUnd.setText(Double.toString(encontrado.getPrecioC()));
                        TextView total = dialog.findViewById(R.id.ptotal);
                        total.setText(Double.toString(encontrado.getPrecioC()*cant));
                        // Registrar en lista de compras
                        Compra nueva = new Compra(encontrado, cant);

                        // Actualizar tabla del historial
                        List<Compra> listaOriginal = new java.util.ArrayList<>(Compra.compras);
                        CompraAdapter adapter = new CompraAdapter(listaOriginal);
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewCompras);
                        recyclerView.setAdapter(adapter);
                        //guardar compra
                        Compra.guardarC(ComprasActivity.this);
                        //Llamar al metodo de guardarProductos();
                        android.widget.Toast.makeText(ComprasActivity.this,
                                "Compra registrada exitosamente.",
                                android.widget.Toast.LENGTH_LONG).show();
                    } else {
                        android.widget.Toast.makeText(ComprasActivity.this,
                                "Producto no encontrado.",
                                android.widget.Toast.LENGTH_LONG).show();
                    }

                }

            }

        });
        dialog.show();
    }
}