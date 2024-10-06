package com.example.onlineshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.models.ProductItem;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewItemActivity extends AppCompatActivity {

    private ImageView productImage, backButton;
    private TextView productName, productDescription, productPrice, itemQuantity, increaseQuantity, decreaseQuantity,
            totalPrice;
    private Button addToCartButton;
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

        // Initialize API service
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Get productId from intent
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

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
