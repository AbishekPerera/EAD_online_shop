package com.example.onlineshop.retrofit;

import com.example.onlineshop.requests.OrderRequest;
import com.example.onlineshop.responses.CartResponse;
import com.example.onlineshop.models.CategoryItem;
import com.example.onlineshop.models.ProductItem;
import com.example.onlineshop.requests.LoginRequest;
import com.example.onlineshop.requests.RegisterRequest;
import com.example.onlineshop.responses.LoginResponse;
import com.example.onlineshop.responses.OrderResponse;
import com.example.onlineshop.responses.RegisterResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("auth/login") // Change "login" to the actual login endpoint
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("user/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("category")
    Call<List<CategoryItem>> getCategories(@Header("Authorization") String token);

    @GET("product/all")
    Call<List<ProductItem>> getProducts(@Header("Authorization") String token);

    @GET("product/{id}")
    Call<ProductItem> getProductDetails(@Path("id") String productId);

    @GET("product/category/{categoryId}")
    Call<List<ProductItem>> getProductsByCategory(@Header("Authorization") String authHeader, @Path("categoryId") String categoryId);

    @GET("cart")
    Call<Void> getUserCart(@Header("Authorization") String authToken);

    @POST("cart")
    Call<Void> createCart(@Header("Authorization") String authToken);

    @POST("cart/addItem/{productId}")
    Call<Void> addItemToCart(@Header("Authorization") String authToken, @Path("productId") String productId);

    // Add an item to the cart (increments the quantity)
    @POST("cart/addItem/{productId}")
    Call<CartResponse> addItemToCart2(@Header("Authorization") String authToken, @Path("productId") String productId);

    // Remove an item from the cart (decrements the quantity)
    @POST("cart/removeItem/{productId}")
    Call<CartResponse> removeItemFromCart(@Header("Authorization") String authToken, @Path("productId") String productId);

    @GET("cart")
    Call<CartResponse> getCart(@Header("Authorization") String token);

    // Place order endpoint
    @POST("order")
    Call<Void> placeOrder(@Header("Authorization") String authToken, @Body OrderRequest orderRequest);

    @GET("order/userOrders")
    Call<List<OrderResponse>> getUserOrders(@Header("Authorization") String authToken);

    @POST("order/cancelOrderRequest/{orderId}")
    Call<Void> cancelOrderRequest(@Header("Authorization") String authToken, @Path("orderId") String orderId, @Body HashMap<String, String> requestBody);

    @PUT("user")
    Call<Void> updateUserName(@Header("Authorization") String token, @Body HashMap<String, String> requestBody);


}
