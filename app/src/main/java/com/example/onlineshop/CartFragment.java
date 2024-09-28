package com.example.onlineshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.adapters.CartAdapter;
import com.example.onlineshop.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private Button placeOrderButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy Data for Cart Items
        cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("Product 1", 100.00, 1, R.drawable.item_1));
        cartItemList.add(new CartItem("Product 2", 200.00, 2, R.drawable.item_2));
        cartItemList.add(new CartItem("Product 3", 150.00, 1, R.drawable.item_3));

        // Set Adapter
        cartAdapter = new CartAdapter(getContext(), cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);

        // Initialize the Place Order button
        placeOrderButton = view.findViewById(R.id.placeOrderButton);

        // Set the onClickListener for the Place Order button
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate the total number of items in the cart
                int totalItems = 0;
                for (CartItem item : cartItemList) {
                    totalItems += item.getProductQuantity();
                }

                // Show a toast with the total items count
                Toast.makeText(getContext(), "Order placed for " + totalItems + " items!", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}
