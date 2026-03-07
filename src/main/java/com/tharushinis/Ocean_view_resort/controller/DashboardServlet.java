package com.tharushinis.Ocean_view_resort.controller;

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
import java.io.IOException;
import java.util.Map;
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard", "/"})
public class DashboardServlet extends HttpServlet {

    private CustomerService customerService;
    private ItemService itemService;
    private BillService billService;

    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
        itemService = new ItemServiceImpl();
        billService = new BillServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get summary statistics
            int totalCustomers = customerService.getTotalCustomers();
            int totalItems = itemService.findAll().size();
            int totalBills = billService.findAll().size();
            double totalRevenue = billService.calculateTotalRevenue();

            // Get sales summary
            Map<String, Object> salesSummary = billService.getSalesSummary();

            // Get low stock items
            int lowStockCount = itemService.getLowStockItems().size();

            // Set attributes
            request.setAttribute("totalCustomers", totalCustomers);
            request.setAttribute("totalItems", totalItems);
            request.setAttribute("totalBills", totalBills);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("salesSummary", salesSummary);
            request.setAttribute("lowStockCount", lowStockCount);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading dashboard data: " + e.getMessage());
        }

        // Forward to dashboard page
        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
