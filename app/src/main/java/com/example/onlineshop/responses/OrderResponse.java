package com.example.onlineshop.responses;

import java.util.List;

public class OrderResponse {
    private String id;
    private String phoneNumber;
    private String deliveryAddress;
    private double totalPrice;
    private String status;
    private boolean isCancelRequested;
    private String note;
    private List<OrderItemResponse> items;

    // Getters for each field
    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCancelRequested() {
        return isCancelRequested;
    }

    public String getNote() {
        return note;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    // Inner class representing the response of each order item
    public static class OrderItemResponse {
        private String id;
        private String productId;
        private String name;
        private int quantity;
        private double price;
        private String status;
        private List<String> images;

        // Getters for each field
        public String getId() {
            return id;
        }

        public String getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public String getStatus() {
            return status;
        }

        public List<String> getImages() {
            return images;
        }
    }
}
