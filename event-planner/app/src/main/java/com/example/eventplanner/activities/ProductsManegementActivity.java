package com.example.eventplanner.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.ProductListPupvFragment;
import com.example.eventplanner.ProductListPupzFragment;
import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityProductsManegementBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};

        TextInputLayout textInputLayout = findViewById(R.id.product_category_filter);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(categoryAdapter);

        TextInputLayout subcategoryTextInputLayout = findViewById(R.id.product_subcategory_filter);
        AutoCompleteTextView subcategoryAutoCompleteTextView = (AutoCompleteTextView) subcategoryTextInputLayout.getEditText();

        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoCompleteTextView.setAdapter(subcategoryAdapter);

        TextInputLayout eventTypeTextInputLayout = findViewById(R.id.event_type_filter);
        AutoCompleteTextView eventTypeAutoCompleteTextView = (AutoCompleteTextView) eventTypeTextInputLayout.getEditText();
        String[] events = {"Event 1", "Event 2", "Event 3", "Event 4", "Event 5"};
        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(ProductsManegementActivity.this, android.R.layout.simple_dropdown_item_1line, events);
        eventTypeAutoCompleteTextView.setAdapter(eventTypeAdapter);

        String usedFragment = getIntent().getStringExtra("used_fragment");

        if(savedInstanceState == null){
            if(usedFragment.equals("product_list_pupv")){
                ProductListPupvFragment fragment = new ProductListPupvFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.product_list_framelayout, fragment)
                        .commit();
            }
            else{
                ProductListPupzFragment fragment = new ProductListPupzFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.product_list_framelayout, fragment)
                        .commit();
            }
        }

    }
}