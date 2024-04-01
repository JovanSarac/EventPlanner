package com.example.eventplanner.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditPackageBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import adapters.EventListAdapter;
import adapters.PackageProductListAdapter;
import adapters.PackageServiceListAdapter;
import model.Product;
import model.Service;

public class EditPackageActivity extends AppCompatActivity {

    ActivityEditPackageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Long packageId = getIntent().getLongExtra("Id", -1);
        String packageName = getIntent().getStringExtra("Name");
        String packageCategory = getIntent().getStringExtra("Category");
        String packageSubcategory = getIntent().getStringExtra("Subcategory");
        String packageDescription = getIntent().getStringExtra("Description");
        Double packagePrice = getIntent().getDoubleExtra("Price", 0);
        Double packageDiscount = getIntent().getDoubleExtra("Discount", 0);
        ArrayList<Integer> packageImages = getIntent().getIntegerArrayListExtra("ImageTypes");
        ArrayList<Product> packageProducts = (ArrayList<Product>) getIntent().getSerializableExtra("Products");
        ArrayList<Service> packageServices = (ArrayList<Service>) getIntent().getSerializableExtra("Services");
        ArrayList<String> packageEvents = getIntent().getStringArrayListExtra("Events");
        String packageReservationDue = getIntent().getStringExtra("ReservationDue");
        String packageCancelationDue = getIntent().getStringExtra("CancelationDue");
        Boolean packageAutomaticAffirmation = getIntent().getBooleanExtra("AutomaticAffirmation", false);
        Boolean packageAvailability = getIntent().getBooleanExtra("Availability", false);
        Boolean packageVisibility = getIntent().getBooleanExtra("Visibility", false);

        TextInputLayout name = findViewById(R.id.name);
        TextInputEditText nameAutoComplete = (TextInputEditText) name.getEditText();
        nameAutoComplete.setText(packageName);

        TextInputLayout category = findViewById(R.id.category);
        TextInputEditText categoryAutoComplete = (TextInputEditText) category.getEditText();
        categoryAutoComplete.setText(packageCategory);

        TextInputLayout subcategory = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoComplete = (AutoCompleteTextView) subcategory.getEditText();
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(EditPackageActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoComplete.setAdapter(subcategoryAdapter);
        subcategoryAutoComplete.setText(packageSubcategory, false);
        subcategoryAutoComplete.setSelection(subcategoryAutoComplete.getText().length());

        TextInputLayout description = findViewById(R.id.description);
        TextInputEditText descriptionAutoComplete = (TextInputEditText) description.getEditText();
        descriptionAutoComplete.setText(packageDescription);

        TextInputLayout price = findViewById(R.id.price);
        TextInputEditText fullPriceAutoComplete = (TextInputEditText) price.getEditText();
        fullPriceAutoComplete.setText(packagePrice.toString());

        TextInputLayout discount = findViewById(R.id.discount);
        TextInputEditText discountAutoComplete = (TextInputEditText) discount.getEditText();
        discountAutoComplete.setText(packageDiscount.toString());

        ImageView imageView = findViewById(R.id.carousel_image_view);
        imageView.setImageResource(packageImages.get(0));

        EventListAdapter eventListAdapter = new EventListAdapter(this, packageEvents);
        binding.events.setAdapter(eventListAdapter);

        PackageProductListAdapter productListAdapter = new PackageProductListAdapter(this, packageProducts);
        binding.productList.setAdapter(productListAdapter);

        PackageServiceListAdapter serviceListAdapter = new PackageServiceListAdapter(this, packageServices);
        binding.serviceList.setAdapter(serviceListAdapter);

        TextInputLayout reservationDue = findViewById(R.id.reservation_due);
        TextInputEditText reservationDueAutoComplete = (TextInputEditText) reservationDue.getEditText();
        reservationDueAutoComplete.setText(packageReservationDue);

        TextInputLayout cancelationDue = findViewById(R.id.cancelation_due);
        TextInputEditText cancelationDueAutoComplete = (TextInputEditText) cancelationDue.getEditText();
        cancelationDueAutoComplete.setText(packageCancelationDue);

        CheckBox automaticAffirmation = findViewById(R.id.automatic_affirmation);
        automaticAffirmation.setChecked(packageAutomaticAffirmation);

        CheckBox available = findViewById(R.id.availability);
        available.setChecked(packageAvailability);

        CheckBox visibility = findViewById(R.id.visibility);
        visibility.setChecked(packageVisibility);
    }
}