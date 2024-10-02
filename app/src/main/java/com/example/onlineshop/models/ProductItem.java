package com.example.onlineshop.models;

import java.util.List;

public class ProductItem {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private double price;
    private boolean isActive;
    private int inventoryCount;
    private int lowStockAlert;
    private List<String> images;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionE() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public double getPriceE() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }
}
