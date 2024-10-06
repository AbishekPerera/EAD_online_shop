package com.example.onlineshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.example.onlineshop.ViewItemActivity;
import com.example.onlineshop.models.ProductItem;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductItem> productList;

    public ProductAdapter(Context context, List<ProductItem> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem product = productList.get(position);
        holder.productName.setText(product.getName());

        // Display the category name
        if (product.getCategory() != null) {
            holder.productCategory.setText(product.getCategory().getName());
        } else {
            holder.productCategory.setText("Selected Category");
        }

        holder.productPrice.setText("$" + product.getPrice());

        // Load first image from the list of product images
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            String imageUrl = product.getImages().get(0);
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.productImage);
        } else {
            // Handle case where there are no images
            holder.productImage.setImageResource(R.drawable.item_4); // Use a default image resource
        }

        // Set product rating dynamically
        if (product.getVendor() != null && product.getVendor().getRatings() != null) {
            float rating = product.getVendor().getRatings().getAverage();
            holder.productRating.setText(String.valueOf(rating));

            // Set colored stars based on rating
            setProductRatingStars(holder, rating);
        } else {
            holder.productRating.setText("0/5");
            setProductRatingStars(holder, 0); // No rating, show empty stars
        }

        // Set onClickListener to open ViewItemActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewItemActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("productName", product.getName());
            intent.putExtra("productDescription", product.getDescription());

            if (product.getImages() != null && !product.getImages().isEmpty()) {
                intent.putExtra("productImage", product.getImages().get(0)); // Send first image
            } else {
                intent.putExtra("productImage", ""); // Send an empty string if no image
            }

            context.startActivity(intent);
        });
    }

    private void setProductRatingStars(ProductViewHolder holder, float rating) {
        int fullStars = (int) rating; // Full stars based on the integer part of the rating
        boolean hasHalfStar = rating - fullStars >= 0.5; // Check if there should be a half star
        int emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0); // Calculate remaining empty stars

        // Reset star views to default (empty)
        holder.star1.setImageResource(android.R.drawable.btn_star_big_off);
        holder.star2.setImageResource(android.R.drawable.btn_star_big_off);
        holder.star3.setImageResource(android.R.drawable.btn_star_big_off);
        holder.star4.setImageResource(android.R.drawable.btn_star_big_off);
        holder.star5.setImageResource(android.R.drawable.btn_star_big_off);

        // Fill stars based on the rating
        if (fullStars >= 1) holder.star1.setImageResource(android.R.drawable.btn_star_big_on);
        if (fullStars >= 2) holder.star2.setImageResource(android.R.drawable.btn_star_big_on);
        if (fullStars >= 3) holder.star3.setImageResource(android.R.drawable.btn_star_big_on);
        if (fullStars >= 4) holder.star4.setImageResource(android.R.drawable.btn_star_big_on);
        if (fullStars == 5) holder.star5.setImageResource(android.R.drawable.btn_star_big_on);

        // Set a half star if applicable
        if (hasHalfStar && fullStars < 5) {
            switch (fullStars + 1) {
                case 1:
                    holder.star1.setImageResource(R.drawable.btn_star_big_half);
                    break;
                case 2:
                    holder.star2.setImageResource(R.drawable.btn_star_big_half);
                    break;
                case 3:
                    holder.star3.setImageResource(R.drawable.btn_star_big_half);
                    break;
                case 4:
                    holder.star4.setImageResource(R.drawable.btn_star_big_half);
                    break;
                case 5:
                    holder.star5.setImageResource(R.drawable.btn_star_big_half);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update product list and refresh RecyclerView
    public void updateProductList(List<ProductItem> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productCategory, productPrice, productRating;
        ImageView star1, star2, star3, star4, star5; // Star views

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productCategory = itemView.findViewById(R.id.product_category);
            productPrice = itemView.findViewById(R.id.product_price);
            productRating = itemView.findViewById(R.id.product_rating);

            // Initialize star ImageViews
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }
}
