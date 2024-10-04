package com.example.onlineshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.adapters.OrdersAdapter;
import com.example.onlineshop.models.Order;
import com.example.onlineshop.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ongoingOrdersRecyclerView, orderHistoryRecyclerView;
    private OrdersAdapter ongoingOrdersAdapter, orderHistoryAdapter;
    private List<Order> ongoingOrderList, orderHistoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize RecyclerViews
        ongoingOrdersRecyclerView = view.findViewById(R.id.ongoingOrdersRecyclerView);
        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        ongoingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Generate sample data
        generateSampleData();

        // Set Adapters
        ongoingOrdersAdapter = new OrdersAdapter(getContext(), ongoingOrderList);
        orderHistoryAdapter = new OrdersAdapter(getContext(), orderHistoryList);

        ongoingOrdersRecyclerView.setAdapter(ongoingOrdersAdapter);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        return view;
    }

    private void generateSampleData() {
        // Sample items for each order with item status
        List<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItem("Product A", 2, 50.00, R.drawable.item_1, "Delivered"));
        orderItems1.add(new OrderItem("Product B", 1, 100.00, R.drawable.item_2, "Pending"));

        List<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(new OrderItem("Product C", 3, 30.00, R.drawable.item_3, "Processing"));

        // Ongoing Orders: Processing, Partially Delivered
        ongoingOrderList = new ArrayList<>();
        ongoingOrderList.add(new Order("123-456-7890", "123 Main St", "$150.00", "Processing", orderItems1));
        ongoingOrderList.add(new Order("098-765-4321", "456 Elm St", "$90.00", "Partially Delivered", orderItems2));

        // Order History: Delivered, Canceled
        orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new Order("111-222-3333", "789 Oak St", "$200.00", "Delivered", orderItems1));
        orderHistoryList.add(new Order("444-555-6666", "321 Pine St", "$60.00", "Canceled", orderItems2));
    }
}
