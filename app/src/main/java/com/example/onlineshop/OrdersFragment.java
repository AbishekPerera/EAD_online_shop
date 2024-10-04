package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.adapters.OrdersAdapter;
import com.example.onlineshop.models.Order;
import com.example.onlineshop.models.OrderItem;
import com.example.onlineshop.responses.OrderResponse;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    private RecyclerView ongoingOrdersRecyclerView, orderHistoryRecyclerView;
    private OrdersAdapter ongoingOrdersAdapter, orderHistoryAdapter;
    private List<Order> ongoingOrderList, orderHistoryList;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize RecyclerViews
        ongoingOrdersRecyclerView = view.findViewById(R.id.ongoingOrdersRecyclerView);
        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        ongoingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize API service
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Fetch orders from API
        fetchUserOrders();

        return view;
    }

    private void fetchUserOrders() {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Make API call to get orders
        Call<List<OrderResponse>> call = apiService.getUserOrders("Bearer " + token);
        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderResponse> orderResponses = response.body();
                    updateOrderUI(orderResponses);
                } else {
                    Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderUI(List<OrderResponse> orderResponses) {
        ongoingOrderList = new ArrayList<>();
        orderHistoryList = new ArrayList<>();

        // Separate ongoing orders and order history based on status
        for (OrderResponse orderResponse : orderResponses) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderResponse.OrderItemResponse itemResponse : orderResponse.getItems()) {
                OrderItem orderItem = new OrderItem(
                        itemResponse.getName(),
                        itemResponse.getQuantity(),
                        itemResponse.getPrice(),
                        R.drawable.default_item, // Placeholder image
                        itemResponse.getStatus()
                );
                orderItems.add(orderItem);
            }

            Order order = new Order(
                    orderResponse.getPhoneNumber(),
                    orderResponse.getDeliveryAddress(),
                    String.valueOf(orderResponse.getTotalPrice()),
                    orderResponse.getStatus(),
                    orderItems
            );

            if (orderResponse.getStatus().equalsIgnoreCase("Processing") || orderResponse.getStatus().equalsIgnoreCase("Partially Delivered")) {
                ongoingOrderList.add(order);
            } else {
                orderHistoryList.add(order);
            }
        }

        // Set Adapters
        ongoingOrdersAdapter = new OrdersAdapter(getContext(), ongoingOrderList);
        orderHistoryAdapter = new OrdersAdapter(getContext(), orderHistoryList);

        ongoingOrdersRecyclerView.setAdapter(ongoingOrdersAdapter);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
    }
}
