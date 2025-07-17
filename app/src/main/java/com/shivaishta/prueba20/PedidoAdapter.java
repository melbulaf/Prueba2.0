package com.shivaishta.prueba20;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final List<Pedido> listaPedidos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pedido pedido);
    }

    public PedidoAdapter(List<Pedido> listaPedidos, OnItemClickListener listener) {
        this.listaPedidos = listaPedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = listaPedidos.get(position);
        Cliente cliente = pedido.getCliente();

        holder.tvCliente.setText(cliente.getNombre());
        holder.tvDireccion.setText(cliente.getDireccion());

        if (cliente.setUrgencia()) {
            holder.tvCliente.setTextColor(Color.RED);
            holder.tvCliente.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning, 0);
        } else {
            holder.tvCliente.setTextColor(Color.BLACK);
            holder.tvCliente.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(pedido));
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvCliente, tvDireccion;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
        }
    }
}