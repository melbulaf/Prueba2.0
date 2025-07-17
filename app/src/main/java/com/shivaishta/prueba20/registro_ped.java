package com.shivaishta.prueba20;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class registro_ped extends AppCompatActivity {

    Button badd;
    Button bregped;
    Button bconfirm;
    Button bfecha;

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

        //Boton Agregar Productos
        badd = findViewById(R.id.btnAgregarProducto);
        badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProd();
            }
        });

        //Boton Fecha
        EditText edfecha = findViewById(R.id.editTextText4);
        Button fecha = findViewById(R.id.fechap);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH); // enero = 0
                int ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        registro_ped.this,
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


        //Boton volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        //Boton registrar
        bregped = findViewById(R.id.button7);
        bregped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtener cliente y fecha
                EditText nombrecliente = findViewById(R.id.editTextText3);
                String cliente = nombrecliente.getText().toString();
                Cliente clienteobj = null;
                for (Cliente c : Cliente.getClientes()) {
                    if (c.getNombre().equalsIgnoreCase(cliente)) {
                        clienteobj = c;
                        break;
                    }
                }

                EditText fechalayout = findViewById(R.id.editTextText4);
                String fecha = fechalayout.getText().toString();

                //obtener producto y cantidad
                List<String> nproductos = new ArrayList<String>();

                TableLayout tabla = findViewById(R.id.tablaProductos);
                for (int i = 0; i < tabla.getChildCount(); i++) {
                    View filaView = tabla.getChildAt(i);

                    if (filaView instanceof TableRow) {
                        TableRow fila = (TableRow) filaView;

                        TextView tvNombre = (TextView) fila.getChildAt(0); // Nombre
                        TextView tvCantidad = (TextView) fila.getChildAt(1); // Cantidad

                        String nombre = tvNombre.getText().toString();
                        int cantidad = Integer.parseInt(tvCantidad.getText().toString());

                        // buscar objeto producto
                        Producto productoEncontrado = null;
                        for (Producto p : Inventario.productos) {
                            if (p.getNombre().equalsIgnoreCase(nombre)) {
                                productoEncontrado = p;
                                break;
                            }
                        }

                        if (productoEncontrado != null) {

                            nproductos.add(Integer.toString(productoEncontrado.getCodigo()) + "_" + Integer.toString(cantidad));

                        }
                    }
                }

                    if (!nproductos.isEmpty() && clienteobj != null && !fecha.isEmpty()) {
                        //Registrar objeto pedido
                        new Pedido(clienteobj, nproductos, fecha);
                        //Guardar Pedidos
                        Pedido.guardarPed(registro_ped.this);
                        //Notificacion
                        android.widget.Toast.makeText(registro_ped.this,
                                "Pedido Registrado Exitosamente.",
                                android.widget.Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();

                } else {
                    android.widget.Toast.makeText(registro_ped.this,
                            "Por favor llena todos los campos.",
                            android.widget.Toast.LENGTH_LONG).show();
                }

            }
        });

        //Boton confirmar
        bconfirm = findViewById(R.id.confirm);
        bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtener cliente y fecha
                EditText nombrecliente = findViewById(R.id.editTextText3);
                String cliente = nombrecliente.getText().toString();
                Cliente clienteobj = null;
                for (Cliente c : Cliente.getClientes()) {
                    if (c.getNombre().equalsIgnoreCase(cliente)) {
                        clienteobj = c;
                        break;
                    }
                }

                EditText fechalayout = findViewById(R.id.editTextText4);
                String fecha = fechalayout.getText().toString();

                //obtener producto y cantidad
                List<String> nproductos = new ArrayList<String>();

                TableLayout tabla = findViewById(R.id.tablaProductos);
                for (int i = 0; i < tabla.getChildCount(); i++) {
                    View filaView = tabla.getChildAt(i);

                    if (filaView instanceof TableRow) {
                        TableRow fila = (TableRow) filaView;

                        TextView tvNombre = (TextView) fila.getChildAt(0); // Nombre
                        TextView tvCantidad = (TextView) fila.getChildAt(1); // Cantidad

                        String nombre = tvNombre.getText().toString();
                        int cantidad = Integer.parseInt(tvCantidad.getText().toString());

                        // buscar objeto producto
                        Producto productoEncontrado = null;
                        for (Producto p : Inventario.productos) {
                            if (p.getNombre().equalsIgnoreCase(nombre)) {
                                productoEncontrado = p;
                                break;
                            }
                        }

                        if (productoEncontrado != null) {

                            nproductos.add(Integer.toString(productoEncontrado.getCodigo()) + "_" + Integer.toString(cantidad));

                        }
                    }
                }

                if (!nproductos.isEmpty() && clienteobj != null && !fecha.isEmpty()) {
                    //Registrar objeto pedido
                    Pedido npedido = new Pedido(clienteobj, nproductos, fecha, true);
                    //Actualizar Productos
                    npedido.confirmar();
                    //Guardar pedidos
                    Pedido.guardarPed(registro_ped.this);
                    //toast para notificar al usuario
                    android.widget.Toast.makeText(registro_ped.this,
                            "Venta Confirmada Exitosamente.",
                            android.widget.Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    android.widget.Toast.makeText(registro_ped.this,
                            "Por favor llena todos los campos.",
                            android.widget.Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    void showDialogProd() {
        final Dialog dialog = new Dialog(registro_ped.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addproductodialog);

        //Initializing the views of the dialog.
        final EditText idnombre = dialog.findViewById(R.id.idnombreventa);
        final EditText cantidad = dialog.findViewById(R.id.cantidadventa);
        Button agregar = dialog.findViewById(R.id.addvent);
        Button cancelar = dialog.findViewById(R.id.cancelarvent);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idnombre.getText().toString().isEmpty() || cantidad.getText().toString().isEmpty()) {
                    android.widget.Toast.makeText(registro_ped.this,
                            "Por favor llena todos los campos.",
                            android.widget.Toast.LENGTH_LONG).show();
                }
                else {
                    String nombreCodigo = idnombre.getText().toString();
                    int cant;
                    try {
                        cant = Integer.parseInt(cantidad.getText().toString());
                        if (cant <= 0) {
                            android.widget.Toast.makeText(registro_ped.this,
                                    "La cantidad debe ser mayor que cero.",
                                    android.widget.Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        android.widget.Toast.makeText(registro_ped.this,
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

                        // Tabla Productos
                        TableLayout tabla = findViewById(R.id.tablaProductos);
                        TableRow fila = new TableRow(registro_ped.this);
                        fila.setPadding(0, 8, 0, 8);

                        TextView tvNombre = new TextView(registro_ped.this);
                        tvNombre.setText(encontrado.getNombre());
                        tvNombre.setTextAppearance(registro_ped.this, R.style.TableCellText);
                        TextView tvCantidad = new TextView(registro_ped.this);
                        tvCantidad.setText(String.valueOf(cant));
                        tvCantidad.setTextAppearance(registro_ped.this, R.style.TableCellText);

                        // Botón eliminar
                        Button btnEliminar = new Button(registro_ped.this);
                        btnEliminar.setText("X");
                        btnEliminar.setBackgroundResource(R.drawable.button_red_circle);
                        btnEliminar.setTextColor(Color.WHITE);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(
                                dpToPx(40),  // ancho
                                dpToPx(40)); // alto
                        btnEliminar.setLayoutParams(params);
                        btnEliminar.setTextSize(12f);
                        btnEliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tabla.removeView(fila);
                            }
                        });

                        // Agregar vistas a la fila
                        fila.addView(tvNombre);
                        fila.addView(tvCantidad);
                        fila.addView(btnEliminar);

                        // Agregar fila a la tabla
                        tabla.addView(fila);

                        //Finalizar
                        android.widget.Toast.makeText(registro_ped.this,
                                "Producto Agregado exitosamente.",
                                android.widget.Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        android.widget.Toast.makeText(registro_ped.this,
                                "Producto no encontrado.",
                                android.widget.Toast.LENGTH_LONG).show();
                    }

                }

            }

        });
        dialog.show();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}