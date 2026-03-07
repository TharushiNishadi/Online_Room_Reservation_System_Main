package com.tharushinis.Ocean_view_resort.service;

import com.tharushinis.Ocean_view_resort.dao.BillDAO;
import com.tharushinis.Ocean_view_resort.dao.impl.BillDAOImpl;
import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;

import java.sql.SQLException;
import java.util.*;

public class ChartDataService {
    private BillDAO billDAO;

    public ChartDataService() {
        this.billDAO = new BillDAOImpl();
    }


    public Map<String, Double> getMonthlySalesData() throws SQLException {
        Map<String, Double> monthlyData = new LinkedHashMap<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // Initialize all months with 0
        for (String month : months) {
            monthlyData.put(month, 0.0);
        }

        // Get all bills for current year
        List<Bill> bills = billDAO.findAll();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        for (Bill bill : bills) {
            cal.setTime(bill.getBillDate());
            if (cal.get(Calendar.YEAR) == currentYear) {
                int monthIndex = cal.get(Calendar.MONTH);
                String monthName = months[monthIndex];
                monthlyData.put(monthName,
                        monthlyData.get(monthName) + bill.getTotalAmount());
            }
        }

        return monthlyData;
    }

    /**
     * Get daily sales for last 7 days
     * @return Map with dates and sales totals
     */
    public Map<String, Double> getWeeklySalesData() throws SQLException {
        Map<String, Double> weeklyData = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();

        // Get last 7 days
        for (int i = 6; i >= 0; i--) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i);
            String dateKey = String.format("%02d/%02d",
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1);
            weeklyData.put(dateKey, 0.0);
        }

        // Get bills from last 7 days
        List<Bill> bills = billDAO.findAll();
        Calendar today = Calendar.getInstance();

        for (Bill bill : bills) {
            cal.setTime(bill.getBillDate());
            long daysDiff = (today.getTimeInMillis() - cal.getTimeInMillis())
                    / (1000 * 60 * 60 * 24);

            if (daysDiff >= 0 && daysDiff < 7) {
                String dateKey = String.format("%02d/%02d",
                        cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1);
                if (weeklyData.containsKey(dateKey)) {
                    weeklyData.put(dateKey,
                            weeklyData.get(dateKey) + bill.getTotalAmount());
                }
            }
        }

        return weeklyData;
    }

    /**
     * Get top selling items data
     * @param limit Number of top items to return
     * @return List of maps containing item names and quantities
     */
    public List<Map<String, Object>> getTopSellingItems(int limit) throws SQLException {
        List<Map<String, Object>> topItems = new ArrayList<>();
        Map<String, Integer> itemSales = new HashMap<>();

        // Aggregate sales by item
        List<Bill> bills = billDAO.findAll();
        for (Bill bill : bills) {
            List<BillItem> billItems = billDAO.getBillItems(bill.getId());
            for (BillItem billItem : billItems) {
                String itemName = billItem.getItem().getName();
                itemSales.put(itemName,
                        itemSales.getOrDefault(itemName, 0) + billItem.getQuantity());
            }
        }

        // Sort by quantity and get top items
        List<Map.Entry<String, Integer>> sortedItems = new ArrayList<>(itemSales.entrySet());
        sortedItems.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(limit, sortedItems.size()); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", sortedItems.get(i).getKey());
            item.put("quantity", sortedItems.get(i).getValue());
            topItems.add(item);
        }

        return topItems;
    }

    /**
     * Get customer distribution data
     * @return Map with customer types and counts
     */
    public Map<String, Integer> getCustomerDistribution() throws SQLException {
        Map<String, Integer> distribution = new HashMap<>();

        // This is a simplified example
        // In real scenario, you might categorize customers based on purchase amount
        List<Bill> bills = billDAO.findAll();
        Map<Integer, Double> customerPurchases = new HashMap<>();

        for (Bill bill : bills) {
            customerPurchases.put(bill.getCustomerId(),
                    customerPurchases.getOrDefault(bill.getCustomerId(), 0.0)
                            + bill.getTotalAmount());
        }

        int regular = 0, premium = 0, vip = 0;

        for (Double amount : customerPurchases.values()) {
            if (amount < 10000) {
                regular++;
            } else if (amount < 50000) {
                premium++;
            } else {
                vip++;
            }
        }

        distribution.put("Regular", regular);
        distribution.put("Premium", premium);
        distribution.put("VIP", vip);

        return distribution;
    }

    /**
     * Convert data to JSON string
     * @param data Map data
     * @return JSON string
     */
    public String toJSON(Map<String, ?> data) {
        StringBuilder json = new StringBuilder("{");
        int count = 0;

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            if (count > 0) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            count++;
        }

        json.append("}");
        return json.toString();
    }
}
