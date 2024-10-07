package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.adapters.CartAdapter;
import com.example.onlineshop.models.CartItem;
import com.example.onlineshop.responses.CartResponse;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView cartTotal;
    private Button placeOrderButton;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize views
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartTotal = view.findViewById(R.id.cartTotal);
        placeOrderButton = view.findViewById(R.id.placeOrderButton);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Fetch cart data from the API
        fetchCartData();

        // Set the onClickListener for the Place Order button
        placeOrderButton.setOnClickListener(v -> {
            OrderDetailsDialogFragment dialogFragment = new OrderDetailsDialogFragment();
            dialogFragment.setOrderListener(() -> {
                // Refresh the cart after order is placed
                fetchCartData();
            });
            dialogFragment.show(getParentFragmentManager(), "OrderDetailsDialog");
        });

        return view;
    }

    private void fetchCartData() {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Make API call to get cart data
        Call<CartResponse> call = apiService.getCart("Bearer " + token);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    updateCartUI(cartResponse);
                } else {
                    Toast.makeText(getContext(), "Failed to load cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartUI(CartResponse cartResponse) {
        // Update the total price
        cartTotal.setText("Total: $" + cartResponse.getTotalPrice());

        // Update the cart items list
        cartItemList = new ArrayList<>();
        for (CartResponse.CartItemResponse itemResponse : cartResponse.getItems()) {
            CartItem cartItem = new CartItem(
                    itemResponse.getProductId(),
                    itemResponse.getName(),
                    itemResponse.getPriceE(),
                    itemResponse.getQuantity(),
                    itemResponse.getInventoryCount(),
                    itemResponse.getImages());
            cartItemList.add(cartItem);
        }

        // Set Adapter
        cartAdapter = new CartAdapter(getContext(), cartItemList, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    // This will be called when the cart is updated
    @Override
    public void onCartUpdated(CartResponse updatedCart) {
        updateCartUI(updatedCart);
    }
}
