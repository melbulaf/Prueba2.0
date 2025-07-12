package com.shivaishta.prueba20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.shivaishta.prueba20.Compra;
public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private List<Compra> listaCompras;

    public CompraAdapter(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        Compra compra = listaCompras.get(position);
        holder.txtCodigo.setText(String.valueOf(compra.producto.getCodigo()));
        holder.txtNombre.setText(compra.producto.getNombre());
        holder.txtCantidad.setText(String.valueOf(compra.cantidad));
        holder.txtPrecio.setText(String.valueOf((int) compra.producto.getPrecioC()));
        holder.txtFecha.setText(compra.fecha);
        holder.txtTotal.setText(String.valueOf((int) compra.total));
    }

    @Override
    public int getItemCount() {
        return listaCompras.size();
    }

    public static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigo, txtNombre, txtCantidad, txtPrecio, txtFecha, txtTotal;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}