package com.example.onlineshop.models;

import java.util.List;

public class ProductItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private Category category;  // Updated to include a category object
    private Vendor vendor;      // Updated to include a vendor object

    // Getters and setters for the new objects
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionE() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }

    public Category getCategory() {
        return category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    // Inner class to represent the category
    public static class Category {
        private String id;
        private String name;
        private boolean isActive;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isActive() {
            return isActive;
        }
    }

    // Inner class to represent the vendor
    public static class Vendor {
        private String id;
        private String name;
        private Ratings ratings;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Ratings getRatings() {
            return ratings;
        }

        // Inner class to represent the ratings
        public static class Ratings {
            private float average;
            private int totalReviews;

            public float getAverage() {
                return average;
            }

            public int getTotalReviews() {
                return totalReviews;
            }
        }
    }
}
