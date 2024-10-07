package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.onlineshop.responses.UserResponse;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Change the status bar color
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.purple_500));
        }

        // Initialize BottomNavigationView and set the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set the default fragment when the activity is loaded (home screen)
        loadFragment(new HomeFragment()); // Assuming you have a HomeFragment

        // Initialize API service and SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Fetch and store user details from API
        fetchUserDetails();

        // Set the listener to switch between fragments
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeFragment(); // Load home fragment
                } else if (itemId == R.id.navigation_cart) {
                    selectedFragment = new CartFragment();
                } else if (itemId == R.id.navigation_orders) {
                    selectedFragment = new OrdersFragment(); // Load orders fragment
                } else if (itemId == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment(); // Load profile fragment
                }

                // Load the selected fragment
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }

    // Helper method to load the selected fragment into the FrameLayout
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Replace with the fragment
        fragmentTransaction.commit();
    }

    // Fetch the user details and store in SharedPreferences
    private void fetchUserDetails() {
        String token = sharedPreferences.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make the API call
        Call<UserResponse> call = apiService.getCurrentUser("Bearer " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    storeUserDetails(user);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Error fetching user details: " + t.getMessage());
            }
        });
    }

    // Store user details in SharedPreferences
    private void storeUserDetails(UserResponse user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getId());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userName", user.getUsername());
        editor.putString("userRole", user.getRole());
        editor.putBoolean("isActive", user.isActive());
        editor.putBoolean("isPending", user.isPending());
        editor.apply();
    }
}
