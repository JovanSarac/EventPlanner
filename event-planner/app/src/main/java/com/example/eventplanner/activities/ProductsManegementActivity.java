package com.example.eventplanner.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityProductsManegementBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import adapters.ProductListAdapter;
import model.Product;

public class ProductsManegementActivity extends AppCompatActivity {

    ActivityProductsManegementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductsManegementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        String[] names = {"Product 1", "Producgt 2", "Product 3", "Product 4", "Product 5"};
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

        ProductListAdapter productListAdapter = new ProductListAdapter(ProductsManegementActivity.this, products);

        TextView textView = new TextView(this);
        textView.setText("Products");
        textView.setTextSize(20);

        binding.productsList.addHeaderView(textView);

        binding.productsList.setAdapter(productListAdapter);
        binding.productsList.setClickable(true);

        Spinner categorySpinner = findViewById(R.id.product_category_filter);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);

        Spinner subcategorySpinner = findViewById(R.id.product_subcategory_filter);
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_spinner_dropdown_item, subcategories);
        subcategorySpinner.setAdapter(subcategoryAdapter);

        Spinner eventTypeSpinner = findViewById(R.id.event_type_filter);
        String[] events = {"Event 1", "Event 2", "Event 3", "Event 4", "Event 5"};
        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_spinner_dropdown_item, events);
        eventTypeSpinner.setAdapter(eventTypeAdapter);
    }
}