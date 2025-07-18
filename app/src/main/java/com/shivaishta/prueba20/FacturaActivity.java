package com.shivaishta.prueba20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FacturaActivity extends AppCompatActivity {

    private static final String TAG = "FacturaActivity";
    private Pedido pedido;
    private File pdfFile;
    private Button btnGenerar, btnAbrir, btnCompartir, btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        if (pedido == null) {
            Toast.makeText(this, "No se recibió el pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvResumen = findViewById(R.id.tvResumen);
        tvResumen.setText(String.format("Factura para %s", pedido.getCliente().getNombre()));

        btnGenerar = findViewById(R.id.btnGenerar);
        btnAbrir = findViewById(R.id.btnAbrir);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        btnGenerar.setVisibility(View.VISIBLE);
        btnAbrir.setVisibility(View.GONE);
        btnCompartir.setVisibility(View.GONE);
        btnFinalizar.setVisibility(View.GONE);

        btnGenerar.setOnClickListener(v -> generarFactura());
        btnAbrir.setOnClickListener(v -> abrirFactura());
        btnCompartir.setOnClickListener(v -> compartirFactura());
        btnFinalizar.setOnClickListener(v -> finish());
    }

    private void generarFactura() {
        // Verificar permisos primero
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
            return;
        }

        new Thread(() -> {
            try {
                File directorio;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    directorio = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "FacturasLucita");
                } else {
                    directorio = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS), "FacturasLucita");
                }

                if (!directorio.exists() && !directorio.mkdirs()) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error al crear directorio", Toast.LENGTH_SHORT).show());
                    return;
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String nombreArchivo = "Factura_" + timeStamp + ".pdf";
                pdfFile = new File(directorio, nombreArchivo);

                Log.d(TAG, "Generando PDF en: " + pdfFile.getAbsolutePath());

                Document documento = new Document();
                PdfWriter.getInstance(documento, new FileOutputStream(pdfFile));
                documento.open();

                Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                Font fuenteSub = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

                // Encabezado
                Paragraph encabezado = new Paragraph("DISTRIBUIDORA LUCITA\nNIT: 900.123.456-7\nCra 45 #32-20, Bogotá\nTel: (601) 555 1234\nEmail: contacto@lucita.com", fuenteSub);
                encabezado.setAlignment(Element.ALIGN_LEFT);
                documento.add(encabezado);
                documento.add(new Paragraph(" ", fuenteSub));

                // Título
                Paragraph titulo = new Paragraph("FACTURA DE VENTA", fuenteTitulo);
                titulo.setAlignment(Element.ALIGN_CENTER);
                documento.add(titulo);
                documento.add(new Paragraph(" "));

                // Datos
                documento.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()), fuenteSub));
                documento.add(new Paragraph("Número de Factura: " + timeStamp, fuenteSub));
                documento.add(new Paragraph(" "));

                // Cliente
                documento.add(new Paragraph("Cliente:", fuenteNegrita));
                documento.add(new Paragraph("Nombre: " + pedido.getCliente().getNombre(), fuenteSub));
                documento.add(new Paragraph("Teléfono: " + pedido.getCliente().getTelefono(), fuenteSub));
                documento.add(new Paragraph("Dirección: " + pedido.getCliente().getDireccion(), fuenteSub));
                documento.add(new Paragraph(" "));

                // Tabla de productos
                PdfPTable tabla = new PdfPTable(4);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{2, 5, 3, 3});

                tabla.addCell(new PdfPCell(new Paragraph("Cant.", fuenteNegrita)));
                tabla.addCell(new PdfPCell(new Paragraph("Descripción", fuenteNegrita)));
                tabla.addCell(new PdfPCell(new Paragraph("Precio Unit.", fuenteNegrita)));
                tabla.addCell(new PdfPCell(new Paragraph("Subtotal", fuenteNegrita)));

                List<ProductoCantidad> productos = parseProductos(pedido.getProductos());
                for (ProductoCantidad pc : productos) {
                    tabla.addCell(new Paragraph(String.valueOf(pc.cantidad), fuenteSub));
                    tabla.addCell(new Paragraph(pc.producto.getNombre(), fuenteSub));
                    tabla.addCell(new Paragraph(String.format("$%,.2f", pc.producto.getPrecio()), fuenteSub));
                    tabla.addCell(new Paragraph(String.format("$%,.2f", pc.producto.getPrecio() * pc.cantidad), fuenteSub));
                }

                documento.add(tabla);
                documento.add(new Paragraph(" "));
                documento.add(new Paragraph(
                        String.format("TOTAL A PAGAR: $%,.2f", calcularTotal(productos)),
                        fuenteNegrita));

                documento.close();

                // Verificar que el archivo se creó correctamente
                if (pdfFile.exists()) {
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(FacturaActivity.this)
                                .setTitle("Éxito")
                                .setMessage("Factura generada exitosamente")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    btnGenerar.setVisibility(View.GONE);
                                    btnAbrir.setVisibility(View.VISIBLE);
                                    btnCompartir.setVisibility(View.VISIBLE);
                                    btnFinalizar.setVisibility(View.VISIBLE);
                                })
                                .setCancelable(false)
                                .show();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error: No se pudo crear el archivo PDF", Toast.LENGTH_LONG).show());
                }

            } catch (IOException | DocumentException e) {
                Log.e(TAG, "Error al generar PDF", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private List<ProductoCantidad> parseProductos(List<String> productosStr) {
        List<ProductoCantidad> productos = new ArrayList<>();
        for (String productoStr : productosStr) {
            String[] partes = productoStr.split("_");
            if (partes.length == 2) {
                try {
                    int codigo = Integer.parseInt(partes[0]);
                    int cantidad = Integer.parseInt(partes[1]);
                    Producto producto = findProductoByCodigo(codigo);
                    if (producto != null) {
                        productos.add(new ProductoCantidad(producto, cantidad));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return productos;
    }

    private Producto findProductoByCodigo(int codigo) {
        for (Producto p : Inventario.productos) { // ← USAR INVENTARIO
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    private double calcularTotal(List<ProductoCantidad> productos) {
        double total = 0;
        for (ProductoCantidad pc : productos) {
            total += pc.producto.getPrecio() * pc.cantidad;
        }
        return total;
    }

    private static class ProductoCantidad {
        Producto producto;
        int cantidad;

        ProductoCantidad(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
    }

    private void abrirFactura() {
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(this, "El archivo no existe", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider",
                pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No hay aplicación para ver PDFs", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartirFactura() {
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(this, "Primero genera la factura", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isWhatsAppInstalled()) {
            Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".provider",
                pdfFile
        );

        String telefono = pedido.getCliente().getTelefono();
        if (telefono == null || telefono.isEmpty()) {
            Toast.makeText(this, "El cliente no tiene número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra("jid", telefono + "@s.whatsapp.net");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error al compartir: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isWhatsAppInstalled() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void mostrarDialogoError(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}