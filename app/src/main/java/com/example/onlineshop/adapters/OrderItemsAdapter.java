package com.example.onlineshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.OrderItem;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;

    public OrderItemsAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_details, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.orderItemName.setText(item.getItemNameE());
        holder.orderItemDetails.setText("Quantity: " + item.getQuantity() + "  |  Price: $" + item.getPriceE());

        // Set the order item status
        holder.orderItemStatus.setText("Status: " + item.getStatus());

        // Load item image
        holder.orderItemImage.setImageResource(item.getImageResource());

        // Set status color based on the item status
        if (item.getStatus().equalsIgnoreCase("Delivered")) {
            holder.orderItemStatus.setTextColor(context.getResources().getColor(R.color.green_700));
        } else if (item.getStatus().equalsIgnoreCase("Pending")) {
            holder.orderItemStatus.setTextColor(context.getResources().getColor(R.color.red_200));
        } else if (item.getStatus().equalsIgnoreCase("Canceled")) {
            holder.orderItemStatus.setTextColor(context.getResources().getColor(R.color.red_700));
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        ImageView orderItemImage;
        TextView orderItemName, orderItemDetails, orderItemStatus;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
            orderItemName = itemView.findViewById(R.id.orderItemName);
            orderItemDetails = itemView.findViewById(R.id.orderItemDetails);
            orderItemStatus = itemView.findViewById(R.id.orderItemStatus); // New status field
        }
    }
}
