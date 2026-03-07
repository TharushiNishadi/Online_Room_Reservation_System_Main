package com.tharushinis.Ocean_view_resort.controller;

import com.tharushinis.Ocean_view_resort.model.*;
import com.tharushinis.Ocean_view_resort.service.BillService;
import com.tharushinis.Ocean_view_resort.service.CustomerService;
import com.tharushinis.Ocean_view_resort.service.ItemService;
import com.tharushinis.Ocean_view_resort.service.impl.BillServiceImpl;
import com.tharushinis.Ocean_view_resort.service.impl.CustomerServiceImpl;
import com.tharushinis.Ocean_view_resort.service.impl.ItemServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BillServlet" , urlPatterns = {"/bill"})
public class BillServlet extends HttpServlet {
    private BillService billService;
    private CustomerService customerService;
    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        billService = new BillServiceImpl();
        customerService = new CustomerServiceImpl();
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("new")) {
                showNewBillForm(request, response);
            } else if (action.equals("list")) {
                listBills(request, response);
            } else if (action.equals("view")) {
                viewBill(request, response);
            } else {
                showNewBillForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            showNewBillForm(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action != null && action.equals("create")) {
                createBill(request, response);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            showNewBillForm(request, response);
        }
    }

    private void showNewBillForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load customers and items for selection
            List<Customer> customers = customerService.findAll();
            List<Item> availableItems = itemService.getAvailableItems();

            request.setAttribute("customers", customers);
            request.setAttribute("items", availableItems);
            request.getRequestDispatcher("/jsp/bill-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading data: " + e.getMessage());
            request.getRequestDispatcher("/jsp/bill-form.jsp").forward(request, response);
        }
    }

    private void listBills(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Bill> bills = billService.findAll();
            request.setAttribute("bills", bills);
            request.setAttribute("totalBills", bills.size());
            request.getRequestDispatcher("/jsp/bill-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading bills: " + e.getMessage());
            request.getRequestDispatcher("/jsp/bill-list.jsp").forward(request, response);
        }
    }

    private void viewBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Bill bill = billService.findById(id);

            if (bill != null) {
                // Load bill items
                List<BillItem> billItems = billService.getBillItems(id);
                bill.setBillItems(billItems);

                request.setAttribute("bill", bill);
                request.getRequestDispatcher("/jsp/bill-view.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/bill?action=list&error=Bill not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/bill?action=list&error=" + e.getMessage());
        }
    }

    private void createBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get customer ID
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            Customer customer = customerService.findById(customerId);

            // Get current user from session
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            // Create new bill
            Bill bill = new Bill();
            bill.setCustomer(customer);
            bill.setCustomerId(customerId);

            // Get selected items and quantities
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");

            if (itemIds == null || itemIds.length == 0) {
                throw new Exception("Please select at least one item");
            }

            List<BillItem> billItems = new ArrayList<>();

            for (int i = 0; i < itemIds.length; i++) {
                if (itemIds[i] != null && !itemIds[i].isEmpty() &&
                        quantities[i] != null && !quantities[i].isEmpty()) {

                    int itemId = Integer.parseInt(itemIds[i]);
                    int quantity = Integer.parseInt(quantities[i]);

                    if (quantity > 0) {
                        Item item = itemService.findById(itemId);
                        BillItem billItem = new BillItem(item, quantity);
                        billItems.add(billItem);
                    }
                }
            }

            if (billItems.isEmpty()) {
                throw new Exception("Please add at least one item with quantity");
            }

            bill.setBillItems(billItems);

            // Create bill
            Bill createdBill = billService.createBill(bill, currentUser);

            // Redirect to view the created bill
            response.sendRedirect(request.getContextPath() +
                    "/bill?action=view&id=" + createdBill.getId() + "&success=Bill created successfully");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format. Please check your input.");
            showNewBillForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            showNewBillForm(request, response);
        }
    }

}
