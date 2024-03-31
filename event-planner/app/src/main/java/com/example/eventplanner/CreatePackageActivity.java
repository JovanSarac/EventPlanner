package com.example.eventplanner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class CreatePackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayList<String> events = new ArrayList<>(Arrays.asList(
                "Event 11", "Event 12", "Event 13", "Event 14", "Event 15",
                "Event 21", "Event 22", "Event 23", "Event 24", "Event 25",
                "Event 31", "Event 32", "Event 33", "Event 34", "Event 35",
                "Event 41", "Event 42", "Event 43", "Event 44", "Event 45",
                "Event 51", "Event 52", "Event 53", "Event 54", "Event 55"));

        TextInputLayout textInputLayout = findViewById(R.id.categories);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(CreatePackageActivity.this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(categoryAdapter);

        TextInputLayout subcategoryTextInputLayout = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoCompleteTextView = (AutoCompleteTextView) subcategoryTextInputLayout.getEditText();
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(CreatePackageActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoCompleteTextView.setAdapter(subcategoryAdapter);

        TextInputLayout eventTextInputLayout = findViewById(R.id.events);
        AutoCompleteTextView eventAutoCompleteTextView = (AutoCompleteTextView) eventTextInputLayout.getEditText();
        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(CreatePackageActivity.this, android.R.layout.simple_dropdown_item_1line, events);
        eventAutoCompleteTextView.setAdapter(eventAdapter);
    }
}