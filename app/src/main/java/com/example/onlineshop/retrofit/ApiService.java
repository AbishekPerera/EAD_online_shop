package com.example.onlineshop.retrofit;

import com.example.onlineshop.requests.LoginRequest;
import com.example.onlineshop.requests.RegisterRequest;
import com.example.onlineshop.responses.LoginResponse;
import com.example.onlineshop.responses.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login") // Change "login" to the actual login endpoint
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("user/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}