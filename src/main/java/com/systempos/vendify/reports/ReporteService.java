package com.systempos.vendify.reports;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.systempos.vendify.model.Cliente;
import com.systempos.vendify.model.Producto;
import com.systempos.vendify.model.Venta;
import com.systempos.vendify.repository.ClienteRepository;
import com.systempos.vendify.repository.ProductoRepository;
import com.systempos.vendify.repository.VentaRepository;

@Service
public class ReporteService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    ProductoRepository productoRepository;

    // Ruta de imagen
    private static final String IMAGE_PATH = "static/reporte.png"; 

    public byte[] generarReporteClientes() throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Agregar imagen
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
        if (imageStream != null) {
            byte[] imageBytes = imageStream.readAllBytes();
            Image img = Image.getInstance(imageBytes);
            img.scaleToFit(100, 100);
            img.setAlignment(Image.ALIGN_RIGHT);
            document.add(img);
        } else {
            System.err.println("No se encontró la imagen en el classpath: " + IMAGE_PATH);
    }


        // Encabezado
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font fontTableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        document.add(new Paragraph("Reporte creado por vendiFyAPI v.25", fontSubtitulo));
        document.add(new Paragraph("© DevSandoval", fontSubtitulo));
        document.add(new Paragraph("\nReporte de Clientes", fontTitulo));

        // Espaciado
        document.add(new Paragraph("\n"));

        // Tabla de clientes
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        // Encabezados de la tabla
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("ID", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nombre", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Datos de clientes
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            table.addCell(String.valueOf(cliente.getId()));
            table.addCell(cliente.getNombre());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public byte[] generarReporteProductos() throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Agregar imagen
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
        if (imageStream != null) {
            byte[] imageBytes = imageStream.readAllBytes();
            Image img = Image.getInstance(imageBytes);
            img.scaleToFit(100, 100);
            img.setAlignment(Image.ALIGN_RIGHT);
            document.add(img);
        } else {
            System.err.println("No se encontró la imagen en el classpath: " + IMAGE_PATH);
    }

        // Encabezado
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font fontTableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        document.add(new Paragraph("Reporte creado por vendiFyAPI v.25", fontSubtitulo));
        document.add(new Paragraph("© DevSandoval", fontSubtitulo));
        document.add(new Paragraph("\nReporte de Productos", fontTitulo));

        // Espaciado
        document.add(new Paragraph("\n"));

        // Tabla de productos
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2});

        // Encabezados de la tabla
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("ID", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nombre", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Precio", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Datos de productos
        List<Producto> productos = productoRepository.findAll();
        for (Producto producto : productos) {
            table.addCell(String.valueOf(producto.getId()));
            table.addCell(producto.getNombre());
            table.addCell("$" + producto.getPrecioUnitario());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public byte[] generarReporteVentas() throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Agregar imagen
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
        if (imageStream != null) {
            byte[] imageBytes = imageStream.readAllBytes();
            Image img = Image.getInstance(imageBytes);
            img.scaleToFit(100, 100);
            img.setAlignment(Image.ALIGN_RIGHT);
            document.add(img);
        } else {
            System.err.println("No se encontró la imagen en el classpath: " + IMAGE_PATH);
    }

        // Encabezado
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font fontTableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        document.add(new Paragraph("Reporte creado por vendiFyAPI v.25", fontSubtitulo));
        document.add(new Paragraph("© DevSandoval", fontSubtitulo));
        document.add(new Paragraph("\nReporte de Ventas", fontTitulo));

        // Espaciado
        document.add(new Paragraph("\n"));

        // Tabla de ventas
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2});

        // Encabezados de la tabla
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("ID", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total", fontTableHeader));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Datos de ventas
        List<Venta> ventas = ventaRepository.findAll();
        for (Venta venta : ventas) {
            table.addCell(String.valueOf(venta.getId()));
            table.addCell(venta.getFecha().toString());
            table.addCell("$" + venta.getTotal());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

}
