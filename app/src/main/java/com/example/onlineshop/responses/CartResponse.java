package com.example.onlineshop.responses;

import java.util.List;

public class CartResponse {
    private String id;
    private double totalPrice;
    private List<CartItemResponse> items;

    public String getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public static class CartItemResponse {
        private String productId;
        private String name;
        private int quantity;
        private int inventoryCount;
        private double price;
        private List<String> images;

        public String getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getInventoryCount() {
            return inventoryCount;
        }

        public double getPriceE() {
            return price;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
