package com.example.eventplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.activities.ProductsManegementActivity;
import com.example.eventplanner.databinding.ActivityProductsManegementBinding;
import com.example.eventplanner.databinding.FragmentProductListPupvBinding;

import java.util.ArrayList;

import adapters.ProductListAdapter;
import adapters.ProductListPupvAdapter;
import model.Product;

public class ProductListPupvFragment extends Fragment{
    View view;
    FragmentProductListPupvBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list_pupv, container, false);
        binding = FragmentProductListPupvBinding.inflate(getLayoutInflater());

        ArrayList<Product> products = getProducts();

        ProductListPupvAdapter productListAdapter = new ProductListPupvAdapter(requireContext(), products);

        binding.productsListPupv.setAdapter(productListAdapter);
        binding.productsListPupv.setClickable(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CreateProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @NonNull
    private static ArrayList<Product> getProducts() {
        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        String[] names = {"Product 1", "Product 2", "Product 3", "Product 4", "Product 5"};
        String[] description = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5"};
        Double[] prices = {10.0, 20.0, 30.0, 40.0, 50.0};
        Double[] discounts = {1.0, 2.0, 3.0, 4.0, 5.0};
        Integer[] imageIds = {R.drawable.product_1, R.drawable.product_2, R.drawable.product_3,
                R.drawable.product_4, R.drawable.product_5};
        Boolean[] available = {true, true, true, true, true};
        Boolean[] visible = {true, true, true, true, true};

        ArrayList<Product> products = new ArrayList<>();

        for(int i = 0; i < ids.length; i++){
            products.add(new Product(ids[i], categories[i], subcategories[i],
                    names[i], description[i], prices[i], discounts[i], imageIds[i], null, available[i], visible[i]));
        }
        return products;
    }
}