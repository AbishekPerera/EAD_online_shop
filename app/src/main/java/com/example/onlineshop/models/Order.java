package com.example.onlineshop.models;

import java.util.List;

public class Order {
    private String id;
    private String phoneNumber;
    private String address;
    private String totalPrice;
    private String status;
    private List<OrderItem> orderItems; // List of items in this order
    private boolean isCancelRequested;
    private String note;

    public Order(String id,String phoneNumber, String address, String totalPrice, String status, List<OrderItem> orderItems, boolean isCancelRequested, String note) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderItems = orderItems;
        this.isCancelRequested = isCancelRequested;
        this.note = note;
    }

    // Getters
    public String getId() { return id; }
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

    public boolean isCancelRequested() {
        return isCancelRequested;
    }

    public String getNote() {
        return note;
    }
}
