package com.tharushinis.Ocean_view_resort.model;

public class Item extends BaseModel {

    private String itemName;
    private String description;
    private double unitPrice;
    private int stockQuantity;


    @Override
    public String getDisplayName() {
        return null;
    }

    public Item() {
    }

    public Item(String name, String description, double unitPrice, int stockQuantity) {
        super();
        this.itemName = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                "name='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
