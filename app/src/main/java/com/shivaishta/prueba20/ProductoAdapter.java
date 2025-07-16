package com.shivaishta.prueba20;

import android.view.*;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private final ArrayList<Producto> productos;

    public ProductoAdapter(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductoAdapter.ViewHolder holder, int position) {
        Producto p = productos.get(position);
        holder.nombre.setText(p.getNombre());
        holder.detalles.setText("Precio: $" + p.getPrecio() + " | Stock: " + p.getCantidad());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, detalles;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(android.R.id.text1);
            detalles = itemView.findViewById(android.R.id.text2);
        }
    }
}
