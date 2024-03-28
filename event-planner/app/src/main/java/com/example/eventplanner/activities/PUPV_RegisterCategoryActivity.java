package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;

import java.util.Arrays;
import java.util.List;

public class PUPV_RegisterCategoryActivity extends AppCompatActivity {
    List<String> categoriesList = Arrays.asList(
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
    List<String> eventNames = Arrays.asList(
            "Birthday Party",
            "Wedding Ceremony",
            "Graduation Celebration",
            "Baby Shower",
            "Housewarming Party",
            "Anniversary Dinner",
            "Retirement Party",
            "Engagement Party",
            "Prom Night",
            "Holiday Gathering",
            "Farewell Party",
            "Reunion",
            "Barbecue Cookout",
            "Fundraising Gala",
            "Business Conference",
            "Product Launch",
            "Music Concert",
            "Art Exhibition",
            "Film Screening",
            "Fashion Show"
    );

    ListView selectedCategories;
    ListView selectedEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pupv_register_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupListViewForCategories();
        setupListViewForEvents();

        selectedCategories = findViewById(R.id.categoryView);
        Button registerButton = (Button) findViewById(R.id.registerUser);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedItems(v);

            }
        });
    }

    private void setupListViewForCategories() {
        selectedCategories = findViewById(R.id.categoryView);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categoriesList);
        selectedCategories.setAdapter(adapterCategories);
    }
    private void setupListViewForEvents() {
        selectedEvents = findViewById(R.id.eventView);
        ArrayAdapter<String> adapterEvents = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, eventNames);
        selectedEvents.setAdapter(adapterEvents);
    }

    private void showSelectedItems(View v) {
        String str = "";
        for (int i = 0; i < selectedCategories.getCount(); i++) {
            if (selectedCategories.isItemChecked(i)) {
                str += selectedCategories.getItemAtPosition(i) + "\n";
            }
        }
        Toast.makeText(v.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}