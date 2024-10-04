package com.example.onlineshop.requests;

public class OrderRequest {
    private String phoneNumber;
    private String deliveryAddress;

    public OrderRequest(String phoneNumber, String deliveryAddress) {
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
