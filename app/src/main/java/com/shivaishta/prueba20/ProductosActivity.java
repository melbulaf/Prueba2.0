package com.shivaishta.prueba20;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private ProductoAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_producto);

        Producto.cargarProductos(this);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoAdapter(Producto.getProductos());
        recyclerProductos.setAdapter(productoAdapter);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnAgregarProducto).setOnClickListener(v -> mostrarDialogoAgregar());

        findViewById(R.id.btnEditarProducto).setOnClickListener(v -> mostrarDialogoBuscarYEditar());

        findViewById(R.id.btnEliminarProducto).setOnClickListener(v -> mostrarDialogoEliminar());
    }

    private void mostrarDialogoAgregar() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialogo_agregar_producto, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        EditText etPrecioV = dialogView.findViewById(R.id.etPrecioVenta);
        EditText etPrecioC = dialogView.findViewById(R.id.etPrecioCompra);
        EditText etCategoria = dialogView.findViewById(R.id.etCategoria);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Producto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String categoria = etCategoria.getText().toString().trim();
                    int cantidad = Integer.parseInt(etCantidad.getText().toString().trim());
                    double precioV = Double.parseDouble(etPrecioV.getText().toString().trim());
                    double precioC = Double.parseDouble(etPrecioC.getText().toString().trim());

                    Producto.agregarProducto(new Producto(nombre, cantidad, precioV, precioC, categoria));
                    Producto.guardarProductos(this);
                    productoAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoBuscarYEditar() {
        EditText input = new EditText(this);
        input.setHint("Nombre del producto a editar");

        new AlertDialog.Builder(this)
                .setTitle("Editar Producto")
                .setView(input)
                .setPositiveButton("Buscar", (dialog, which) -> {
                    String nombreBuscar = input.getText().toString().trim();
                    Producto producto = buscarProductoPorNombre(nombreBuscar);
                    if (producto != null) {
                        mostrarDialogoEditar(producto);
                    } else {
                        Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditar(Producto producto) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialogo_agregar_producto, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        EditText etPrecioV = dialogView.findViewById(R.id.etPrecioVenta);
        EditText etPrecioC = dialogView.findViewById(R.id.etPrecioCompra);
        EditText etCategoria = dialogView.findViewById(R.id.etCategoria);

        etNombre.setText(producto.getNombre());
        etCantidad.setText(String.valueOf(producto.getCantidad()));
        etPrecioV.setText(String.valueOf(producto.getPrecioV()));
        etPrecioC.setText(String.valueOf(producto.getPrecioC()));
        etCategoria.setText(producto.getCategoria());

        new AlertDialog.Builder(this)
                .setTitle("Editar Producto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    producto.setNombre(etNombre.getText().toString().trim());
                    producto.setCantidad(Integer.parseInt(etCantidad.getText().toString().trim()));
                    producto.setPrecioV(Double.parseDouble(etPrecioV.getText().toString().trim()));
                    producto.setPrecioC(Double.parseDouble(etPrecioC.getText().toString().trim()));
                    producto.setCategoria(etCategoria.getText().toString().trim());

                    Producto.guardarProductos(this);
                    productoAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEliminar() {
        EditText input = new EditText(this);
        input.setHint("Nombre del producto a eliminar");

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setView(input)
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    String nombre = input.getText().toString().trim();
                    Producto producto = buscarProductoPorNombre(nombre);
                    if (producto != null) {
                        Producto.eliminarProducto(producto);
                        Producto.guardarProductos(this);
                        productoAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : Producto.getProductos()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
