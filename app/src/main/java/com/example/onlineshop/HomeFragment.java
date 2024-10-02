package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlineshop.adapters.BannerAdapter;
import com.example.onlineshop.adapters.CategoryAdapter;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.models.CategoryItem;
import com.example.onlineshop.models.ProductItem;
import com.example.onlineshop.retrofit.ApiService;
import com.example.onlineshop.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductItem> productList;

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryItem> categoryList;

    private ViewPager2 bannerCarousel;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize banner carousel
        bannerCarousel = view.findViewById(R.id.bannerCarousel);

        // Sample banner images
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.top_banner);
        bannerImages.add(R.drawable.img);
        bannerImages.add(R.drawable.top_banner);

        // Set up BannerAdapter
        bannerAdapter = new BannerAdapter(getContext(), bannerImages);
        bannerCarousel.setAdapter(bannerAdapter);


        // Initialize Categories RecyclerView
        categoryRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        // Initialize API service
        apiService = RetrofitClient.getClient("http://10.0.2.2:5163/api/").create(ApiService.class);

        // Fetch categories
        fetchCategories();

        // Initialize Products RecyclerView
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);

        // Sample Product List
        productList = new ArrayList<>();
        productList.add(new ProductItem("Product 1", "Category 1", 100.0, 4.5f, R.drawable.item_1));
        productList.add(new ProductItem("Product 2", "Category 2", 200.0, 4.0f, R.drawable.item_2));
        productList.add(new ProductItem("Product 3", "Category 3", 300.0, 3.5f, R.drawable.item_3));
        productList.add(new ProductItem("Product 4", "Category 4", 150.0, 4.8f, R.drawable.item_4));

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        return view;
    }

    private void fetchCategories() {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Make API call to get categories
        Call<List<CategoryItem>> call = apiService.getCategories("Bearer " + token);
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                if (response.isSuccessful()) {
                    // Set the categories in RecyclerView
                    categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Toast.makeText(getContext(), "API Call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
