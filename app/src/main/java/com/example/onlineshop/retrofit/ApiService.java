package com.example.onlineshop.retrofit;

import com.example.onlineshop.models.CategoryItem;
import com.example.onlineshop.models.ProductItem;
import com.example.onlineshop.requests.LoginRequest;
import com.example.onlineshop.requests.RegisterRequest;
import com.example.onlineshop.responses.LoginResponse;
import com.example.onlineshop.responses.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}
