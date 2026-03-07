package com.tharushinis.Ocean_view_resort.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Bill extends BaseModel {


    private String billNumber;
    private Customer customer;
    private int customerId;
    private double totalAmount;
    private java.sql.Date billDate;
    private User createdBy;
    private int createdById;
    private List<BillItem> billItems; // Using ArrayList collection

    // Constructors
    public Bill() {
        super();
        this.billItems = new ArrayList<>();
        this.billDate = new java.sql.Date(System.currentTimeMillis());
    }

    public Bill(String billNumber, Customer customer, User createdBy) {
        this();
        this.billNumber = billNumber;
        this.customer = customer;
        this.customerId = customer.getId();
        this.createdBy = createdBy;
        this.createdById = createdBy.getId();
    }

    // Implementation of abstract method
    @Override
    public String getDisplayName() {
        return "Bill #" + this.billNumber;
    }

    // Business logic methods
    public void addBillItem(BillItem item) {
        this.billItems.add(item);
        calculateTotalAmount();
    }

    public void removeBillItem(BillItem item) {
        this.billItems.remove(item);
        calculateTotalAmount();
    }

    public void calculateTotalAmount() {
        this.totalAmount = 0;
        for (BillItem item : billItems) {
            this.totalAmount += item.getTotalPrice();
        }
    }

    // Generate unique bill number
    public static String generateBillNumber() {
        return "BILL-" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer.getId();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public java.sql.Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        this.createdById = createdBy.getId();
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
        calculateTotalAmount();
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", billNumber='" + billNumber + '\'' +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", billDate=" + billDate +
                ", createdById=" + createdById +
                ", itemCount=" + billItems.size() +
                '}';
    }
}
