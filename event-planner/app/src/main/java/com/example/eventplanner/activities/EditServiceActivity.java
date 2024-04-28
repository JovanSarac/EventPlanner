package com.example.eventplanner.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditServiceBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import com.example.eventplanner.adapters.EventListAdapter;
import com.example.eventplanner.adapters.ImageAdapter;

public class EditServiceActivity extends AppCompatActivity {

    ActivityEditServiceBinding binding;
    FloatingActionButton addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Long serviceId = getIntent().getLongExtra("Id", -1);
        String serviceName = getIntent().getStringExtra("Name");
        String serviceCategory = getIntent().getStringExtra("Category");
        String serviceSubcategory = getIntent().getStringExtra("Subcategory");
        String serviceDescription = getIntent().getStringExtra("Description");
        String serviceSpecific = getIntent().getStringExtra("Specific");
        Double servicePricePerHour = getIntent().getDoubleExtra("PricePerHour", 0);
        Double serviceFullPrice = getIntent().getDoubleExtra("FullPrice", 0);
        Double serviceDiscount = getIntent().getDoubleExtra("Discount", 0);
        Double serviceDuration = getIntent().getDoubleExtra("Duration", 0);
        String serviceLocation = getIntent().getStringExtra("Location");
        ArrayList<Integer> serviceImageId = getIntent().getIntegerArrayListExtra("Image");
        ArrayList<String> serviceEvents = getIntent().getStringArrayListExtra("Events");
        ArrayList<String> serviceProviders = getIntent().getStringArrayListExtra("Providers");
        String serviceReservationDue = getIntent().getStringExtra("ReservationDue");
        String serviceCancelationDue = getIntent().getStringExtra("CancelationDue");
        Boolean serviceAutomaticAffirmation = getIntent().getBooleanExtra("AutomaticAffirmation", false);
        Boolean serviceAvailability = getIntent().getBooleanExtra("Availability", false);
        Boolean serviceVisibility = getIntent().getBooleanExtra("Visibility", false);

        addImage = findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        TextInputLayout name = findViewById(R.id.name);
        TextInputEditText nameAutoComplete = (TextInputEditText) name.getEditText();
        nameAutoComplete.setText(serviceName);

        TextInputLayout category = findViewById(R.id.category);
        TextInputEditText categoryAutoComplete = (TextInputEditText) category.getEditText();
        categoryAutoComplete.setText(serviceCategory);

        TextInputLayout subcategory = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoComplete = (AutoCompleteTextView) subcategory.getEditText();
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(EditServiceActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoComplete.setAdapter(subcategoryAdapter);
        subcategoryAutoComplete.setText(serviceSubcategory, false);
        subcategoryAutoComplete.setSelection(subcategoryAutoComplete.getText().length());

        TextInputLayout description = findViewById(R.id.description);
        TextInputEditText descriptionAutoComplete = (TextInputEditText) description.getEditText();
        descriptionAutoComplete.setText(serviceDescription);

        TextInputLayout specific = findViewById(R.id.specific);
        TextInputEditText specificAutoComplete = (TextInputEditText) specific.getEditText();
        specificAutoComplete.setText(serviceSpecific);

        TextInputLayout pricePerHour = findViewById(R.id.pricePerHour);
        TextInputEditText pricePerHourAutoComplete = (TextInputEditText) pricePerHour.getEditText();
        pricePerHourAutoComplete.setText(servicePricePerHour.toString());

        TextInputLayout fullPrice = findViewById(R.id.price);
        TextInputEditText fullPriceAutoComplete = (TextInputEditText) fullPrice.getEditText();
        fullPriceAutoComplete.setText(serviceFullPrice.toString());

        TextInputLayout duration = findViewById(R.id.duration);
        TextInputEditText durationAutoComplete = (TextInputEditText) duration.getEditText();
        durationAutoComplete.setText(serviceDuration.toString());

        TextInputLayout discount = findViewById(R.id.discount);
        TextInputEditText discountAutoComplete = (TextInputEditText) discount.getEditText();
        discountAutoComplete.setText(serviceDiscount.toString());

        TextInputLayout location = findViewById(R.id.location);
        TextInputEditText locationAutoComplete = (TextInputEditText) location.getEditText();
        locationAutoComplete.setText(serviceLocation);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < serviceImageId.size(); i++){
            arrayList.add(serviceImageId.get(i));
        }

        ImageAdapter adapter = new ImageAdapter(EditServiceActivity.this, arrayList);
        recyclerView.setAdapter(adapter);

        EventListAdapter eventListAdapter = new EventListAdapter(this, serviceEvents);
        binding.events.setAdapter(eventListAdapter);

        EventListAdapter providerListAdapter = new EventListAdapter(this, serviceProviders);
        binding.providers.setAdapter(providerListAdapter);

        TextInputLayout reservationDue = findViewById(R.id.reservation_due);
        TextInputEditText reservationDueAutoComplete = (TextInputEditText) reservationDue.getEditText();
        reservationDueAutoComplete.setText(serviceReservationDue);

        TextInputLayout cancelationDue = findViewById(R.id.cancelation_due);
        TextInputEditText cancelationDueAutoComplete = (TextInputEditText) cancelationDue.getEditText();
        cancelationDueAutoComplete.setText(serviceCancelationDue);

        CheckBox automaticAffirmation = findViewById(R.id.automatic_affirmation);
        automaticAffirmation.setChecked(serviceAutomaticAffirmation);

        CheckBox available = findViewById(R.id.availability);
        available.setChecked(serviceAvailability);

        CheckBox visibility = findViewById(R.id.visibility);
        visibility.setChecked(serviceVisibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            /*ImageView imageView = findViewById(R.id.carousel_image_view);
            imageView.setImageURI(selectedImage);*/
        }
    }
}