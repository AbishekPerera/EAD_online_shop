package com.example.onlineshop.models;

public class CartItem {
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int productImageResId;

    public CartItem(String productName, double productPrice, int productQuantity, int productImageResId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImageResId = productImageResId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductImageResId() {
        return productImageResId;
    }
}
