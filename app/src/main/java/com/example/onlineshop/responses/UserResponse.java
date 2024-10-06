package com.example.onlineshop.responses;

public class UserResponse {
    private String id;
    private String email;
    private String username;
    private String role;
    private boolean isActive;
    private boolean isPending;

    // Getters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isPending() {
        return isPending;
    }
}
