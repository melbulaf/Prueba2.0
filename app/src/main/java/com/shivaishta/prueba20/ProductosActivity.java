package com.shivaishta.prueba20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnAgregarProducto;
    private ArrayAdapter<Producto> adapter;
    private ArrayList<Producto> productos = Producto.productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        listView = findViewById(R.id.listViewProductos);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productos);
        listView.setAdapter(adapter);

        // Agregar nuevo producto
        btnAgregarProducto.setOnClickListener(view -> mostrarDialogoProducto(null));

        // Editar producto
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Producto producto = productos.get(position);
            mostrarDialogoProducto(producto);
        });

        // Eliminar producto
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Producto producto = productos.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Deseas eliminar el producto " + producto.getNombre() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        productos.remove(producto);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void mostrarDialogoProducto(Producto productoExistente) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogo_producto, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombreProducto);
        EditText etCantidad = dialogView.findViewById(R.id.etCantidadProducto);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecioProducto);
        EditText etPrecioC = dialogView.findViewById(R.id.etPrecioCompraProducto);
        EditText etCategoria = dialogView.findViewById(R.id.etCategoriaProducto);

        if (productoExistente != null) {
            etNombre.setText(productoExistente.getNombre());
            etCantidad.setText(String.valueOf(productoExistente.getCantidad()));
            etPrecio.setText(String.valueOf(productoExistente.getPrecio()));
            etPrecioC.setText(String.valueOf(productoExistente.getPrecioC()));
            etCategoria.setText(productoExistente.getCategoria());
        }

        new AlertDialog.Builder(this)
                .setTitle(productoExistente == null ? "Agregar producto" : "Editar producto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString();
                    int cantidad = Integer.parseInt(etCantidad.getText().toString());
                    double precio = Double.parseDouble(etPrecio.getText().toString());
                    double precioC = Double.parseDouble(etPrecioC.getText().toString());
                    String categoria = etCategoria.getText().toString();

                    if (productoExistente == null) {
                        new Producto(nombre, categoria, precio, precioC, cantidad);
                        Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                    } else {
                        productoExistente.setNombre(nombre);
                        productoExistente.setCantidad(cantidad);
                        productoExistente.setPrecio(precio);
                        productoExistente.setPrecioC(precioC);
                        productoExistente.setCategoria(categoria);
                        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
