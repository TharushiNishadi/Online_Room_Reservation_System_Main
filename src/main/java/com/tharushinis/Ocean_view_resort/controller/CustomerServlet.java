package com.tharushinis.Ocean_view_resort.controller;

import com.tharushinis.Ocean_view_resort.model.Customer;
import com.tharushinis.Ocean_view_resort.service.CustomerService;
import com.tharushinis.Ocean_view_resort.service.impl.CustomerServiceImpl;


import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/customer"})
public class CustomerServlet extends HttpServlet {

public void init() throws ServletException {
super.init();
customerService = new CustomerServiceImpl();
}

    private CustomerService customerService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listCustomers(request, response);
            } else if (action.equals("add")) {
                showAddForm(request, response);
            } else if (action.equals("edit")) {
                showEditForm(request, response);
            } else if (action.equals("delete")) {
                deleteCustomer(request, response);
            } else if (action.equals("search")) {
                searchCustomers(request, response);
            } else {
                listCustomers(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            listCustomers(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action != null && action.equals("save")) {
                saveCustomer(request, response);
            } else if (action != null && action.equals("update")) {
                updateCustomer(request, response);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
        }

    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Customer> customers = customerService.findAll();
            request.setAttribute("customers", customers);
            request.setAttribute("totalCustomers", customers.size());
            request.getRequestDispatcher("/jsp/customer-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading customers: " + e.getMessage());
            request.getRequestDispatcher("/jsp/customer-list.jsp").forward(request, response);
        }
    }

    private void searchCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String searchTerm = request.getParameter("searchTerm");
            List<Customer> customers = customerService.searchByName(searchTerm);
            request.setAttribute("customers", customers);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("totalCustomers", customers.size());
            request.getRequestDispatcher("/jsp/customer-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error searching customers: " + e.getMessage());
            listCustomers(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/customer-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Customer customer = customerService.findById(id);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/jsp/customer-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading customer: " + e.getMessage());
            listCustomers(request, response);
        }
    }

    private void saveCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Customer customer = new Customer();
            customer.setName(request.getParameter("name"));
            customer.setEmail(request.getParameter("email"));
            customer.setPhone(request.getParameter("phone"));
            customer.setAddress(request.getParameter("address"));

            customerService.save(customer);

            response.sendRedirect(request.getContextPath() + "/customer?success=Customer added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.setAttribute("customer", extractCustomerFromRequest(request));
            request.getRequestDispatcher("/jsp/customer-form.jsp").forward(request, response);
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Customer customer = new Customer();
            customer.setId(Integer.parseInt(request.getParameter("id")));
            customer.setName(request.getParameter("name"));
            customer.setEmail(request.getParameter("email"));
            customer.setPhone(request.getParameter("phone"));
            customer.setAddress(request.getParameter("address"));

            customerService.update(customer);

            response.sendRedirect(request.getContextPath() + "/customer?success=Customer updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.setAttribute("customer", extractCustomerFromRequest(request));
            request.getRequestDispatcher("/jsp/customer-form.jsp").forward(request, response);
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            customerService.delete(id);
            response.sendRedirect(request.getContextPath() + "/customer?success=Customer deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/customer?error=" + e.getMessage());
        }
    }

    private Customer extractCustomerFromRequest(HttpServletRequest request) {
        Customer customer = new Customer();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            customer.setId(Integer.parseInt(idStr));
        }
        customer.setName(request.getParameter("name"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));
        customer.setAddress(request.getParameter("address"));
        return customer;
    }
}
