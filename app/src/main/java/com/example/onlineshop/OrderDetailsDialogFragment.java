package com.example.onlineshop;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.onlineshop.requests.OrderRequest;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsDialogFragment extends DialogFragment {

    private EditText phoneNumberEditText, addressEditText;
    private Button submitButton;
    private ApiService apiService;
    private OrderListener orderListener;

    public OrderDetailsDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_dialog, container, false);

        // Initialize views
        phoneNumberEditText = view.findViewById(R.id.cartPhoneNumberEditText);
        addressEditText = view.findViewById(R.id.cartAddressEditText);
        submitButton = view.findViewById(R.id.cartSubmitButton);

        // Initialize API service
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Set submit button click listener
        submitButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                placeOrder(phoneNumber, address);
            }
        });

        return view;
    }

    // Method to place an order
    private void placeOrder(String phoneNumber, String address) {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Prepare the order data
        OrderRequest orderRequest = new OrderRequest(phoneNumber, address);

        // Make API call to place the order
        Call<Void> call = apiService.placeOrder("Bearer " + token, orderRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Order placed successfully, show toast and notify CartFragment
                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Notify listener (CartFragment) to refresh the cart
                    if (orderListener != null) {
                        orderListener.onOrderPlaced();
                    }

                    // Dismiss the dialog
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Delivery Details");
        return dialog;
    }

    // Set the listener from CartFragment
    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }

    // Interface to notify CartFragment about the order placement
    public interface OrderListener {
        void onOrderPlaced();
    }
}
