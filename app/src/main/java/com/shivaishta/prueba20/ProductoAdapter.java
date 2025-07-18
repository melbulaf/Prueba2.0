package com.shivaishta.prueba20;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> productos;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ProductoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtCodigo.setText("Código: " + producto.getCodigo());
        holder.txtPrecioCompra.setText("Precio compra: $" + producto.getPrecioC());
        holder.txtPrecioVenta.setText("Precio venta: $" + producto.getPrecioV());
        holder.txtCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.txtCategoria.setText("Categoría: " + producto.getCategoria());

        // Cambio de color si está seleccionado
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFEB3B")); // Amarillo
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        // Selección al hacer clic
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public Producto getSelectedProducto() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return productos.get(selectedPosition);
        }
        return null;
    }

    public void clearSelection() {
        int oldPosition = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        if (oldPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPosition);
        }
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCodigo, txtPrecioCompra, txtPrecioVenta, txtCantidad, txtCategoria;
        CardView cardView;

        ProductoViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtCodigo = itemView.findViewById(R.id.txtCodigoProducto);
            txtPrecioCompra = itemView.findViewById(R.id.txtPrecioCompra);
            txtPrecioVenta = itemView.findViewById(R.id.txtPrecioVenta);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            cardView = (CardView) itemView;
        }
    }
}
