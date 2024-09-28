package com.example.onlineshop.models;

public class OrderItem {
    private String orderId;
    private String orderDate;
    private String orderStatus;
    private String productName;
    private int quantity;
    private double price;
    private int imageResource;

    public OrderItem(String orderId, String orderDate, String orderStatus, String productName, int quantity, double price, int imageResource) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageResource = imageResource;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }
}
