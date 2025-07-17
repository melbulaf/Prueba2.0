package com.shivaishta.prueba20;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> {

    private List<Pedido> ventas;
    private Context context;

    public VentaAdapter(Context context, List<Pedido> listaFiltrada) {
        this.context = context;
        this.ventas = listaFiltrada;
    }

    public VentaAdapter(Context context) {
        this.context = context;
        this.ventas = new ArrayList<>();
        for (Pedido p : Pedido.pedidos) {
            if (p.getConfirmado()) {
                ventas.add(p);
            }
        }
    }


    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_venta, parent, false);
        return new VentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaViewHolder holder, int position) {
        Pedido pedido = ventas.get(position);

        int total = 0;
        int totalCantidad = 0;

        // Calcular total y total de productos vendidos
        for (String pp : pedido.getProductos()) {
            String[] partes = pp.split("_");
            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);

            for (Producto p : Inventario.productos) {
                if (p.getCodigo() == codigo) {
                    total += p.getPrecio() * cant;
                    totalCantidad += cant;
                    break;
                }
            }
        }

        holder.txtClienteVenta.setText(pedido.getCliente().getNombre());
        holder.txtCantidadVenta.setText(String.valueOf(totalCantidad));
        holder.txtFechaVenta.setText(pedido.getFecha());
        holder.txtTotalVenta.setText(String.valueOf(total));

        holder.btnVerProductos.setOnClickListener(v -> mostrarDialogoProductos(pedido));
    }

    private void mostrarDialogoProductos(Pedido pedido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Productos del Pedido");

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        ScrollView verticalScrollView = new ScrollView(context);
        TableLayout table = new TableLayout(context);
        table.setStretchAllColumns(true);

        TableRow header = new TableRow(context);
        header.addView(createHeaderCell("Código"));
        header.addView(createHeaderCell("Nombre"));
        header.addView(createHeaderCell("Cantidad"));
        table.addView(header);

        for (String pp : pedido.getProductos()) {
            String[] partes = pp.split("_");
            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);
            Producto productoEncontrado = null;

            for (Producto p : Inventario.productos) {
                if (p.getCodigo() == codigo) {
                    productoEncontrado = p;
                    break;
                }
            }

            if (productoEncontrado != null) {
                TableRow fila = new TableRow(context);
                fila.addView(createDataCell(String.valueOf(productoEncontrado.getCodigo())));
                fila.addView(createDataCell(productoEncontrado.getNombre()));
                fila.addView(createDataCell(String.valueOf(cant)));
                table.addView(fila);
            }
        }

        verticalScrollView.addView(table);
        horizontalScrollView.addView(verticalScrollView);
        builder.setView(horizontalScrollView);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    private TextView createHeaderCell(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(14);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    private TextView createDataCell(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(13);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return tv;
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView txtClienteVenta, txtCantidadVenta, txtFechaVenta, txtTotalVenta;
        Button btnVerProductos;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClienteVenta = itemView.findViewById(R.id.txtClienteVenta);
            txtCantidadVenta = itemView.findViewById(R.id.txtCantidadVenta);
            txtFechaVenta = itemView.findViewById(R.id.txtFechaVenta);
            txtTotalVenta = itemView.findViewById(R.id.txtTotalVenta);
            btnVerProductos = itemView.findViewById(R.id.btnVerProductos);
        }
    }
}
