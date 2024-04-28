package com.example.eventplanner.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.SubcategoriesCardAdapter;
import com.example.eventplanner.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class SuggestedSubcategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_subcategories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        List<Subcategory> dataList = new ArrayList<>();
        dataList.add(new Subcategory("Electronics", "Smartphones", "The latest smartphones with advanced features.", 1));
        dataList.add(new Subcategory("Electronics", "Laptops", "High-performance laptops for work and gaming.", 1));
        dataList.add(new Subcategory("Electronics", "Cameras", "Professional cameras for photography enthusiasts.", 1));
        dataList.add(new Subcategory("Clothing", "Men's T-shirts", "Stylish and comfortable t-shirts for men.", 2));
        dataList.add(new Subcategory("Clothing", "Women's Dresses", "Elegant dresses for various occasions.", 2));
        dataList.add(new Subcategory("Clothing", "Kids' Apparel", "Adorable clothing for children of all ages.", 2));

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SubcategoriesCardAdapter(dataList));
    }
}