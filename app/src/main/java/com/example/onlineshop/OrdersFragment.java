package com.example.onlineshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.adapters.OrdersAdapter;
import com.example.onlineshop.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ongoingOrdersRecyclerView, orderHistoryRecyclerView;
    private OrdersAdapter ongoingOrdersAdapter, orderHistoryAdapter;
    private List<OrderItem> ongoingOrderList, orderHistoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize RecyclerViews
        ongoingOrdersRecyclerView = view.findViewById(R.id.ongoingOrdersRecyclerView);
        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        ongoingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy data for Ongoing Orders
        ongoingOrderList = new ArrayList<>();
        ongoingOrderList.add(new OrderItem("1234", "28 Sept 2024", "Approved", "Product A", 2, 100.00, R.drawable.item_1));
        ongoingOrderList.add(new OrderItem("1235", "27 Sept 2024", "Dispatched", "Product B", 1, 200.00, R.drawable.item_2));

        orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new OrderItem("1232", "25 Sept 2024", "Delivered", "Product C", 3, 150.00, R.drawable.item_3));
        orderHistoryList.add(new OrderItem("1231", "24 Sept 2024", "Canceled", "Product D", 1, 50.00, R.drawable.item_4));


        // Set Adapters
        ongoingOrdersAdapter = new OrdersAdapter(getContext(), ongoingOrderList);
        orderHistoryAdapter = new OrdersAdapter(getContext(), orderHistoryList);

        ongoingOrdersRecyclerView.setAdapter(ongoingOrdersAdapter);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        return view;
    }
}
