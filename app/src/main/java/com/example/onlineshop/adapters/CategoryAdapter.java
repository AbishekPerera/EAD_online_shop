package com.example.onlineshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategoryItem> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private int selectedPosition = -1; // To track the selected category position

    public CategoryAdapter(Context context, List<CategoryItem> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.onCategoryClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem categoryItem = categoryList.get(position);
        holder.categoryName.setText(categoryItem.getCategoryName());

        // Change background color based on whether the item is selected or not
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_rounded_corner); // Set selected background
        } else {
            holder.itemView.setBackgroundResource(R.drawable.rounded_corner_cards); // Default background
        }

        // Set click listener to handle category clicks
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position; // Update selected position
            notifyDataSetChanged(); // Notify adapter to refresh the background
            onCategoryClickListener.onCategoryClick(categoryItem.getId());
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }

    // Interface to handle clicks
    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryId);
    }
}
