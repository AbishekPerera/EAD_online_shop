package com.example.onlineshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;
import com.example.onlineshop.responses.UserResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private LinearLayout updateNameSection, deleteAccountSection;
    private String token, userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        userId = sharedPreferences.getString("userId", ""); // Assume user ID is saved in SharedPreferences

        // Initialize views
        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView emailTextView = view.findViewById(R.id.userEmail);
        updateNameSection = view.findViewById(R.id.updateNameSection);
        deleteAccountSection = view.findViewById(R.id.deleteAccountSection);

        // Fetch and update user details whenever the profile fragment is opened
        fetchAndStoreUserDetails(userNameTextView, emailTextView);

        // Set an onClickListener for the Update Name section
        updateNameSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateNameDialog(userNameTextView);
            }
        });

        // Handle Deactivate Account section
        deleteAccountSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeactivateAccountDialog();
            }
        });

        // Handle logout action
        LinearLayout logoutSection = view.findViewById(R.id.logoutSection);
        logoutSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return view;
    }

    // Method to show the dialog for updating the user's name
    private void showUpdateNameDialog(TextView userNameTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_name, null);
        builder.setView(dialogView);

        EditText editTextNewName = dialogView.findViewById(R.id.editTextNewName);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        updateButton.setOnClickListener(v -> {
            String newName = editTextNewName.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateUserName(newName, userNameTextView);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the user's name
    private void updateUserName(String newName, TextView userNameTextView) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", newName);

        Call<Void> call = apiService.updateUserName("Bearer " + token, requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                    userNameTextView.setText(newName);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", newName);
                    editor.apply();
                } else {
                    Toast.makeText(getContext(), "Failed to update name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to fetch and store user details
    private void fetchAndStoreUserDetails(TextView userNameTextView, TextView emailTextView) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getCurrentUser("Bearer " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", user.getUsername());
                    editor.putString("userEmail", user.getEmail());
                    editor.putString("userId", user.getId()); // Store the user ID
                    editor.apply();

                    userNameTextView.setText(user.getUsername());
                    emailTextView.setText(user.getEmail());
                } else {
                    Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show the confirmation dialog for deactivating the account
    private void showDeactivateAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Deactivate Account")
                .setMessage("Are you sure you want to deactivate your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deactivateAccount()) // Call the deactivate function
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Method to deactivate the user's account
    private void deactivateAccount() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deactivateAccount("Bearer " + token, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Account deactivated successfully", Toast.LENGTH_SHORT).show();
                    logoutUser(); // After deactivating, log the user out
                } else {
                    Toast.makeText(getContext(), "Failed to deactivate account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to log out the user
    private void logoutUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved data
        editor.apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);
        getActivity().finish(); // Finish current activity
    }
}
