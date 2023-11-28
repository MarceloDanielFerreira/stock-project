package com.app.stockproject.service;

import com.app.stockproject.dto.LibroDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class ReportService {

    public byte[] generateLowStockReport(List<LibroDto> librosConBajoStock) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        document.add(new Paragraph("Informe de Libros con Bajo Stock"));

        // Crear una tabla con columnas: Título, Cantidad en Stock, Precio Unitario, Total
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 2, 2}); // Ajusta los anchos de las columnas según tus necesidades

        // Encabezados de la tabla
        PdfPCell cellTitulo = new PdfPCell(new Phrase("Título"));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTitulo);

        PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad en Stock"));
        cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellCantidad);

        PdfPCell cellPrecioUnitario = new PdfPCell(new Phrase("Precio Unitario"));
        cellPrecioUnitario.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellPrecioUnitario);

        PdfPCell cellTotal = new PdfPCell(new Phrase("Total"));
        cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTotal);

        // Agregar filas con datos de libros y calcular total
        double totalEstimado = 0;
        for (LibroDto libro : librosConBajoStock) {
            PdfPCell cellTituloData = new PdfPCell(new Phrase(libro.getTitulo()));
            cellTituloData.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cellTituloData);

            PdfPCell cellCantidadData = new PdfPCell(new Phrase(String.valueOf(libro.getCantidad())));
            cellCantidadData.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellCantidadData);

            double precioUnitario = libro.getPrecio(); // Suponiendo que tienes un método en LibroDto para obtener el precio
            double totalLibro = libro.getCantidad() * precioUnitario;
            totalEstimado += totalLibro;

            PdfPCell cellPrecioUnitarioData = new PdfPCell(new Phrase(formatCurrency(precioUnitario)));
            cellPrecioUnitarioData.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellPrecioUnitarioData);

            PdfPCell cellTotalData = new PdfPCell(new Phrase(formatCurrency(totalLibro)));
            cellTotalData.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellTotalData);
        }

        // Agregar fila con total estimado
        PdfPCell cellTotalEstimado = new PdfPCell(new Phrase("Total Estimado"));
        cellTotalEstimado.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTotalEstimado.setColspan(3); // Ocupa tres columnas
        table.addCell(cellTotalEstimado);

        PdfPCell cellTotalEstimadoData = new PdfPCell(new Phrase(formatCurrency(totalEstimado)));
        cellTotalEstimadoData.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTotalEstimadoData);

        // Agregar la tabla al documento
        document.add(table);

        document.close();

        return outputStream.toByteArray();
    }

    private String formatCurrency(double amount) {
        // Formatea el monto como moneda (por ejemplo, "$1,000.00")
        Locale locale = new Locale("en", "US"); // Locale para Estados Unidos
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }
}
