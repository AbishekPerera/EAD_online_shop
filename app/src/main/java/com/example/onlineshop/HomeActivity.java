package com.example.onlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

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

        // Set the selected item (if needed)
        bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Assuming home is selected by default

        // Use the updated setOnItemSelectedListener method
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {// Already on home screen
                    return true;
                } else if (itemId == R.id.navigation_orders) {// Open OrdersActivity when orders icon is tapped
                    Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_profile) {// Handle profile click if necessary
                    return true;
                }
                return false;
            }
        });
    }
}
