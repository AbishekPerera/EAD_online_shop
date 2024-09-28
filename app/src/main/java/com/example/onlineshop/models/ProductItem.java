package com.example.onlineshop.models;

public class ProductItem {
    private String name;
    private String category;
    private double price;
    private float rating;
    private int imageResId;

    public ProductItem(String name, String category, double price, float rating, int imageResId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.imageResId = imageResId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPriceE() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResId() {
        return imageResId;
    }
}
