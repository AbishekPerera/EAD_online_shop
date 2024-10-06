package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.models.ProductItem;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewItemActivity extends AppCompatActivity {

    private ImageView productImage, backButton;
    private TextView productName, productDescription, productPrice, itemQuantity, increaseQuantity, decreaseQuantity,
            totalPrice, vendorNameViewProduct, vendorRatingViewProduct;
    private Button addToCartButton;
    private LinearLayout reviewSectionViewProduct;
    private int quantity = 1;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Initialize views
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        totalPrice = findViewById(R.id.totalPrice);
        itemQuantity = findViewById(R.id.itemQuantity);
        increaseQuantity = findViewById(R.id.increaseQuantity);
        decreaseQuantity = findViewById(R.id.decreaseQuantity);
        addToCartButton = findViewById(R.id.addToCartButton);
        backButton = findViewById(R.id.backButton);
        vendorNameViewProduct = findViewById(R.id.vendorNameViewProduct);
        vendorRatingViewProduct = findViewById(R.id.vendorRatingViewProduct);
        reviewSectionViewProduct = findViewById(R.id.reviewSectionViewProduct);

        // Initialize API service
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Get productId from intent
        String productId = getIntent().getStringExtra("productId");

        // Fetch product details from the API
        fetchProductDetails(productId);

        // Handle quantity increase
        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            itemQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Handle quantity decrease
        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                itemQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener(v -> {
            addItemToCart(quantity, productId); // Add the selected quantity to the cart
        });
    }

    // Fetch product details from the API
    private void fetchProductDetails(String productId) {
        Call<ProductItem> call = apiService.getProductDetails(productId);
        call.enqueue(new Callback<ProductItem>() {
            @Override
            public void onResponse(Call<ProductItem> call, Response<ProductItem> response) {
                if (response.isSuccessful()) {
                    ProductItem product = response.body();
                    if (product != null) {
                        updateUIWithProductDetails(product);
                    }
                } else {
                    Toast.makeText(ViewItemActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductItem> call, Throwable t) {
                Toast.makeText(ViewItemActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ViewItemActivity", "API error", t);
            }
        });
    }

    // Update the UI with the product details
    private void updateUIWithProductDetails(ProductItem product) {
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText("Price: $" + product.getPrice());
        totalPrice.setText("Total: $" + product.getPrice() * quantity);

        // Load the first image
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(this)
                    .load(product.getImages().get(0))
                    .into(productImage);
        }

        // Update vendor info
        if (product.getVendor() != null) {
            vendorNameViewProduct.setText("Vendor: " + product.getVendor().getName());
            vendorRatingViewProduct.setText("Average Rating: " + product.getVendor().getRatings().getAverage() + "/5");

            // Display user comments
            List<ProductItem.Vendor.Comment> comments = product.getVendor().getComments();
            displayUserComments(comments);
        }
    }

    // Display user comments in the review section
    // Display user comments in the review section
    private void displayUserComments(List<ProductItem.Vendor.Comment> comments) {
        // Clear previous reviews
        reviewSectionViewProduct.removeAllViews();

        // Get logged-in user's ID and username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String loggedUserId = sharedPreferences.getString("userId", ""); // Retrieve userId
        String loggedUserName = sharedPreferences.getString("userName", "Anonymous"); // Retrieve userName

        LayoutInflater inflater = LayoutInflater.from(this);

        for (ProductItem.Vendor.Comment comment : comments) {
            // Inflate a new view for each comment
            View reviewView = inflater.inflate(R.layout.item_review, reviewSectionViewProduct, false);

            TextView commentText = reviewView.findViewById(R.id.commentText);
            TextView commentDate = reviewView.findViewById(R.id.commentDate);
            TextView commenterName = reviewView.findViewById(R.id.commenterName);

            // Set the comment and date
            commentText.setText(comment.getComment());
            commentDate.setText(comment.getCreatedAt().toString());

            // Check if the comment was made by the logged-in user
            if (comment.getCustomerId().equals(loggedUserId)) {
                // Show "userName (by You)" if the logged-in user made the comment
                commenterName.setText(loggedUserName + " (by You)");
            } else {
                // Show "Anonymous" if it's a comment from another user
                commenterName.setText("Anonymous");
            }

            // Add the review view to the review section
            reviewSectionViewProduct.addView(reviewView);
        }
    }


    // Update the total price when quantity changes
    private void updateTotalPrice() {
        double pricePerItem = Double.parseDouble(productPrice.getText().toString().replace("Price: $", ""));
        double total = pricePerItem * quantity;
        totalPrice.setText("Total: $" + total);
    }

    // Add items to cart based on the selected quantity, handling sequential requests
    private void addItemToCart(int quantity, String productId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Start adding items one by one
        addItemToCartRecursive(token, productId, quantity, 0); // Start with 0 index
    }

    // Recursive method to handle the API calls one by one
    private void addItemToCartRecursive(String token, String productId, int totalQuantity, int currentCount) {
        if (currentCount < totalQuantity) {
            Call<Void> call = apiService.addItemToCart("Bearer " + token, productId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Continue to the next iteration
                        addItemToCartRecursive(token, productId, totalQuantity, currentCount + 1);
                    } else {
                        Toast.makeText(ViewItemActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ViewItemActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // All items have been added
            Toast.makeText(ViewItemActivity.this, "All items added to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
