package com.shivaishta.prueba20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FacturaActivity extends AppCompatActivity {

    private Pedido pedido;
    private File pdfFile;
    private Button btnGenerar, btnAbrir, btnCompartir, btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        // Obtener pedido del Intent
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        if (pedido == null) {
            Toast.makeText(this, "No se recibió el pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Mostrar resumen
        TextView tvResumen = findViewById(R.id.tvResumen);
        tvResumen.setText(String.format("Factura para %s", pedido.getCliente().getNombre()));

        // Configurar botones
        btnGenerar = findViewById(R.id.btnGenerar);
        btnAbrir = findViewById(R.id.btnAbrir);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        // Inicialmente solo mostrar el botón Generar
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
        try {
            File directorio = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "FacturasLucita");
            if (!directorio.exists() && !directorio.mkdirs()) {
                Toast.makeText(this, "Error al crear directorio", Toast.LENGTH_SHORT).show();
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombreArchivo = "Factura_" + timeStamp + ".pdf";
            pdfFile = new File(directorio, nombreArchivo);

            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(pdfFile));
            documento.open();

            // Fuentes
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fuenteSub = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Encabezado empresa
            Paragraph encabezado = new Paragraph("DISTRIBUIDORA LUCITA\nNIT: 900.123.456-7\nCra 45 #32-20, Bogotá\nTel: (601) 555 1234\nEmail: contacto@lucita.com", fuenteSub);
            encabezado.setAlignment(Element.ALIGN_LEFT);
            documento.add(encabezado);

            documento.add(new Paragraph(" ", fuenteSub)); // Espaciado

            // Título de factura
            Paragraph titulo = new Paragraph("FACTURA DE VENTA", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph(" "));

            // Fecha y número
            documento.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()), fuenteSub));
            documento.add(new Paragraph("Número de Factura: " + timeStamp, fuenteSub));
            documento.add(new Paragraph(" "));

            // Datos del cliente
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

            for (String pp : pedido.getProductos()) { //partes[0] es el codigo del producto
                                                    //partes [1] es la cantidad de ese prod.
                String[] partes = pp.split("_");
                int codigo = Integer.parseInt(partes[0]);
                int cant = Integer.parseInt(partes[1]);
                Producto p = null;
                //obtener producto
                for (Producto producto : Producto.productos) {
                    if (producto.getCodigo() == codigo) {
                        p = producto;
                        break;
                    }
                }

                if (p != null) {

                tabla.addCell(new Paragraph(String.valueOf(p.getCantidad()), fuenteSub));
                tabla.addCell(new Paragraph(p.getNombre(), fuenteSub));
                tabla.addCell(new Paragraph(String.format("$%,.2f", p.getPrecio()), fuenteSub));
                tabla.addCell(new Paragraph(String.format("$%,.2f", p.getPrecio() * p.getCantidad()), fuenteSub));
            } }

            documento.add(tabla);

            // Total
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(
                    String.format("TOTAL A PAGAR: $%,.2f", calcularTotal()),
                    fuenteNegrita));

            documento.close();

            new AlertDialog.Builder(this)
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

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_LONG).show();
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

    private double calcularTotal() {
        double total = 0;
        for (String pp : pedido.getProductos()) { //partes[0] es el codigo del producto
                                                  //partes [1] es la cantidad de ese prod.
            String[] partes = pp.split("_");
            int codigo = Integer.parseInt(partes[0]);
            int cant = Integer.parseInt(partes[1]);
            Producto p = null;
            //obtener producto
            for (Producto producto : Producto.productos) {
                if (producto.getCodigo() == codigo) {
                    p = producto;
                    break;
                }
            }

            if (p != null) {
            total += p.getPrecio() * cant;
            }
        }
        return total;
    }

    private void compartirFactura() {
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(this, "Primero genera la factura", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".provider",
                pdfFile
        );

        String telefono = pedido.getCliente().getTelefonoInternacional(); // Ej: "+57300..."
        if (telefono.isEmpty()) {
            Toast.makeText(this, "El cliente no tiene número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Formato requerido por WhatsApp: número sin el "+" y el dominio de usuario
        String numeroSinSignoMas = telefono.replace("+", "");
        String jid = numeroSinSignoMas + "@s.whatsapp.net";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra("jid", jid); // Dirección directa al chat
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir WhatsApp: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void mostrarDialogoError(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }
    private boolean isWhatsAppInstalled() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            try {
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES); // WhatsApp Business
                return true;
            } catch (PackageManager.NameNotFoundException ex) {
                return false;
            }
        }
    }


}