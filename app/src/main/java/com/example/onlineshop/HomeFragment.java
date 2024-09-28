package com.example.onlineshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlineshop.adapters.BannerAdapter;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.models.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductItem> productList;
    private ViewPager2 bannerCarousel;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;

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

        recyclerView = view.findViewById(R.id.productsRecyclerView);
        // Set up the RecyclerView with GridLayoutManager (2 columns)
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
}
