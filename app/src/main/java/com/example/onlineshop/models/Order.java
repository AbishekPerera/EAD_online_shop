package com.example.onlineshop.models;

import java.util.List;

public class Order {
    private String phoneNumber;
    private String address;
    private String totalPrice;
    private String status;
    private List<OrderItem> orderItems; // List of items in this order

    public Order(String phoneNumber, String address, String totalPrice, String status, List<OrderItem> orderItems) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderItems = orderItems;
    }

    // Getters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
