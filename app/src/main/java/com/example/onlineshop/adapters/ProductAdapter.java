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
        holder.productCategory.setText(product.getCategory());
        holder.productPrice.setText("$" + product.getPriceE());
        holder.productRating.setText(String.valueOf(product.getRating()));
        holder.productImage.setImageResource(product.getImageResId());

        // Set onClickListener to open ViewItemActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewItemActivity.class);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productDescription", "This is a description for " + product.getName());
            intent.putExtra("productImage", product.getImageResId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productCategory, productPrice, productRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productCategory = itemView.findViewById(R.id.product_category);
            productPrice = itemView.findViewById(R.id.product_price);
            productRating = itemView.findViewById(R.id.product_rating);
        }
    }
}
