package com.tharushinis.Ocean_view_resort.service;


import com.tharushinis.Ocean_view_resort.model.Bill;
import com.tharushinis.Ocean_view_resort.model.BillItem;
import com.tharushinis.Ocean_view_resort.model.User;

import java.util.List;
import java.util.Map;


public interface BillService extends BaseService<Bill> {


    Bill createBill(Bill bill, User createdBy) throws Exception;


    List<Bill> getBillsByCustomer(int customerId) throws Exception;


    List<Bill> getBillsByDateRange(String startDate, String endDate) throws Exception;


    List<BillItem> getBillItems(int billId) throws Exception;

    double calculateTotalRevenue() throws Exception;

    Map<String, Object> getSalesSummary() throws Exception;
}