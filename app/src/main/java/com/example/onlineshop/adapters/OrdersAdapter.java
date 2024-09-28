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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrdersAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        holder.productName.setText(orderItem.getProductName());
        holder.orderDate.setText("Date: " + orderItem.getOrderDate());
        holder.productQuantityPrice.setText("Qty: " + orderItem.getQuantity() + " | Price: $" + orderItem.getPrice());
        holder.orderStatus.setText(orderItem.getOrderStatus());

        // Set status color
        if (orderItem.getOrderStatus().equalsIgnoreCase("Canceled")) {
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.red_700));
        } else if (orderItem.getOrderStatus().equalsIgnoreCase("Delivered")) {
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.green_700));
        } else {
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.purple_500));
        }

        // Set product image (for now using a default image, but it can be updated dynamically)
        holder.productImage.setImageResource(orderItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productName, orderDate, productQuantityPrice, orderStatus;
        ImageView productImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            orderDate = itemView.findViewById(R.id.orderDate);
            productQuantityPrice = itemView.findViewById(R.id.productQuantityPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
