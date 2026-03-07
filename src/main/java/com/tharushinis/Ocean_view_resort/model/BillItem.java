package com.tharushinis.Ocean_view_resort.model;




/** * BillItem model class for individual items in a bill ----- >*/
public class BillItem {
    private int id;
    private int billId;
    private Item item;
    private int itemId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    // Constructors
    public BillItem() {
    }

    public BillItem(Item item , int quantity){
        this.item = item;
        this.itemId = item.getId();
        this.quantity = quantity;
        this.unitPrice = item.getUnitPrice();
        this.totalPrice = calculateTotalPrice();

    }
    public double calculateTotalPrice() {
        this.totalPrice = this.unitPrice * this.quantity;
        return this.totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
