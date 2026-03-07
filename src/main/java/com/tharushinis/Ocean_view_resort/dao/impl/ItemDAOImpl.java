package com.tharushinis.Ocean_view_resort.dao.impl;

import com.tharushinis.Ocean_view_resort.dao.ItemDAO;
import com.tharushinis.Ocean_view_resort.model.Item;
import com.tharushinis.Ocean_view_resort.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public Item save(Item item) throws SQLException {
        // ✅ column name is item_name (NOT name)
        String sql = "INSERT INTO items (item_name, description, unit_price, stock_quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // ✅ use getName() or getItemName() based on your model
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getUnitPrice());
            stmt.setInt(4, item.getStockQuantity());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
        return item;
    }

    @Override
    public boolean update(Item item) throws SQLException {
        // ✅ correct UPDATE syntax (NO INTO, NO VALUES)
        String sql = "UPDATE items SET item_name = ?, description = ?, unit_price = ?, stock_quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getUnitPrice());
            stmt.setInt(4, item.getStockQuantity());
            stmt.setInt(5, item.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Item findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractItemFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Item> findAll() throws SQLException {
        // ✅ ORDER BY item_name (NOT name)
        String sql = "SELECT * FROM items ORDER BY item_name";
        List<Item> items = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(extractItemFromResultSet(rs));
            }
        }
        return items;
    }

    @Override
    public List<Item> findByNamePattern(String namePattern) throws SQLException {
        // ✅ WHERE item_name LIKE ... ORDER BY item_name
        String sql = "SELECT * FROM items WHERE item_name LIKE ? ORDER BY item_name";
        List<Item> items = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + namePattern + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractItemFromResultSet(rs));
                }
            }
        }
        return items;
    }

    @Override
    public List<Item> findAvailableItems() throws SQLException {
        // ✅ ORDER BY item_name
        String sql = "SELECT * FROM items WHERE stock_quantity > 0 ORDER BY item_name";
        List<Item> items = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(extractItemFromResultSet(rs));
            }
        }
        return items;
    }

    @Override
    public boolean updateStock(int itemId, int quantity) throws SQLException {
        String sql = "UPDATE items SET stock_quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);

            return stmt.executeUpdate() > 0;
        }
    }

    private Item extractItemFromResultSet(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));

        // ✅ column is item_name
        item.setName(rs.getString("item_name"));

        item.setDescription(rs.getString("description"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setStockQuantity(rs.getInt("stock_quantity"));
        item.setCreatedAt(rs.getTimestamp("created_at"));

        // ✅ your setter name was setUpdateAt (typo?) keep as you have it
        item.setUpdateAt(rs.getTimestamp("updated_at"));
        return item;
    }
}