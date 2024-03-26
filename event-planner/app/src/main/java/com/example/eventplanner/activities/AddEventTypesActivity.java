package com.example.eventplanner.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;

import java.util.Arrays;
import java.util.List;

public class AddEventTypesActivity extends AppCompatActivity {
    List<String> subcategoriesList = Arrays.asList(
            "Plumbing Services",
            "Electrical Services",
            "Home Cleaning",
            "Gardening Services",
            "Painting Services",
            "Appliance Repair",
            "Computer Repair",
            "Mobile Phone Repair",
            "Car Mechanics",
            "Locksmith Services",
            "Carpentry Services",
            "Roofing Services",
            "Pest Control Services",
            "Moving Services",
            "Event Planning",
            "Photography Services",
            "Tutoring Services",
            "Fitness Training",
            "Legal Services",
            "Financial Consultancy"
    );
    ListView subcategoriesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event_types);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupListViewForCategories();
    }
    private void setupListViewForCategories() {
        subcategoriesView = findViewById(R.id.subcategoriesView);
        ArrayAdapter<String> adapterSubategories = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, subcategoriesList);
        subcategoriesView.setAdapter(adapterSubategories);
    }
}