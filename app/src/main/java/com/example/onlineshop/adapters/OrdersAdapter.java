package com.example.onlineshop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.models.Order;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Add this at the top of your OrdersAdapter class

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private ApiService apiService;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;

        apiService = RetrofitClient.getClient().create(ApiService.class);
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
        holder.requestedToDelete.setVisibility(order.isCancelRequested() ? View.VISIBLE : View.GONE);

        // Set RecyclerView for order items
        OrderItemsAdapter itemsAdapter = new OrderItemsAdapter(context, order.getOrderItems());
        holder.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.orderItemsRecyclerView.setAdapter(itemsAdapter);

        // Handle long press for canceling orders (only ongoing orders)
        if (order.getStatus().equalsIgnoreCase("Processing")
                || order.getStatus().equalsIgnoreCase("Partially Delivered")) {
            holder.itemView.setOnLongClickListener(v -> {
                showCancelOrderDialog(order); // Call method to display dialog
                return true;
            });
        }

        // Expand/Collapse order items
        holder.expandOrderButton.setOnClickListener(v -> {
            if (holder.orderItemsRecyclerView.getVisibility() == View.VISIBLE) {
                holder.orderItemsRecyclerView.setVisibility(View.GONE);
                holder.expandOrderButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
            } else {
                holder.orderItemsRecyclerView.setVisibility(View.VISIBLE);
                holder.expandOrderButton.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
            }
        });
    }

    private void showCancelOrderDialog(Order order) {
        // Create the dialog using the custom layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_order, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText cancelOrderNote = dialogView.findViewById(R.id.cancelOrderNote);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        // Handle "Cancel" button click
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Handle "Yes" button click
        confirmButton.setOnClickListener(v -> {
            String note = cancelOrderNote.getText().toString().trim();

            // check if note is empty then show toast
            if (note.isEmpty()) {
                Toast.makeText(context, "Please enter a note", Toast.LENGTH_SHORT).show();
                return;
            }

            cancelOrderRequest(order.getId(), note); // Send cancel request with note
            dialog.dismiss();
        });
    }

    private void cancelOrderRequest(String orderId, String note) {
        // Get token from SharedPreferences (context needed)
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Create request body for cancellation
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("note", note);

        // Make API call
        Call<Void> call = apiService.cancelOrderRequest("Bearer " + token, orderId, requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Order requested to cancel", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderPhone, orderAddress, orderTotalPrice, orderStatus, requestedToDelete;
        ImageButton expandOrderButton;
        RecyclerView orderItemsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderPhone = itemView.findViewById(R.id.orderPhone);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            requestedToDelete = itemView.findViewById(R.id.requestedToDelete);
            expandOrderButton = itemView.findViewById(R.id.expandOrderButton);
            orderItemsRecyclerView = itemView.findViewById(R.id.orderItemsRecyclerView);
        }
    }
}
