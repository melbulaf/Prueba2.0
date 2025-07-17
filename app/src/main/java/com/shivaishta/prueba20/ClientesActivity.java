package com.shivaishta.prueba20;

import android.content.DialogInterface;
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

public class ClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerClientes;
    private ClienteAdapter clienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        // Cargar los clientes desde almacenamiento
        Cliente.cargarClientes(this);

        recyclerClientes = findViewById(R.id.recyclerClientes);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        clienteAdapter = new ClienteAdapter(Cliente.getClientes());
        recyclerClientes.setAdapter(clienteAdapter);

        // Botón volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Botón Agregar
        findViewById(R.id.btnAgregarCliente).setOnClickListener(v -> mostrarDialogoCliente(null));

        // Botón Editar
        findViewById(R.id.btnEditarCliente).setOnClickListener(v -> {
            List<Cliente> lista = Cliente.getClientes();
            if (!lista.isEmpty()) {
                mostrarDialogoCliente(lista.get(0)); // Editar el primer cliente como ejemplo
            } else {
                Toast.makeText(this, "No hay clientes para editar", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón Eliminar
        findViewById(R.id.btnEliminarCliente).setOnClickListener(v -> {
            List<Cliente> lista = Cliente.getClientes();
            if (!lista.isEmpty()) {
                Cliente cliente = lista.get(0); // Eliminar el primero como ejemplo
                Cliente.eliminarCliente(cliente);
                clienteAdapter.notifyDataSetChanged();
                Cliente.guardarClientes(this);
                Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay clientes para eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCliente(Cliente clienteEditar) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialogo_cliente, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etTelefono = dialogView.findViewById(R.id.etTelefono);
        EditText etDireccion = dialogView.findViewById(R.id.etDireccion);
        EditText etUrgencia = dialogView.findViewById(R.id.etUrgencia);

        if (clienteEditar != null) {
            etNombre.setText(clienteEditar.getNombre());
            etTelefono.setText(clienteEditar.getTelefono());
            etDireccion.setText(clienteEditar.getDireccion());
            etUrgencia.setText(clienteEditar.getUrgencia());
        }

        new AlertDialog.Builder(this)
                .setTitle(clienteEditar == null ? "Agregar Cliente" : "Editar Cliente")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String telefono = etTelefono.getText().toString().trim();
                    String direccion = etDireccion.getText().toString().trim();
                    String urgencia = etUrgencia.getText().toString().trim();

                    if (nombre.isEmpty() || telefono.isEmpty()) {
                        Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (clienteEditar != null) {
                        clienteEditar.setNombre(nombre);
                        clienteEditar.setTelefono(telefono);
                        clienteEditar.setDireccion(direccion);
                        clienteEditar.setUrgencia(urgencia);
                    } else {
                        Cliente.agregarCliente(new Cliente(nombre, telefono, direccion, urgencia));
                    }

                    Cliente.guardarClientes(this);
                    clienteAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
