package com.example.onlineshop.models;

public class CategoryItem {
    private String id;
    private String name;
    private boolean isActive;

    public CategoryItem(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
