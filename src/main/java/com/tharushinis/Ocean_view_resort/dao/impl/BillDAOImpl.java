package com.tharushinis.Ocean_view_resort.dao.impl;


import com.tharushinis.Ocean_view_resort.dao.BillDAO;

import com.tharushinis.Ocean_view_resort.dao.CustomerDAO;
import com.tharushinis.Ocean_view_resort.dao.ItemDAO;
import com.tharushinis.Ocean_view_resort.dao.UserDAO;
import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;
import com.tharushinis.Ocean_view_resort.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill DAO Implementation
 * Demonstrates transaction management for bill and bill items
 */
public class BillDAOImpl implements BillDAO {
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private UserDAO userDAO;

    public BillDAOImpl() {
        this.customerDAO = new CustomerDAOImpl();
        this.itemDAO = new ItemDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public Bill save(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (bill_number, customer_id, total_amount, bill_date, created_by) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bill.getBillNumber());
            stmt.setInt(2, bill.getCustomerId());
            stmt.setDouble(3, bill.getTotalAmount());
            stmt.setDate(4, bill.getBillDate());
            stmt.setInt(5, bill.getCreatedById());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bill.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return bill;
        }
    }

    @Override
    public Bill saveWithItems(Bill bill) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Save bill first
            String billSql = "INSERT INTO bills (bill_number, customer_id, total_amount, bill_date, created_by) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, bill.getBillNumber());
                stmt.setInt(2, bill.getCustomerId());
                stmt.setDouble(3, bill.getTotalAmount());
                stmt.setDate(4, bill.getBillDate());
                stmt.setInt(5, bill.getCreatedById());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            bill.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            }

            // Save bill items
            String itemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(itemSql)) {
                for (BillItem item : bill.getBillItems()) {
                    stmt.setInt(1, bill.getId());
                    stmt.setInt(2, item.getItemId());
                    stmt.setInt(3, item.getQuantity());
                    stmt.setDouble(4, item.getUnitPrice());
                    stmt.setDouble(5, item.getTotalPrice());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit(); // Commit transaction
            return bill;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean update(Bill bill) throws SQLException {
        String sql = "UPDATE bills SET customer_id = ?, total_amount = ?, bill_date = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getCustomerId());
            stmt.setDouble(2, bill.getTotalAmount());
            stmt.setDate(3, bill.getBillDate());
            stmt.setInt(4, bill.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM bills WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Bill findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM bills WHERE id = ?";
        Bill bill = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bill = extractBillFromResultSet(rs);
                }
            }
        }

        // Only after ResultSet and Statement are closed:
        if (bill != null) {
            bill.setCustomer(customerDAO.findById(bill.getCustomerId()));
            bill.setCreatedBy(userDAO.findById(bill.getCreatedById()));
            bill.setBillItems(getBillItems(bill.getId()));
        }
        return bill;
    }

    @Override
    public List<Bill> findAll() throws SQLException {
        String sql = "SELECT * FROM bills ORDER BY bill_date DESC, id DESC";
        List<Bill> bills = new ArrayList<>();

        // First: Read all bills from result set
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bill bill = extractBillFromResultSet(rs);
                bills.add(bill);
            }
        }


        for (Bill bill : bills) {
            bill.setCustomer(customerDAO.findById(bill.getCustomerId()));
            bill.setCreatedBy(userDAO.findById(bill.getCreatedById()));

        }

        return bills;
    }

    @Override
    public List<Bill> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM bills WHERE customer_id = ? ORDER BY bill_date DESC";
        List<Bill> bills = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = extractBillFromResultSet(rs);
                    bill.setCustomer(customerDAO.findById(bill.getCustomerId()));
                    bill.setCreatedBy(userDAO.findById(bill.getCreatedById()));
                    bills.add(bill);
                }
            }
        }
        return bills;
    }

    @Override
    public List<Bill> findByDateRange(String startDate, String endDate) throws SQLException {
        String sql = "SELECT * FROM bills WHERE bill_date BETWEEN ? AND ? ORDER BY bill_date DESC";
        List<Bill> bills = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = extractBillFromResultSet(rs);
                    bill.setCustomer(customerDAO.findById(bill.getCustomerId()));
                    bill.setCreatedBy(userDAO.findById(bill.getCreatedById()));
                    bills.add(bill);
                }
            }
        }
        return bills;
    }

    @Override
    public List<BillItem> getBillItems(int billId) throws SQLException {
        String sql = "SELECT * FROM bill_items WHERE bill_id = ?";
        List<BillItem> items = new ArrayList<>();


        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setId(rs.getInt("id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setItemId(rs.getInt("item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setTotalPrice(rs.getDouble("total_price"));
                    // Do NOT call itemDAO.findById here!
                    items.add(item);
                }
            }
        }

        for (BillItem item : items) {
            item.setItem(itemDAO.findById(item.getItemId()));
        }
        return items;
    }

    // Helper method to extract Bill from ResultSet
    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setId(rs.getInt("id"));
        bill.setBillNumber(rs.getString("bill_number"));
        bill.setCustomerId(rs.getInt("customer_id"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setBillDate(rs.getDate("bill_date"));
        bill.setCreatedById(rs.getInt("created_by"));
        bill.setCreatedAt(rs.getTimestamp("created_at"));
        return bill;
    }
}