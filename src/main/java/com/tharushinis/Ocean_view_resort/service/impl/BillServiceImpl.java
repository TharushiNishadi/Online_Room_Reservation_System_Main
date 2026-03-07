package com.tharushinis.Ocean_view_resort.service.impl;



import com.tharushinis.Ocean_view_resort.dao.BillDAO;
import com.tharushinis.Ocean_view_resort.dao.ItemDAO;
import com.tharushinis.Ocean_view_resort.dao.impl.BillDAOImpl;
import com.tharushinis.Ocean_view_resort.dao.impl.ItemDAOImpl;
import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;
import com.tharushinis.Ocean_view_resort.model.Item;
import com.tharushinis.Ocean_view_resort.model.User;
import com.tharushinis.Ocean_view_resort.service.BillService;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillServiceImpl implements BillService {
    private BillDAO billDAO;
    private ItemDAO itemDAO;

    public BillServiceImpl() {
        this.billDAO = new BillDAOImpl();
        this.itemDAO = new ItemDAOImpl();
    }

    @Override
    public Bill save(Bill bill) throws Exception {
        if (!validate(bill)) {
            throw new IllegalArgumentException("Invalid bill data");
        }

        return billDAO.save(bill);
    }

    @Override
    public boolean update(Bill bill) throws Exception {
        if (!validate(bill)) {
            throw new IllegalArgumentException("Invalid bill data");
        }

        // Check if bill exists
        Bill existing = billDAO.findById(bill.getId());
        if (existing == null) {
            throw new Exception("Bill not found");
        }

        return billDAO.update(bill);
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Check if bill exists
        Bill bill = billDAO.findById(id);
        if (bill == null) {
            throw new Exception("Bill not found");
        }

        return billDAO.delete(id);
    }

    @Override
    public Bill findById(int id) throws Exception {
        Bill bill = billDAO.findById(id);
        if (bill == null) {
            throw new Exception("Bill not found");
        }
        return bill;
    }

    @Override
    public List<Bill> findAll() throws Exception {
        return billDAO.findAll();
    }

    @Override
    public boolean validate(Bill bill) {
        if (bill == null) {
            return false;
        }
        if (bill.getCustomerId() <= 0) {
            return false;
        }
        if (bill.getBillItems() == null || bill.getBillItems().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public Bill createBill(Bill bill, User createdBy) throws Exception {
        // Set bill number and created by
        bill.setBillNumber(Bill.generateBillNumber());
        bill.setCreatedBy(createdBy);
        bill.setCreatedById(createdBy.getId());
        bill.setBillDate(new Date(System.currentTimeMillis()));

        // Validate bill
        if (!validate(bill)) {
            throw new IllegalArgumentException("Invalid bill data");
        }

        // Validate stock availability for all items
        for (BillItem billItem : bill.getBillItems()) {
            Item item = itemDAO.findById(billItem.getItemId());
            if (item == null) {
                throw new Exception("Item not found: " + billItem.getItemId());
            }

            if (!item.hasEnoughStock(billItem.getQuantity())) {
                throw new Exception("Insufficient stock for item: " + item.getName() +
                        ". Available: " + item.getStockQuantity() + ", Required: " + billItem.getQuantity());
            }

            // Set item details in bill item
            billItem.setItem(item);
            billItem.setUnitPrice(item.getUnitPrice());
            billItem.calculateTotalPrice();
        }

        // Calculate total amount
        bill.calculateTotalAmount();

        // Save bill with items
        Bill savedBill = billDAO.saveWithItems(bill);

        // Update stock quantities
        for (BillItem billItem : bill.getBillItems()) {
            Item item = itemDAO.findById(billItem.getItemId());
            int newStock = item.getStockQuantity() - billItem.getQuantity();
            itemDAO.updateStock(item.getId(), newStock);
        }

        return savedBill;
    }

    @Override
    public List<Bill> getBillsByCustomer(int customerId) throws Exception {
        return billDAO.findByCustomerId(customerId);
    }

    @Override
    public List<Bill> getBillsByDateRange(String startDate, String endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date range cannot be null");
        }

        return billDAO.findByDateRange(startDate, endDate);
    }

    @Override
    public List<BillItem> getBillItems(int billId) throws Exception {
        return billDAO.getBillItems(billId);
    }

    @Override
    public double calculateTotalRevenue() throws Exception {
        List<Bill> allBills = billDAO.findAll();
        double totalRevenue = 0;

        for (Bill bill : allBills) {
            totalRevenue += bill.getTotalAmount();
        }

        return totalRevenue;
    }

    @Override
    public Map<String, Object> getSalesSummary() throws Exception {
        // Using HashMap to store sales summary
        Map<String, Object> summary = new HashMap<>();

        List<Bill> allBills = billDAO.findAll();

        // Calculate various metrics
        int totalBills = allBills.size();
        double totalRevenue = 0;
        double highestBillAmount = 0;
        double lowestBillAmount = Double.MAX_VALUE;

        // Item sales count map
        Map<String, Integer> itemSalesCount = new HashMap<>();

        for (Bill bill : allBills) {
            totalRevenue += bill.getTotalAmount();

            if (bill.getTotalAmount() > highestBillAmount) {
                highestBillAmount = bill.getTotalAmount();
            }

            if (bill.getTotalAmount() < lowestBillAmount) {
                lowestBillAmount = bill.getTotalAmount();
            }

            // Get bill items to count item sales
            List<BillItem> items = billDAO.getBillItems(bill.getId());
            for (BillItem item : items) {
                String itemName = item.getItem() != null ? item.getItem().getName() : "Unknown";
                itemSalesCount.put(itemName,
                        itemSalesCount.getOrDefault(itemName, 0) + item.getQuantity());
            }
        }

        // Calculate average bill amount
        double averageBillAmount = totalBills > 0 ? totalRevenue / totalBills : 0;


        String bestSellingItem = "";
        int maxSales = 0;
        for (Map.Entry<String, Integer> entry : itemSalesCount.entrySet()) {
            if (entry.getValue() > maxSales) {
                maxSales = entry.getValue();
                bestSellingItem = entry.getKey();
            }
        }

        // Populate summary map
        summary.put("totalBills", totalBills);
        summary.put("totalRevenue", totalRevenue);
        summary.put("averageBillAmount", averageBillAmount);
        summary.put("highestBillAmount", highestBillAmount);
        summary.put("lowestBillAmount", lowestBillAmount == Double.MAX_VALUE ? 0 : lowestBillAmount);
        summary.put("bestSellingItem", bestSellingItem);
        summary.put("bestSellingItemCount", maxSales);
        summary.put("uniqueItemsSold", itemSalesCount.size());

        return summary;
    }

}