package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductItem> productList; // Original product list
    private List<ProductItem> filteredProductList; // Filtered list for search results

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryItem> categoryList;

    private ViewPager2 bannerCarousel;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;

    private ApiService apiService;
    private androidx.appcompat.widget.SearchView searchView; // SearchView for searching products

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
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

        // Initialize SearchView
        searchView = view.findViewById(R.id.searchBar);

        // Initialize Categories RecyclerView
        categoryRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        // Initialize API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Check if user has a cart
        checkUserCart();

        // Fetch categories
        fetchCategories();

        // Initialize Products RecyclerView
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);

        // Fetch products
        fetchProducts(null); // No category filter initially

        // Set up SearchView
        setupSearchView();

        return view;
    }

    private void checkUserCart() {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Check if user has a cart
        Call<Void> call = apiService.getUserCart("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    // If response is not 200, handle as if the cart does not exist
                    if (response.code() == 404) {
                        // Cart does not exist, create a new cart
                        createUserCart();
                    } else {
                        Toast.makeText(getContext(), "Failed to check cart: " + response.message(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to check cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserCart() {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Create a new cart
        Call<Void> call = apiService.createCart("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cart created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to create cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to create cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    categoryAdapter = new CategoryAdapter(getContext(), categoryList, HomeFragment.this);
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

    // Fetch products by category or all products
    private void fetchProducts(@Nullable String categoryId) {
        // Get token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<List<ProductItem>> call;
        if (categoryId != null) {
            // Make API call to get products by category
            call = apiService.getProductsByCategory("Bearer " + token, categoryId);
        } else {
            // Make API call to get all products
            call = apiService.getProducts("Bearer " + token);
        }

        call.enqueue(new Callback<List<ProductItem>>() {
            @Override
            public void onResponse(Call<List<ProductItem>> call, Response<List<ProductItem>> response) {
                if (response.isSuccessful()) {
                    // Set the products in RecyclerView
                    productList = response.body();
                    filteredProductList = new ArrayList<>(productList); // Initially, the filtered list is the same as
                                                                        // the original list
                    productAdapter = new ProductAdapter(getContext(), filteredProductList);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductItem>> call, Throwable t) {
                Toast.makeText(getContext(), "API Call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(String categoryId) {
        // Fetch products based on selected category
        fetchProducts(categoryId);
    }

    // Set up SearchView for filtering products by name
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when submit is pressed
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as text changes
                filterProducts(newText);
                return true;
            }
        });
    }

    // Filter products by name based on search query
    private void filterProducts(String query) {
        if (TextUtils.isEmpty(query)) {
            // If search query is empty, show all products
            filteredProductList = new ArrayList<>(productList);
        } else {
            // Filter the product list
            List<ProductItem> filteredList = new ArrayList<>();
            for (ProductItem product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            filteredProductList = filteredList;
        }

        // Update RecyclerView with filtered list
        productAdapter.updateProductList(filteredProductList);
    }
}
