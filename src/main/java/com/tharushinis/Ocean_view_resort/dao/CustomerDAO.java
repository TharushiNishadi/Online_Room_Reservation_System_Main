package com.tharushinis.Ocean_view_resort.dao;


import com.tharushinis.Ocean_view_resort.model.Customer;


import java.sql.SQLException;
import java.util.List;

/** * Customer DAO interface extending Generic DAO */
public interface CustomerDAO extends GenericDAO<Customer, Integer> {


    List<Customer> searchByName(String name) throws SQLException;


    Customer findByEmail(String email) throws SQLException;
}