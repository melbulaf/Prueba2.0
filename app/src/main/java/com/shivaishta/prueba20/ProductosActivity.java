package com.shivaishta.prueba20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;
    private Button btnAgregar, btnEditar, btnEliminar, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnAgregar = findViewById(R.id.btnAgregarProducto);
        btnEditar = findViewById(R.id.btnEditarProducto);
        btnEliminar = findViewById(R.id.btnEliminarProducto);
        btnRegresar = findViewById(R.id.btnRegresarProducto);

        listaProductos = Producto.cargar(this);
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProductos.setAdapter(productoAdapter);

        btnAgregar.setOnClickListener(v -> mostrarDialogoProducto(null));

        btnEditar.setOnClickListener(v -> {
            int pos = productoAdapter.getSelectedPosition();
            if (pos >= 0) {
                Producto producto = listaProductos.get(pos);
                mostrarDialogoProducto(producto);
            } else {
                Toast.makeText(this, "Seleccione un producto para editar", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            int pos = productoAdapter.getSelectedPosition();
            if (pos >= 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Está seguro de eliminar este producto?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            listaProductos.remove(pos);
                            productoAdapter.notifyDataSetChanged();
                            Producto.guardar(listaProductos, this);
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(this, "Seleccione un producto para eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoProducto(Producto productoEditar) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogo_producto, null);

        EditText etNombre = dialogView.findViewById(R.id.edtNombre);
        EditText etPrecioC = dialogView.findViewById(R.id.edtPrecioCompra);
        EditText etPrecioV = dialogView.findViewById(R.id.edtPrecioVenta);
        EditText etCantidad = dialogView.findViewById(R.id.edtCantidad);
        EditText etCategoria = dialogView.findViewById(R.id.edtCategoria);
        EditText etCodigo = dialogView.findViewById(R.id.edtCodigo);
        Switch switchUrgente = dialogView.findViewById(R.id.switchUrgente);

        if (productoEditar != null) {
            etNombre.setText(productoEditar.getNombre());
            etPrecioC.setText(String.valueOf(productoEditar.getPrecioC()));
            etPrecioV.setText(String.valueOf(productoEditar.getPrecioV()));
            etCantidad.setText(String.valueOf(productoEditar.getCantidad()));
            etCategoria.setText(productoEditar.getCategoria());
            etCodigo.setText(String.valueOf(productoEditar.getCodigo()));
            etCodigo.setEnabled(false); // No permitir cambiar el código al editar
            switchUrgente.setChecked(productoEditar.isUrgente());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.btnGuardarProducto).setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String precioCStr = etPrecioC.getText().toString().trim();
            String precioVStr = etPrecioV.getText().toString().trim();
            String cantidadStr = etCantidad.getText().toString().trim();
            String categoria = etCategoria.getText().toString().trim();
            String codigoStr = etCodigo.getText().toString().trim();
            boolean urgente = switchUrgente.isChecked();

            if (nombre.isEmpty() || precioCStr.isEmpty() || precioVStr.isEmpty() ||
                    cantidadStr.isEmpty() || categoria.isEmpty() || codigoStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double precioC = Double.parseDouble(precioCStr);
                double precioV = Double.parseDouble(precioVStr);
                int cantidad = Integer.parseInt(cantidadStr);
                int codigo = Integer.parseInt(codigoStr);

                if (productoEditar == null) {
                    // Validar código duplicado
                    for (Producto p : listaProductos) {
                        if (p.getCodigo() == codigo) {
                            Toast.makeText(this, "Ya existe un producto con ese código", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Producto nuevo = new Producto(nombre, cantidad, precioV, precioC, categoria, codigo, urgente);
                    listaProductos.add(nuevo);
                } else {
                    // Editar producto existente
                    productoEditar.setNombre(nombre);
                    productoEditar.setCantidad(cantidad);
                    productoEditar.setPrecioC(precioC);
                    productoEditar.setPrecioV(precioV);
                    productoEditar.setCategoria(categoria);
                    productoEditar.setUrgente(urgente);
                }

                productoAdapter.notifyDataSetChanged();
                Producto.guardar(listaProductos, this);
                dialog.dismiss();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            }
        });

        dialogView.findViewById(R.id.btnCancelarProducto).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
