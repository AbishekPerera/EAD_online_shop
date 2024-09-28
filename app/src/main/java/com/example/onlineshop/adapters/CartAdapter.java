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
import com.example.onlineshop.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
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

        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText("Price: $" + cartItem.getProductPrice());
        holder.productQuantity.setText("Quantity: " + cartItem.getProductQuantity());
        holder.productImage.setImageResource(cartItem.getProductImageResId());

        // Handle item removal
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // Use getAdapterPosition()
                if (currentPosition != RecyclerView.NO_POSITION) {
                    // Remove item from list
                    cartItemList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, cartItemList.size());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, removeItem;
        TextView productName, productPrice, productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            removeItem = itemView.findViewById(R.id.removeItem);
        }
    }
}
