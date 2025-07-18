package com.shivaishta.prueba20;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidosViewHolder> {

    private List<Pedido> pedidos;
    private Context context;

    public PedidosAdapter(Context context) {
        this.context = context;
        this.pedidos = new ArrayList<>();
        for (Pedido p : Pedido.pedidos) {
            if (!p.getConfirmado()) {
                pedidos.add(p);
            }
        }
    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new PedidosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        int total = 0;
        int totalCantidad = 0;

        for (String pp : pedido.getProductos()) {
            String[] partes = pp.split("_");
            if (partes.length != 2) continue;

            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);

            for (Producto p : Producto.getProductos()) {
                if (p.getCodigo() == codigo) {
                    total += p.getPrecio() * cant;
                    totalCantidad += cant;
                    break;
                }
            }
        }

        holder.txtCliente.setText(pedido.getCliente().getNombre());
        holder.txtCantidad.setText(String.valueOf(totalCantidad));
        holder.txtFecha.setText(pedido.getFecha());
        holder.txtTotal.setText(String.valueOf(total));

        holder.btnVerProductos.setOnClickListener(v -> mostrarDialogoProductos(pedido));

        holder.btnConfirmar.setOnClickListener(v -> {
            pedido.confirmar();
            pedidos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pedidos.size());
            Pedido.guardarPed(context);
        });
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
            if (partes.length != 2) continue;

            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);

            for (Producto p : Producto.getProductos()) {
                if (p.getCodigo() == codigo) {
                    TableRow fila = new TableRow(context);
                    fila.addView(createDataCell(String.valueOf(p.getCodigo())));
                    fila.addView(createDataCell(p.getNombre()));
                    fila.addView(createDataCell(String.valueOf(cant)));
                    table.addView(fila);
                    break;
                }
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
        return pedidos.size();
    }

    static class PedidosViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtCantidad, txtFecha, txtTotal;
        Button btnVerProductos, btnConfirmar;

        public PedidosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.txtClientePedido);
            txtCantidad = itemView.findViewById(R.id.txtCantidadPedido);
            txtFecha = itemView.findViewById(R.id.txtFechaPedido);
            txtTotal = itemView.findViewById(R.id.txtTotalPedido);
            btnVerProductos = itemView.findViewById(R.id.btnVerProductosPed);
            btnConfirmar = itemView.findViewById(R.id.btnConfirm);
        }
    }
}
