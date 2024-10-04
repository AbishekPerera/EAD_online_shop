package com.example.onlineshop.models;

public class OrderItem {
    private String itemName;
    private int quantity;
    private double price;
    private int imageResource;
    private String status; // Added status field

    public OrderItem(String itemName, int quantity, double price, int imageResource, String status) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.imageResource = imageResource;
        this.status = status; // Initialize status
    }

    // Getters
    public String getItemNameE() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceE() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getStatus() {
        return status; // Return the status of the item
    }
}
