package com.example.onlineshop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.example.onlineshop.models.CartItem;
import com.example.onlineshop.responses.CartResponse;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private ApiService apiService;
    private CartUpdateListener cartUpdateListener;

    public CartAdapter(Context context, List<CartItem> cartItemList, CartUpdateListener cartUpdateListener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartUpdateListener = cartUpdateListener;
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class); // Replace with your API base URL
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        holder.productName.setText(cartItem.getProductNameE());
        holder.productPrice.setText("Price: $" + cartItem.getProductPrice());
        holder.productQuantity.setText(String.valueOf(cartItem.getProductQuantity()));

        // Load the first image from the list of product images if available
        if (cartItem.getImagesE() != null && !cartItem.getImagesE().isEmpty()) {
            String imageUrl = cartItem.getImagesE().get(0);
            Glide.with(context).load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.default_item); // Placeholder image
        }

        // Handle Increment Button
        holder.increaseQuantity.setOnClickListener(v -> {
            updateCartQuantity(cartItem.getProductId(), "increase");
        });

        // Handle Decrement Button
        holder.decreaseQuantity.setOnClickListener(v -> {
//            if (cartItem.getProductQuantity() > 1) {
                updateCartQuantity(cartItem.getProductId(), "decrease");
//            }
        });
    }

    private void updateCartQuantity(String productId, String action) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<CartResponse> call;
        if (action.equals("increase")) {
            call = apiService.addItemToCart2("Bearer " + token, productId);
        } else {
            call = apiService.removeItemFromCart("Bearer " + token, productId);
        }

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse updatedCart = response.body();
                    cartUpdateListener.onCartUpdated(updatedCart);  // Notify the fragment to update the UI
                } else {
                    Toast.makeText(context, "Failed to update cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productPrice, productQuantity;
        Button increaseQuantity, decreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
        }
    }

    // Interface to notify the CartFragment about updates
    public interface CartUpdateListener {
        void onCartUpdated(CartResponse updatedCart);
    }
}
