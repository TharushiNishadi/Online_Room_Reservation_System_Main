package com.tharushinis.Ocean_view_resort.service.impl;

import com.tharushinis.Ocean_view_resort.dao.ItemDAO;
import com.tharushinis.Ocean_view_resort.dao.impl.ItemDAOImpl;
import com.tharushinis.Ocean_view_resort.model.Item;
import com.tharushinis.Ocean_view_resort.service.ItemService;

import java.util.ArrayList;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    private ItemDAO itemDAO;

    public ItemServiceImpl() {
        this.itemDAO = new ItemDAOImpl();
    }

    @Override
    public Item save(Item item) throws Exception {
        if (!validate(item)) {
            throw new IllegalArgumentException("Invalid item data");
        }

        return itemDAO.save(item);
    }

    @Override
    public boolean update(Item item) throws Exception {
        if (!validate(item)) {
            throw new IllegalArgumentException("Invalid item data");
        }

        // Check if item exists
        Item existing = itemDAO.findById(item.getId());
        if (existing == null) {
            throw new Exception("Item not found");
        }

        return itemDAO.update(item);
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Check if item exists
        Item item = itemDAO.findById(id);
        if (item == null) {
            throw new Exception("Item not found");
        }

        // TODO: Check if item is used in any bills before deleting

        return itemDAO.delete(id);
    }

    @Override
    public Item findById(int id) throws Exception {
        Item item = itemDAO.findById(id);
        if (item == null) {
            throw new Exception("Item not found");
        }
        return item;
    }

    @Override
    public List<Item> findAll() throws Exception {
        return itemDAO.findAll();
    }

    @Override
    public boolean validate(Item item) {
        if (item == null) {
            return false;
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return false;
        }
        if (item.getUnitPrice() < 0) {
            return false;
        }
        if (item.getStockQuantity() < 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Item> searchByName(String namePattern) throws Exception {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return findAll();
        }
        return itemDAO.findByNamePattern(namePattern.trim());
    }

    @Override
    public List<Item> getAvailableItems() throws Exception {
        return itemDAO.findAvailableItems();
    }

    @Override
    public boolean updateStock(int itemId, int quantity) throws Exception {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        // Check if item exists
        Item item = itemDAO.findById(itemId);
        if (item == null) {
            throw new Exception("Item not found");
        }

        return itemDAO.updateStock(itemId, quantity);
    }

    @Override
    public boolean hasSufficientStock(int itemId, int requiredQuantity) throws Exception {
        if (requiredQuantity <= 0) {
            throw new IllegalArgumentException("Required quantity must be positive");
        }

        Item item = itemDAO.findById(itemId);
        if (item == null) {
            throw new Exception("Item not found");
        }

        return item.getStockQuantity() >= requiredQuantity;
    }

    @Override
    public List<Item> getLowStockItems() throws Exception {
        List<Item> allItems = itemDAO.findAll();
        List<Item> lowStockItems = new ArrayList<>();

        // Filter items with stock less than 10
        for (Item item : allItems) {
            if (item.getStockQuantity() < 10) {
                lowStockItems.add(item);
            }
        }

        return lowStockItems;
    }
}