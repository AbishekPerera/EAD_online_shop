package com.example.onlineshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set basic order details
        holder.orderPhone.setText("Phone: " + order.getPhoneNumber());
        holder.orderAddress.setText("Address: " + order.getAddress());
        holder.orderTotalPrice.setText("Total: " + order.getTotalPrice());
        holder.orderStatus.setText(order.getStatus());

        // Set RecyclerView for order items
        OrderItemsAdapter itemsAdapter = new OrderItemsAdapter(context, order.getOrderItems());
        holder.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.orderItemsRecyclerView.setAdapter(itemsAdapter);

        // Expand/Collapse order items
        holder.expandOrderButton.setOnClickListener(v -> {
            if (holder.orderItemsRecyclerView.getVisibility() == View.VISIBLE) {
                holder.orderItemsRecyclerView.setVisibility(View.GONE);
                holder.expandOrderButton.setImageResource(R.drawable.arrow_detail);
            } else {
                holder.orderItemsRecyclerView.setVisibility(View.VISIBLE);
                holder.expandOrderButton.setImageResource(R.drawable.arrow_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderPhone, orderAddress, orderTotalPrice, orderStatus;
        ImageButton expandOrderButton;
        RecyclerView orderItemsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderPhone = itemView.findViewById(R.id.orderPhone);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            expandOrderButton = itemView.findViewById(R.id.expandOrderButton);
            orderItemsRecyclerView = itemView.findViewById(R.id.orderItemsRecyclerView);
        }
    }
}
