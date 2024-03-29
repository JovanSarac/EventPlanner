package com.example.eventplanner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.activities.ProductsManegementActivity;
import com.example.eventplanner.databinding.ActivityEditProductBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import adapters.EventListAdapter;

public class EditProductActivity extends AppCompatActivity {

    ActivityEditProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Long productId = getIntent().getLongExtra("productId", -1);
        String productCategory = getIntent().getStringExtra("productCategory");
        String productSubcategory = getIntent().getStringExtra("productSubcategory");
        String productName = getIntent().getStringExtra("productName");
        String productDescription = getIntent().getStringExtra("productDescription");
        Double productPrice = getIntent().getDoubleExtra("productPrice", 0);
        Double productDiscount = getIntent().getDoubleExtra("productDiscount", 0);
        Integer productImage = getIntent().getIntExtra("productImage", -1);
        ArrayList<String> productEvents = getIntent().getStringArrayListExtra("productEvents");
        Boolean productAvailability = getIntent().getBooleanExtra("productAvailability", true);
        Boolean productVisibility = getIntent().getBooleanExtra("productVisibility", true);


        TextInputLayout category = findViewById(R.id.category);
        TextInputEditText categoryAutoComplete = (TextInputEditText) category.getEditText();
        categoryAutoComplete.setText(productCategory);

        TextInputLayout subcategory = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoComplete = (AutoCompleteTextView) subcategory.getEditText();
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoComplete.setAdapter(subcategoryAdapter);
        subcategoryAutoComplete.setText(productSubcategory, false);
        subcategoryAutoComplete.setSelection(subcategoryAutoComplete.getText().length());

        TextInputLayout name = findViewById(R.id.name);
        TextInputEditText nameAutoComplete = (TextInputEditText) name.getEditText();
        nameAutoComplete.setText(productName);

        TextInputLayout description = findViewById(R.id.description);
        TextInputEditText descriptionAutoComplete = (TextInputEditText) description.getEditText();
        descriptionAutoComplete.setText(productDescription);

        TextInputLayout price = findViewById(R.id.price);
        TextInputEditText priceAutoComplete = (TextInputEditText) price.getEditText();
        priceAutoComplete.setText(productPrice.toString());

        TextInputLayout discount = findViewById(R.id.discount);
        TextInputEditText discountAutoComplete = (TextInputEditText) discount.getEditText();
        discountAutoComplete.setText(productDiscount.toString());

        ImageView imageView = findViewById(R.id.carousel_image_view);
        imageView.setImageResource(productImage);

        EventListAdapter eventListAdapter = new EventListAdapter(this, productEvents);
        binding.events.setAdapter(eventListAdapter);

        CheckBox available = findViewById(R.id.availability);
        available.setChecked(productAvailability);

        CheckBox visibility = findViewById(R.id.visibility);
        visibility.setChecked(productVisibility);
    }
}