package com.shivaishta.prueba20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {
    private List<Cliente> clientes;

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cliente c = clientes.get(position);
        holder.nombre.setText(c.getNombre());
        holder.detalle.setText(c.getTelefono() + " | " + (c.esUrgente() ? "Urgente" : "Normal"));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, detalle;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(android.R.id.text1);
            detalle = itemView.findViewById(android.R.id.text2);
        }
    }
}
