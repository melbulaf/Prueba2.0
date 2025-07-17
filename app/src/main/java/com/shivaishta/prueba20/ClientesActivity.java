package com.shivaishta.prueba20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Cliente> clientes;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        listView = findViewById(R.id.listaClientes);
        btnAgregar = findViewById(R.id.btnAgregarCliente);
        clientes = Cliente.getClientes();

        actualizarLista();

        btnAgregar.setOnClickListener(v -> mostrarDialogoCliente(null));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cliente seleccionado = clientes.get(position);
            mostrarDialogoCliente(seleccionado);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Cliente cliente = clientes.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Deseas eliminar a " + cliente.getNombre() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        clientes.remove(cliente);
                        actualizarLista();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void actualizarLista() {
        ArrayList<String> nombres = new ArrayList<>();
        for (Cliente c : clientes) {
            nombres.add(c.getNombre() + " - " + c.getTelefono());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listView.setAdapter(adapter);
    }

    private void mostrarDialogoCliente(Cliente clienteEditar) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View vista = inflater.inflate(R.layout.dialogo_cliente, null);

        EditText txtNombre = vista.findViewById(R.id.dialogNombre);
        EditText txtTelefono = vista.findViewById(R.id.dialogTelefono);
        EditText txtDireccion = vista.findViewById(R.id.dialogDireccion);
        EditText txtUrgencia = vista.findViewById(R.id.dialogUrgencia);

        if (clienteEditar != null) {
            txtNombre.setText(clienteEditar.getNombre());
            txtTelefono.setText(clienteEditar.getTelefono());
            txtDireccion.setText(clienteEditar.getDireccion());
            txtUrgencia.setText(clienteEditar.getMotivoUrgencia());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(clienteEditar == null ? "Nuevo Cliente" : "Editar Cliente");
        builder.setView(vista);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = txtNombre.getText().toString().trim();
            String tel = txtTelefono.getText().toString().trim();
            String dir = txtDireccion.getText().toString().trim();
            String urg = txtUrgencia.getText().toString().trim();

            if (nombre.isEmpty() || tel.isEmpty() || dir.isEmpty()) {
                Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (clienteEditar != null) {
                clienteEditar.setNombre(nombre);
                clienteEditar.setTelefono(tel);
                clienteEditar.setDireccion(dir);
                clienteEditar.setMotivoUrgencia(urg);
            } else {
                new Cliente(nombre, tel, dir, urg);
            }
            actualizarLista();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
