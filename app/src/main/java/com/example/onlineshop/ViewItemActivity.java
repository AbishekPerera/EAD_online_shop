package com.example.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewItemActivity extends AppCompatActivity {

    private ImageView productImage, backButton;
    private TextView productName, productDescription, itemQuantity, increaseQuantity, decreaseQuantity;
    private Button addToCartButton;
    private int quantity = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Initialize views
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        itemQuantity = findViewById(R.id.itemQuantity);
        increaseQuantity = findViewById(R.id.increaseQuantity);
        decreaseQuantity = findViewById(R.id.decreaseQuantity);
        addToCartButton = findViewById(R.id.addToCartButton);
        backButton = findViewById(R.id.backButton);

        // Get the intent extras
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String name = intent.getStringExtra("productName");
        String description = intent.getStringExtra("productDescription");
        int imageResId = intent.getIntExtra("productImage", R.drawable.item_1);

        // Set data to views
        productImage.setImageResource(imageResId);
        productName.setText(name);
        productDescription.setText(description);

        // Handle quantity increase
        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            itemQuantity.setText(String.valueOf(quantity));
        });

        // Handle quantity decrease
        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                itemQuantity.setText(String.valueOf(quantity));
            }
        });

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener(v -> {
            // You can handle adding the item to the cart here
        });
    }
}
