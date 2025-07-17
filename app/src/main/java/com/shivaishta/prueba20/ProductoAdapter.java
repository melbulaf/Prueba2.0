package com.shivaishta.prueba20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.txtCodigo.setText("Código: " + producto.getCodigo());
        holder.txtNombre.setText("Nombre: " + producto.getNombre());
        holder.txtCategoria.setText("Categoría: " + producto.getCategoria());
        holder.txtCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.txtPrecioCompra.setText("Precio compra: $" + producto.getPrecioC());
        holder.txtPrecioVenta.setText("Precio venta: $" + producto.getPrecioV());
        holder.txtUrgente.setText(producto.getUrgente());
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigo, txtNombre, txtCategoria, txtCantidad, txtPrecioCompra, txtPrecioVenta, txtUrgente;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecioCompra = itemView.findViewById(R.id.txtPrecioCompra);
            txtPrecioVenta = itemView.findViewById(R.id.txtPrecioVenta);
            txtUrgente = itemView.findViewById(R.id.txtUrgente);
        }
    }
}
