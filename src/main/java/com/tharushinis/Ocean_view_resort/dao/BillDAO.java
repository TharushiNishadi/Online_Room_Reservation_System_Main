package com.tharushinis.Ocean_view_resort.dao;

import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;


import java.sql.SQLException;
import java.util.List;


/** * Bill DAO interface extending Generic DAO */
public interface BillDAO extends GenericDAO<Bill, Integer> {


    Bill saveWithItems(Bill bill) throws SQLException;


    List<Bill> findByCustomerId(int customerId) throws SQLException;


    List<Bill> findByDateRange(String startDate, String endDate) throws SQLException;


    List<BillItem> getBillItems(int billId) throws SQLException;
}