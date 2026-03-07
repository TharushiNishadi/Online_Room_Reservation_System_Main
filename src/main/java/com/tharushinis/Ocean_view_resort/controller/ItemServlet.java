package com.tharushinis.Ocean_view_resort.controller;

import com.tharushinis.Ocean_view_resort.model.Item;
import com.tharushinis.Ocean_view_resort.service.ItemService;
import com.tharushinis.Ocean_view_resort.service.impl.ItemServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ItemServlet" , urlPatterns = {"/item"})
public class ItemServlet extends HttpServlet {

    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listItems(request, response);
            } else if (action.equals("add")) {
                showAddForm(request, response);
            } else if (action.equals("edit")) {
                showEditForm(request, response);
            } else if (action.equals("delete")) {
                deleteItem(request, response);
            } else {
                listItems(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            listItems(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action != null && action.equals("save")) {
                saveItem(request, response);
            } else if (action != null && action.equals("update")) {
                updateItem(request, response);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void listItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Item> items = itemService.findAll();
            List<Item> lowStockItems = itemService.getLowStockItems();

            request.setAttribute("items", items);
            request.setAttribute("totalItems", items.size());
            request.setAttribute("lowStockCount", lowStockItems.size());
            request.getRequestDispatcher("/jsp/item-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading items: " + e.getMessage());
            request.getRequestDispatcher("/jsp/item-list.jsp").forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Item item = itemService.findById(id);
            request.setAttribute("item", item);
            request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading item: " + e.getMessage());
            listItems(request, response);
        }
    }

    private void saveItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Item item = new Item();

            String itemName = request.getParameter("itemName");
            if (itemName == null || itemName.trim().isEmpty()) {
                request.setAttribute("error", "Invalid item data: Item Name is required.");
                request.setAttribute("item", extractItemFromRequest(request));
                request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
                return;
            }

            item.setName(itemName.trim());
            item.setDescription(request.getParameter("description"));
            item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
            item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));

            itemService.save(item);
            response.sendRedirect(request.getContextPath() + "/item?success=Item added successfully");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format. Please check price and quantity.");
            request.setAttribute("item", extractItemFromRequest(request));
            request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error saving item: " + e.getMessage());
            request.setAttribute("item", extractItemFromRequest(request));
            request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Item item = new Item();
            item.setId(Integer.parseInt(request.getParameter("id")));

            String itemName = request.getParameter("itemName");
            if (itemName == null || itemName.trim().isEmpty()) {
                request.setAttribute("error", "Invalid item data: Item Name is required.");
                request.setAttribute("item", extractItemFromRequest(request));
                request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
                return;
            }

            item.setName(itemName.trim());
            item.setDescription(request.getParameter("description"));
            item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
            item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));

            boolean updated = itemService.update(item);
            if (!updated) {
                request.setAttribute("error", "Update failed: item not found.");
                request.setAttribute("item", item);
                request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/item?success=Item updated successfully");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format. Please check price and quantity.");
            request.setAttribute("item", extractItemFromRequest(request));
            request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating item: " + e.getMessage());
            request.setAttribute("item", extractItemFromRequest(request));
            request.getRequestDispatcher("/jsp/item-form.jsp").forward(request, response);
        }
    }
    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            itemService.delete(id);
            response.sendRedirect(request.getContextPath() + "/item?success=Item deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/item?error=" + e.getMessage());
        }
    }

    private Item extractItemFromRequest(HttpServletRequest request) {
        Item item = new Item();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            item.setId(Integer.parseInt(idStr));
        }
        item.setName(request.getParameter("itemName"));
        item.setDescription(request.getParameter("description"));

        String priceStr = request.getParameter("unitPrice");
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                item.setUnitPrice(Double.parseDouble(priceStr));
            } catch (NumberFormatException e) {
                item.setUnitPrice(0);
            }
        }

        String quantityStr = request.getParameter("stockQuantity");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                item.setStockQuantity(Integer.parseInt(quantityStr));
            } catch (NumberFormatException e) {
                item.setStockQuantity(0);
            }
        }

        return item;
    }
}
