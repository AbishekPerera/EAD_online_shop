package com.example.onlineshop;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        // Set the default fragment when the activity is loaded (home screen)
        loadFragment(new HomeFragment()); // Assuming you have a HomeFragment

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
}
