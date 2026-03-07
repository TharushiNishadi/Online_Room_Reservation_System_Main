package com.tharushinis.Ocean_view_resort.service.impl;


import com.tharushinis.Ocean_view_resort.dao.CustomerDAO;
import com.tharushinis.Ocean_view_resort.dao.impl.CustomerDAOImpl;
import com.tharushinis.Ocean_view_resort.model.Customer;
import com.tharushinis.Ocean_view_resort.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;

    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public Customer save(Customer customer) throws Exception {
        if (!validate(customer)) {
            throw new IllegalArgumentException("Invalid customer data");
        }

        // Check if email already exists
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            if (isEmailExists(customer.getEmail(), null)) {
                throw new Exception("Email already exists");
            }
        }

        return customerDAO.save(customer);
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        if (!validate(customer)) {
            throw new IllegalArgumentException("Invalid customer data");
        }

        // Check if customer exists
        Customer existing = customerDAO.findById(customer.getId());
        if (existing == null) {
            throw new Exception("Customer not found");
        }

        // Check if email already exists for another customer
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            if (isEmailExists(customer.getEmail(), customer.getId())) {
                throw new Exception("Email already exists for another customer");
            }
        }

        return customerDAO.update(customer);
    }

    @Override
    public boolean delete(int id) throws Exception {
        // Check if customer exists
        Customer customer = customerDAO.findById(id);
        if (customer == null) {
            throw new Exception("Customer not found");
        }

        // TODO: Check if customer has any bills before deleting

        return customerDAO.delete(id);
    }

    @Override
    public Customer findById(int id) throws Exception {
        Customer customer = customerDAO.findById(id);
        if (customer == null) {
            throw new Exception("Customer not found");
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() throws Exception {
        return customerDAO.findAll();
    }

    @Override
    public boolean validate(Customer customer) {
        if (customer == null) {
            return false;
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return false;
        }
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            // Basic email validation
            if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return false;
            }
        }
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            // Basic phone validation (Sri Lankan format)
            if (!customer.getPhone().matches("^[0-9]{10}$")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Customer> searchByName(String searchTerm) throws Exception {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return customerDAO.searchByName(searchTerm.trim());
    }

    @Override
    public boolean isEmailExists(String email, Integer excludeId) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Customer customer = customerDAO.findByEmail(email.trim());
        if (customer == null) {
            return false;
        }

        // If excludeId is provided, check if it's the same customer
        if (excludeId != null && customer.getId() == excludeId) {
            return false;
        }

        return true;
    }

    @Override
    public int getTotalCustomers() throws Exception {
        List<Customer> customers = customerDAO.findAll();
        return customers.size();
    }
}