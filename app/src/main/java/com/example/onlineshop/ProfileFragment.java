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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private LinearLayout updateNameSection;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Initialize Update Name section
        updateNameSection = view.findViewById(R.id.updateNameSection);

        // Set an onClickListener for the Update Name section
        updateNameSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateNameDialog();
            }
        });

        // Handle logout action
        LinearLayout logoutSection = view.findViewById(R.id.logoutSection);
        logoutSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the SharedPreferences token
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // This will remove all saved data (like the token)
                editor.apply();

                // Redirect to the login screen (MainActivity)
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
                startActivity(intent);
                getActivity().finish(); // Finish current activity
            }
        });

        return view;
    }

    private void showUpdateNameDialog() {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_name, null);
        builder.setView(dialogView);

        // Find the EditText and Buttons in the dialog layout
        EditText editTextNewName = dialogView.findViewById(R.id.editTextNewName);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        // Create the AlertDialog and show it
        AlertDialog dialog = builder.create();
        dialog.show();

        // Cancel Button action
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Update Button action
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextNewName.getText().toString().trim();
                if (!newName.isEmpty()) {
                    updateUserName(newName);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserName(String newName) {
        // Make the API call to update the user name
        ApiService apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Create the request body
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", newName);

        Call<Void> call = apiService.updateUserName("Bearer " + token, requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Update the displayed name in the profile
                    Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, update the TextView displaying the user's name in the profile
                     TextView userName = getView().findViewById(R.id.userName);
                     userName.setText(newName);
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
}
