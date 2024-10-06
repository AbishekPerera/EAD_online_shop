package com.example.onlineshop.models;

import java.util.List;

public class ProductItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private boolean isActive;        // New field to track if the product is active
    private int inventoryCount;      // New field to track inventory count
    private int lowStockAlert;       // New field for low stock alert threshold
    private Category category;       // Updated to include a category object
    private Vendor vendor;           // Updated to include a vendor object

    // Getters and setters for the new objects
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public int getLowStockAlert() {
        return lowStockAlert;
    }

    public Category getCategory() {
        return category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    // Setter methods (if required)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public void setLowStockAlert(int lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
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

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }

    // Inner class to represent the vendor
    public static class Vendor {
        private String id;
        private String name;
        private Ratings ratings;
        private List<Comment> comments;  // Updated to include a list of comments

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Ratings getRatings() {
            return ratings;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRatings(Ratings ratings) {
            this.ratings = ratings;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
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

            public void setAverage(float average) {
                this.average = average;
            }

            public void setTotalReviews(int totalReviews) {
                this.totalReviews = totalReviews;
            }
        }

        // Inner class to represent a comment
        public static class Comment {
            private String customerId;
            private String comment;
            private String createdAt;

            public String getCustomerId() {
                return customerId;
            }

            public String getComment() {
                return comment;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }
        }
    }
}
