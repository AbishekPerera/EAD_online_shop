package com.example.onlineshop.models;

import java.util.List;

public class ProductItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private String categoryId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionE() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
