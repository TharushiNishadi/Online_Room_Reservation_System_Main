package com.tharushinis.Ocean_view_resort.service;


import com.tharushinis.Ocean_view_resort.model.Customer;

import java.util.List;

public interface CustomerService extends BaseService<Customer> {


    List<Customer> searchByName(String searchTerm) throws Exception;

    boolean isEmailExists(String email, Integer excludeId) throws Exception;

    int getTotalCustomers() throws Exception;
}