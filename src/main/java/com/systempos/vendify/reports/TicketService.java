package com.systempos.vendify.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.systempos.vendify.model.Concepto;
import com.systempos.vendify.model.Venta;

import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    public void generarTicketPDF(Venta venta) throws DocumentException, IOException {
        Document document = new Document(PageSize.A7);
        PdfWriter.getInstance(document, new FileOutputStream("ticket_" + venta.getId() + ".pdf"));
        document.open();

        // Fuentes
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 8);

        // Encabezado
        Paragraph header = new Paragraph("vendiFyAPI\n", fontBold);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph empresa = new Paragraph(
                "RFC: SASA031023\n" +
                "Dirección: San Ignacio 37\n" +
                "Tel: (341) 419-0809\n", fontNormal);
        empresa.setAlignment(Element.ALIGN_CENTER);
        document.add(empresa);

        document.add(new Paragraph("================================", fontBold));

        // Datos de la venta
        document.add(new Paragraph("Venta No: " + venta.getId(), fontNormal));
        document.add(new Paragraph("Fecha: " + venta.getFecha(), fontNormal));
        document.add(new Paragraph("Cliente: " + venta.getCliente().getId(), fontNormal));

        document.add(new Paragraph("--------------------------------", fontNormal));

        // Crear tabla
        PdfPTable table = new PdfPTable(4); // 4 columnas
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 2, 2}); // Ajusta las proporciones

        // Encabezados
        table.addCell(new PdfPCell(new Phrase("Producto", fontBold)));
        table.addCell(new PdfPCell(new Phrase("Cant", fontBold)));
        table.addCell(new PdfPCell(new Phrase("P.Unit", fontBold)));
        table.addCell(new PdfPCell(new Phrase("Total", fontBold)));

        // Línea separadora
        PdfPCell separator = new PdfPCell(new Phrase("--------------------------------", fontNormal));
        separator.setColspan(4);
        separator.setBorder(PdfPCell.NO_BORDER);
        table.addCell(separator);

        // Productos
        for (Concepto concepto : venta.getConceptos()) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(concepto.getProducto().getId()), fontNormal)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(concepto.getCantidad()), fontNormal)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", concepto.getPrecioUnitario()), fontNormal)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", concepto.getImporte()), fontNormal)));
        }

        document.add(table);

        document.add(new Paragraph("--------------------------------", fontNormal));

        // Total
        Paragraph total = new Paragraph("TOTAL: $" + String.format("%.2f", venta.getTotal()), fontBold);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
    }

}
