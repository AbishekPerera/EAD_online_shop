package com.example.onlineshop.models;

public class CategoryItem {
    private String categoryName;
    private int categoryImageResId;

    public CategoryItem(String categoryName, int categoryImageResId) {
        this.categoryName = categoryName;
        this.categoryImageResId = categoryImageResId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryImageResId() {
        return categoryImageResId;
    }
}
