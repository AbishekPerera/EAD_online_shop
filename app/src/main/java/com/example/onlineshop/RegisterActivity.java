package com.example.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.requests.RegisterRequest;
import com.example.onlineshop.responses.RegisterResponse;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText registerUsername, registerEmail, registerPassword, registerConfirmPassword;
    Button registerButton;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purple_200));
        }

        registerUsername = findViewById(R.id.name);
        registerEmail = findViewById(R.id.email);
        registerPassword = findViewById(R.id.password);
        registerConfirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.registerButton);

        // Initialize Retrofit
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String usernameText = registerUsername.getText().toString().trim();
                String emailText = registerEmail.getText().toString().trim();
                String passwordText = registerPassword.getText().toString().trim();
                String confirmPasswordText = registerConfirmPassword.getText().toString().trim();

                // Validate input
                if (!emailText.isEmpty() && !usernameText.isEmpty() && !passwordText.isEmpty() && passwordText.equals(confirmPasswordText)) {
                    // Call register method
                    registerUser(usernameText, emailText, passwordText);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please check your input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // backToLoginText
        findViewById(R.id.backToLoginText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Back to login page
                finish();
            }
        });
    }

    // Register user method
    private void registerUser(String username, String email, String password) {
        // Create a RegisterRequest object
        RegisterRequest registerRequest = new RegisterRequest(username, email, password, "Customer");

        // Call API to register user
        Call<RegisterResponse> call = apiService.registerUser(registerRequest);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    // Handle success (e.g., redirect to login or home)
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle registration failure
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                // Handle error
                Toast.makeText(RegisterActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
