package com.example.onlineshop.responses;

import java.util.List;

public class OrderResponse {
    private String id;
    private String phoneNumber;
    private String deliveryAddress;
    private double totalPrice;
    private String status;
    private List<OrderItemResponse> items;

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

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public static class OrderItemResponse {
        private String id;
        private String productId;
        private String name;
        private int quantity;
        private double price;
        private String status;
        private List<String> images;

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
