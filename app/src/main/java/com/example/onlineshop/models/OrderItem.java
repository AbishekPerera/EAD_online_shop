package com.example.onlineshop.models;

import java.util.List;

public class OrderItem {
    private String itemName;
    private int quantity;
    private double price;
    private List<String> images; // Store list of image URLs
    private String status;

    public OrderItem(String itemName, int quantity, double price, List<String> images, String status) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.images = images; // Initialize with image URLs
        this.status = status;
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

    public List<String> getImages() {
        return images; // Return list of images
    }

    public String getStatus() {
        return status;
    }
}
