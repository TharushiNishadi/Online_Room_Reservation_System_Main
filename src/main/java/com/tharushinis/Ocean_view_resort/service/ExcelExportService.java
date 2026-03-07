package com.tharushinis.Ocean_view_resort.service;

import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.Customer;


import com.tharushinis.Ocean_view_resort.model.Item;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelExportService {
    /**
     * Export customers list to Excel
     * @param customers List of customers
     * @return byte array of Excel file
     */
    public static byte[] exportCustomers(List<Customer> customers) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        // Create header style
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Email", "Phone", "Address", "Created Date"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getId());
            row.createCell(1).setCellValue(customer.getName());
            row.createCell(2).setCellValue(customer.getEmail() != null ? customer.getEmail() : "");
            row.createCell(3).setCellValue(customer.getPhone() != null ? customer.getPhone() : "");
            row.createCell(4).setCellValue(customer.getAddress() != null ? customer.getAddress() : "");
            row.createCell(5).setCellValue(customer.getCreatedAt().toString());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    /**
     * Export items inventory to Excel
     * @param items List of items
     * @return byte array of Excel file
     */
    public static byte[] exportItems(List<Item> items) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        CellStyle warningStyle = createWarningStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Item Name", "Description", "Unit Price (Rs.)", "Stock Quantity", "Total Value", "Status"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        double totalInventoryValue = 0;

        for (Item item : items) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(item.getDescription() != null ? item.getDescription() : "");

            Cell priceCell = row.createCell(3);
            priceCell.setCellValue(item.getUnitPrice());
            priceCell.setCellStyle(currencyStyle);

            Cell stockCell = row.createCell(4);
            stockCell.setCellValue(item.getStockQuantity());
            if (item.getStockQuantity() < 10) {
                stockCell.setCellStyle(warningStyle);
            }

            double totalValue = item.getUnitPrice() * item.getStockQuantity();
            Cell valueCell = row.createCell(5);
            valueCell.setCellValue(totalValue);
            valueCell.setCellStyle(currencyStyle);
            totalInventoryValue += totalValue;

            String status = item.getStockQuantity() == 0 ? "Out of Stock" :
                    item.getStockQuantity() < 10 ? "Low Stock" : "In Stock";
            row.createCell(6).setCellValue(status);
        }

        // Add summary row
        Row summaryRow = sheet.createRow(rowNum + 1);
        Cell summaryLabel = summaryRow.createCell(4);
        summaryLabel.setCellValue("Total Inventory Value:");
        summaryLabel.setCellStyle(headerStyle);

        Cell summaryValue = summaryRow.createCell(5);
        summaryValue.setCellValue(totalInventoryValue);
        summaryValue.setCellStyle(headerStyle);

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    /**
     * Export sales report to Excel
     * @param bills List of bills
     * @return byte array of Excel file
     */
    public static byte[] exportSalesReport(List<Bill> bills) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Bill Number", "Date", "Customer", "Items Count", "Total Amount (Rs.)", "Created By"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        double totalSales = 0;

        for (Bill bill : bills) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getBillNumber());

            Cell dateCell = row.createCell(1);
            dateCell.setCellValue(bill.getBillDate());
            dateCell.setCellStyle(dateStyle);

            row.createCell(2).setCellValue(bill.getCustomer().getName());
            row.createCell(3).setCellValue(bill.getBillItems().size());

            Cell amountCell = row.createCell(4);
            amountCell.setCellValue(bill.getTotalAmount());
            amountCell.setCellStyle(currencyStyle);
            totalSales += bill.getTotalAmount();

            row.createCell(5).setCellValue(bill.getCreatedBy().getFullName());
        }

        // Add summary rows
        rowNum++;
        Row summaryRow1 = sheet.createRow(rowNum++);
        summaryRow1.createCell(3).setCellValue("Total Sales:");
        Cell totalCell = summaryRow1.createCell(4);
        totalCell.setCellValue(totalSales);
        totalCell.setCellStyle(headerStyle);

        Row summaryRow2 = sheet.createRow(rowNum++);
        summaryRow2.createCell(3).setCellValue("Number of Bills:");
        summaryRow2.createCell(4).setCellValue(bills.size());

        Row summaryRow3 = sheet.createRow(rowNum);
        summaryRow3.createCell(3).setCellValue("Average Bill Value:");
        Cell avgCell = summaryRow3.createCell(4);
        avgCell.setCellValue(bills.size() > 0 ? totalSales / bills.size() : 0);
        avgCell.setCellStyle(currencyStyle);

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    /**
     * Export comprehensive dashboard report
     * @param data Map containing various statistics
     * @return byte array of Excel file
     */
    public static byte[] exportDashboardReport(Map<String, Object> data) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        // Sheet 1: Overview
        Sheet overviewSheet = workbook.createSheet("Overview");
        createOverviewSheet(overviewSheet, data, workbook);

        // Sheet 2: Monthly Sales
        Sheet monthlySheet = workbook.createSheet("Monthly Sales");
        createMonthlySalesSheet(monthlySheet, data, workbook);

        // Sheet 3: Top Products
        Sheet productsSheet = workbook.createSheet("Top Products");
        createTopProductsSheet(productsSheet, data, workbook);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    // Helper methods for styling
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("dd-MM-yyyy"));
        return style;
    }

    private static CellStyle createWarningStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        return style;
    }

    private static void createOverviewSheet(Sheet sheet, Map<String, Object> data, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row row1 = sheet.createRow(0);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("Metric");
        cell1.setCellStyle(headerStyle);
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("Value");
        cell2.setCellStyle(headerStyle);

        int rowNum = 1;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().toString());
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private static void createMonthlySalesSheet(Sheet sheet, Map<String, Object> data, Workbook workbook) {
        // Implementation for monthly sales chart data
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);

        Cell monthHeader = headerRow.createCell(0);
        monthHeader.setCellValue("Month");
        monthHeader.setCellStyle(headerStyle);

        Cell salesHeader = headerRow.createCell(1);
        salesHeader.setCellValue("Sales (Rs.)");
        salesHeader.setCellStyle(headerStyle);

        // Add monthly data here
    }

    private static void createTopProductsSheet(Sheet sheet, Map<String, Object> data, Workbook workbook) {
        // Implementation for top products data
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);

        Cell productHeader = headerRow.createCell(0);
        productHeader.setCellValue("Product");
        productHeader.setCellStyle(headerStyle);

        Cell quantityHeader = headerRow.createCell(1);
        quantityHeader.setCellValue("Quantity Sold");
        quantityHeader.setCellStyle(headerStyle);

        // Add product data here
    }
}
