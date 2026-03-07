package com.tharushinis.Ocean_view_resort.util;

import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    /**
     * Generate PDF bill
     * @param bill Bill object with all details
     * @return byte array of PDF
     */
    public static byte[] generateBillPDF(Bill bill) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add header
            addHeader(document);

            // Add bill info
            addBillInfo(document, bill);

            // Add customer info
            addCustomerInfo(document, bill);

            // Add items table
            addItemsTable(document, bill);

            // Add footer
            addFooter(document, bill);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new Exception("Error generating PDF: " + e.getMessage());
        }
    }

    private static void addHeader(Document document) throws DocumentException {
        // Company header
        Paragraph header = new Paragraph();
        header.setAlignment(Element.ALIGN_CENTER);

        Chunk title = new Chunk("Ocean View Resort", TITLE_FONT);
        header.add(title);
        header.add(Chunk.NEWLINE);

        Chunk subtitle = new Chunk("Online Room Reservation System", HEADER_FONT);
        header.add(subtitle);
        header.add(Chunk.NEWLINE);

        Chunk address = new Chunk("123 Main Street, Colombo | Tel: 011-1234567", SMALL_FONT);
        header.add(address);

        document.add(header);
        document.add(new Paragraph("\n"));

        // Add line separator
        LineSeparator line = new LineSeparator();
        document.add(new Chunk(line));
        document.add(new Paragraph("\n"));
    }

    private static void addBillInfo(Document document, Bill bill) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Left side
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.addElement(new Paragraph("INVOICE", HEADER_FONT));
        table.addCell(leftCell);

        // Right side
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Paragraph billDetails = new Paragraph();
        billDetails.add(new Chunk("Bill Number: ", NORMAL_FONT));
        billDetails.add(new Chunk(bill.getBillNumber() + "\n", NORMAL_FONT));

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        billDetails.add(new Chunk("Date: ", NORMAL_FONT));
        billDetails.add(new Chunk(sdf.format(bill.getBillDate()) + "\n", NORMAL_FONT));

        billDetails.add(new Chunk("Prepared By: ", NORMAL_FONT));
        billDetails.add(new Chunk(bill.getCreatedBy().getFullName(), NORMAL_FONT));

        rightCell.addElement(billDetails);
        table.addCell(rightCell);

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private static void addCustomerInfo(Document document, Bill bill) throws DocumentException {
        Paragraph customerHeader = new Paragraph("Bill To:", HEADER_FONT);
        document.add(customerHeader);

        Paragraph customerDetails = new Paragraph();
        customerDetails.add(new Chunk(bill.getCustomer().getName() + "\n", NORMAL_FONT));

        if (bill.getCustomer().getEmail() != null) {
            customerDetails.add(new Chunk(bill.getCustomer().getEmail() + "\n", SMALL_FONT));
        }
        if (bill.getCustomer().getPhone() != null) {
            customerDetails.add(new Chunk(bill.getCustomer().getPhone() + "\n", SMALL_FONT));
        }
        if (bill.getCustomer().getAddress() != null) {
            customerDetails.add(new Chunk(bill.getCustomer().getAddress(), SMALL_FONT));
        }

        document.add(customerDetails);
        document.add(new Paragraph("\n"));
    }

    private static void addItemsTable(Document document, Bill bill) throws DocumentException {
        // Create table with 5 columns
        PdfPTable table = new PdfPTable(new float[]{1, 4, 2, 2, 2});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Add table headers
        addTableHeader(table, "#");
        addTableHeader(table, "Item Description");
        addTableHeader(table, "Unit Price");
        addTableHeader(table, "Quantity");
        addTableHeader(table, "Total");

        // Add items
        int index = 1;
        for (BillItem item : bill.getBillItems()) {
            addTableCell(table, String.valueOf(index++));
            addTableCell(table, item.getItem().getName());
            addTableCell(table, String.format("Rs. %.2f", item.getUnitPrice()));
            addTableCell(table, String.valueOf(item.getQuantity()));
            addTableCell(table, String.format("Rs. %.2f", item.getTotalPrice()));
        }

        // Add subtotal row
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setColspan(3);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(emptyCell);

        addTableCell(table, "Subtotal:", true);
        addTableCell(table, String.format("Rs. %.2f", bill.getTotalAmount()), true);

        // Add tax row
        table.addCell(emptyCell);
        addTableCell(table, "Tax (0%):", true);
        addTableCell(table, "Rs. 0.00", true);

        // Add grand total row
        table.addCell(emptyCell);
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Grand Total:", HEADER_FONT));
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setBorder(Rectangle.TOP);
        table.addCell(totalLabelCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(
                String.format("Rs. %.2f", bill.getTotalAmount()), HEADER_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorder(Rectangle.TOP);
        table.addCell(totalValueCell);

        document.add(table);
    }

    private static void addTableHeader(PdfPTable table, String text) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(text, HEADER_FONT));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(8);
        table.addCell(header);
    }

    private static void addTableCell(PdfPTable table, String text) {
        addTableCell(table, text, false);
    }

    private static void addTableCell(PdfPTable table, String text, boolean isAlignRight) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(5);
        if (isAlignRight) {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        table.addCell(cell);
    }

    private static void addFooter(Document document, Bill bill) throws DocumentException {
        document.add(new Paragraph("\n\n"));

        LineSeparator line = new LineSeparator();
        document.add(new Chunk(line));

        Paragraph footer = new Paragraph();
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.add(new Chunk("\nThank you for your business!\n", NORMAL_FONT));
        footer.add(new Chunk("Generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), SMALL_FONT));

        document.add(footer);
    }


    public static void saveBillPDF(Bill bill, String filePath) throws Exception {
        byte[] pdfBytes = generateBillPDF(bill);
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(pdfBytes);
        fos.close();
    }
}
