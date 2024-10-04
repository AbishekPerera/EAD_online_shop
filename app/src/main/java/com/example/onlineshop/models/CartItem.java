package com.example.onlineshop.models;

import java.util.List;

public class CartItem {
    private String productId;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int inventoryCount;
    private List<String> images;

    public CartItem(String productId, String productName, double productPrice, int productQuantity, int inventoryCount, List<String> images) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.inventoryCount = inventoryCount;
        this.images = images;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductNameE() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public List<String> getImagesE() {
        return images;
    }
}
