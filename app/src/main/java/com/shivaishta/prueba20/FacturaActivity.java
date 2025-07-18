package com.shivaishta.prueba20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

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

        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
        if (pedido == null) {
            Toast.makeText(this, "No se recibió el pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvResumen = findViewById(R.id.tvResumen);
        tvResumen.setText("Factura para " + pedido.getCliente().getNombre());

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
        try {
            File directorio = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "FacturasLucita");
            if (!directorio.exists() && !directorio.mkdirs()) {
                Toast.makeText(this, "No se pudo crear el directorio", Toast.LENGTH_SHORT).show();
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombreArchivo = "Factura_" + timeStamp + ".pdf";
            pdfFile = new File(directorio, nombreArchivo);

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            doc.open();

            Font titulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            Paragraph encabezado = new Paragraph("DISTRIBUIDORA LUCITA\nNIT: 900.123.456-7\nCra 45 #32-20, Bogotá\nTel: (601) 555 1234\nEmail: contacto@lucita.com", normal);
            encabezado.setAlignment(Element.ALIGN_LEFT);
            doc.add(encabezado);
            doc.add(new Paragraph(" "));

            Paragraph tituloFactura = new Paragraph("FACTURA DE VENTA", titulo);
            tituloFactura.setAlignment(Element.ALIGN_CENTER);
            doc.add(tituloFactura);
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()), normal));
            doc.add(new Paragraph("Número de Factura: " + timeStamp, normal));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Cliente:", negrita));
            doc.add(new Paragraph("Nombre: " + pedido.getCliente().getNombre(), normal));
            doc.add(new Paragraph("Teléfono: " + pedido.getCliente().getTelefono(), normal));
            doc.add(new Paragraph("Dirección: " + pedido.getCliente().getDireccion(), normal));
            doc.add(new Paragraph(" "));


            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2, 5, 3, 3});

            tabla.addCell(new PdfPCell(new Paragraph("Cant.", negrita)));
            tabla.addCell(new PdfPCell(new Paragraph("Descripción", negrita)));
            tabla.addCell(new PdfPCell(new Paragraph("Precio Unit.", negrita)));
            tabla.addCell(new PdfPCell(new Paragraph("Subtotal", negrita)));

            double total = 0.0;

            for (String item : pedido.getProductos()) {
                String[] partes = item.split("_");
                if (partes.length != 2) continue;

                String codigo = partes[0];
                int cantidad;
                try {
                    cantidad = Integer.parseInt(partes[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                int cod = Integer.parseInt(codigo);
                Producto prod = Producto.buscarPorCodigo(cod);
                if (prod == null) continue;

                double subtotal = prod.getPrecio() * cantidad;
                total += subtotal;

                tabla.addCell(new Paragraph(String.valueOf(cantidad), normal));
                tabla.addCell(new Paragraph(prod.getNombre(), normal));
                tabla.addCell(new Paragraph(String.format("$%,.2f", prod.getPrecio()), normal));
                tabla.addCell(new Paragraph(String.format("$%,.2f", subtotal), normal));
            }

            doc.add(tabla);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("TOTAL A PAGAR: " + String.format("$%,.2f", total), negrita));

            doc.close();

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
            Toast.makeText(this, "Error al generar factura", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void abrirFactura() {
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No hay visor de PDF instalado", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartirFactura() {
        if (pdfFile == null || !pdfFile.exists()) {
            Toast.makeText(this, "Primero genera la factura", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
        String telefono = pedido.getCliente().getTelefonoInternacional(); // +573001234567

        if (telefono == null || telefono.isEmpty()) {
            Toast.makeText(this, "Número de cliente no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String jid = telefono.replace("+", "") + "@s.whatsapp.net";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra("jid", jid); // Envío directo
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir WhatsApp", Toast.LENGTH_LONG).show();
        }
    }

}
