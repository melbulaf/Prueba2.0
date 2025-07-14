package com.shivaishta.prueba20;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InventarioActivity extends AppCompatActivity{

    String criterio = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_inventario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainInventario), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBuscar = findViewById(R.id.btnBuscar);
        Button btnOrdenar = findViewById(R.id.btnOrdenar);
        EditText campoBuscar = findViewById(R.id.campoBuscar);
        TextView MensajeError = findViewById(R.id.labelError);
        TableLayout TablaProductos = findViewById(R.id.tablaProductos);
        Spinner spinner = findViewById(R.id.SpinnerOrdenar);

        //Boton volver
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Inventario InstanciaDeInventario = Inventario.getInstancia();

        if (InstanciaDeInventario.obtenerProductos().isEmpty()) {
            InstanciaDeInventario.agregarProducto(new Producto("pan", "panaderia", 2500, 2000, 2));
            InstanciaDeInventario.agregarProducto(new Producto("banano", "frutas", 700, 400, 3));
            InstanciaDeInventario.agregarProducto(new Producto("queso", "lacteos", 7000, 5000, 1));
            InstanciaDeInventario.agregarProducto(new Producto("leche", "lacteos", 4500, 3500, 2));
            InstanciaDeInventario.agregarProducto(new Producto("manzana", "frutas", 1200, 800, 5));
            InstanciaDeInventario.agregarProducto(new Producto("tomate", "verduras", 900, 600, 6));
            InstanciaDeInventario.agregarProducto(new Producto("arroz", "granos", 3000, 2500, 4));
            InstanciaDeInventario.agregarProducto(new Producto("harina", "granos", 2800, 2300, 3));
            InstanciaDeInventario.agregarProducto(new Producto("mantequilla", "lacteos", 5500, 4500, 1));
            InstanciaDeInventario.agregarProducto(new Producto("pollo", "carnes", 10000, 8000, 3));
            InstanciaDeInventario.agregarProducto(new Producto("carne molida", "carnes", 12000, 9500, 2));
            InstanciaDeInventario.agregarProducto(new Producto("yogur", "lacteos", 3200, 2500, 5));
            InstanciaDeInventario.agregarProducto(new Producto("café", "bebidas", 8500, 7000, 2));
            InstanciaDeInventario.agregarProducto(new Producto("jugo de naranja", "bebidas", 4000, 3000, 3));
            InstanciaDeInventario.agregarProducto(new Producto("huevos", "lacteos", 6000, 5000, 6));
            InstanciaDeInventario.agregarProducto(new Producto("galletas", "panaderia", 2500, 2000, 3));
            InstanciaDeInventario.agregarProducto(new Producto("azúcar", "granos", 2700, 2200, 4));
            InstanciaDeInventario.agregarProducto(new Producto("sal", "granos", 1000, 700, 3));
            InstanciaDeInventario.agregarProducto(new Producto("zanahoria", "verduras", 800, 500, 5));
            InstanciaDeInventario.agregarProducto(new Producto("cebolla", "verduras", 1100, 750, 6));
        }

        ArrayList<String> filtros = new ArrayList<>();
        filtros.add("Cantidad");
        filtros.add("Categoría");
        filtros.add("Código");
        filtros.add("Nombre");
        filtros.add("Precio");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                filtros
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                criterio = filtros.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnOrdenar.setOnClickListener(v -> {
            ArrayList<Producto> productosSorted = InstanciaDeInventario.obtenerProductos();

            switch (criterio) {
                case "Código":
                    productosSorted.sort((a, b) -> Integer.compare(a.getCodigo(), b.getCodigo()));
                    break;
                case "Nombre":
                    productosSorted.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
                    break;
                case "Categoría":
                    productosSorted.sort((a, b) -> a.getCategoria().compareToIgnoreCase(b.getCategoria()));
                    break;
                case "Precio":
                    productosSorted.sort((a, b) -> Double.compare(a.getPrecio(), b.getPrecio()));
                    break;
                case "Cantidad":
                    productosSorted.sort((a, b) -> Integer.compare(a.getCantidad(), b.getCantidad()));
                    break;
                default:
                    break;
            }

            int totalFilas = TablaProductos.getChildCount();
            for (int i = totalFilas-1; i >= 0; i--) {
                TablaProductos.removeViewAt(i);
            }
            for (Producto p : productosSorted) {
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        WRAP_CONTENT));

                TextView tvCodigo = new TextView(this);
                tvCodigo.setText(String.valueOf(p.getCodigo()));
                tvCodigo.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCodigo.setPadding(1, 4, 1, 4);
                tvCodigo.setTextColor(Color.BLACK);
                tvCodigo.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCodigo.setGravity(Gravity.CENTER);
                tvCodigo.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.12f));

                TextView tvNombre = new TextView(this);
                tvNombre.setText(String.valueOf(p.getNombre()));
                tvNombre.setBackgroundResource(R.drawable.casilla_simple_background);
                tvNombre.setPadding(1, 4, 1, 4);
                tvNombre.setTextColor(Color.BLACK);
                tvNombre.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvNombre.setGravity(Gravity.CENTER);
                tvNombre.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.21f));

                TextView tvCategoria = new TextView(this);
                tvCategoria.setText(String.valueOf(p.getCategoria()));
                tvCategoria.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCategoria.setPadding(1, 4, 1, 4);
                tvCategoria.setTextColor(Color.BLACK);
                tvCategoria.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCategoria.setGravity(Gravity.CENTER);
                tvCategoria.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.2f));

                TextView tvPrecio = new TextView(this);
                tvPrecio.setText(String.valueOf(p.getPrecio()));
                tvPrecio.setBackgroundResource(R.drawable.casilla_simple_background);
                tvPrecio.setPadding(3, 4, 3, 4);
                tvPrecio.setTextColor(Color.BLACK);
                tvPrecio.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvPrecio.setGravity(Gravity.CENTER);
                tvPrecio.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.15f));

                TextView tvPrecioC = new TextView(this);
                tvPrecioC.setText(String.valueOf(p.getPrecioC()));
                tvPrecioC.setBackgroundResource(R.drawable.casilla_simple_background);
                tvPrecioC.setPadding(3, 4, 3, 4);
                tvPrecioC.setTextColor(Color.BLACK);
                tvPrecioC.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvPrecioC.setGravity(Gravity.CENTER);
                tvPrecioC.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.14f));

                TextView tvCantidad = new TextView(this);
                tvCantidad.setText(String.valueOf(p.getCantidad()));
                tvCantidad.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCantidad.setPadding(3, 4, 3, 4);
                tvCantidad.setTextColor(Color.BLACK);
                tvCantidad.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCantidad.setGravity(Gravity.CENTER);
                tvCantidad.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.18f));

                fila.addView(tvCodigo);
                fila.addView(tvNombre);
                fila.addView(tvCategoria);
                fila.addView(tvPrecio);
                fila.addView(tvPrecioC);
                fila.addView(tvCantidad);
                TablaProductos.addView(fila);
            }

        });

        btnBuscar.setOnClickListener(v -> {
            String NombreCodigo = campoBuscar.getText().toString();
            Producto encontrado = null;
            try {
                int codigoBuscado = Integer.parseInt(NombreCodigo); // evaluar si es código
                for (Producto p : InstanciaDeInventario.obtenerProductos()) {
                    if (p.getCodigo() == codigoBuscado) {
                        encontrado = p;
                        break;
                    }
                }
            } catch (NumberFormatException e) { // si no es número, buscar por nombre
                for (Producto p : InstanciaDeInventario.obtenerProductos()) {
                    if (p.getNombre().equalsIgnoreCase(NombreCodigo)) {
                        encontrado = p;
                        break;
                    }
                }
            }

            if (encontrado != null) {
                MensajeError.setVisibility(View.INVISIBLE);
                //DefaultTableModel modelo = (DefaultTableModel) TablaProductos.getModel();
                int totalFilas = TablaProductos.getChildCount();
                for (int i = totalFilas-1; i >= 0; i--) {
                    TablaProductos.removeViewAt(i);
                }
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        WRAP_CONTENT));

                TextView tvCodigo = new TextView(this);
                tvCodigo.setText(String.valueOf(encontrado.getCodigo()));
                tvCodigo.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCodigo.setPadding(1, 4, 1, 4);
                tvCodigo.setTextColor(Color.BLACK);
                tvCodigo.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCodigo.setGravity(Gravity.CENTER);
                tvCodigo.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.12f));

                TextView tvNombre = new TextView(this);
                tvNombre.setText(String.valueOf(encontrado.getNombre()));
                tvNombre.setBackgroundResource(R.drawable.casilla_simple_background);
                tvNombre.setPadding(1, 4, 1, 4);
                tvNombre.setTextColor(Color.BLACK);
                tvNombre.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvNombre.setGravity(Gravity.CENTER);
                tvNombre.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.21f));

                TextView tvCategoria = new TextView(this);
                tvCategoria.setText(String.valueOf(encontrado.getCategoria()));
                tvCategoria.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCategoria.setPadding(1, 4, 1, 4);
                tvCategoria.setTextColor(Color.BLACK);
                tvCategoria.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCategoria.setGravity(Gravity.CENTER);
                tvCategoria.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.2f));

                TextView tvPrecio = new TextView(this);
                tvPrecio.setText(String.valueOf(encontrado.getPrecio()));
                tvPrecio.setBackgroundResource(R.drawable.casilla_simple_background);
                tvPrecio.setPadding(3, 4, 3, 4);
                tvPrecio.setTextColor(Color.BLACK);
                tvPrecio.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvPrecio.setGravity(Gravity.CENTER);
                tvPrecio.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.15f));

                TextView tvPrecioC = new TextView(this);
                tvPrecioC.setText(String.valueOf(encontrado.getPrecioC()));
                tvPrecioC.setBackgroundResource(R.drawable.casilla_simple_background);
                tvPrecioC.setPadding(3, 4, 3, 4);
                tvPrecioC.setTextColor(Color.BLACK);
                tvPrecioC.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvPrecioC.setGravity(Gravity.CENTER);
                tvPrecioC.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.14f));

                TextView tvCantidad = new TextView(this);
                tvCantidad.setText(String.valueOf(encontrado.getCantidad()));
                tvCantidad.setBackgroundResource(R.drawable.casilla_simple_background);
                tvCantidad.setPadding(3, 4, 3, 4);
                tvCantidad.setTextColor(Color.BLACK);
                tvCantidad.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tvCantidad.setGravity(Gravity.CENTER);
                tvCantidad.setLayoutParams(new TableRow.LayoutParams(0, MATCH_PARENT, 0.18f));

                fila.addView(tvCodigo);
                fila.addView(tvNombre);
                fila.addView(tvCategoria);
                fila.addView(tvPrecio);
                fila.addView(tvPrecioC);
                fila.addView(tvCantidad);
                TablaProductos.addView(fila);
            } else {
                MensajeError.setVisibility(View.VISIBLE);
                //MensajeError.setText("Producto no registrado en el inventario");
            }
        });
    }

}
