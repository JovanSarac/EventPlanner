package com.example.eventplanner.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.components.TimeInputComponent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OD_RegisterActivity extends AppCompatActivity {
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
    Button buttonSelectImage;
    ImageView imageViewProfile;

    TimeInputComponent mondayComponent;
    TimeInputComponent tuesdayComponent;
    TimeInputComponent wednesdayComponent;
    TimeInputComponent thursdayComponent;
    TimeInputComponent fridayComponent;
    TimeInputComponent saturdayComponent;
    TimeInputComponent sundayComponent;
    static final int REQUEST_IMAGE_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_od_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupTimePickComponents();
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

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonOD = findViewById(R.id.ODRadio);
        RadioButton radioButtonPUPV = findViewById(R.id.PUPVRadio);
        LinearLayout PUPVLayout=findViewById(R.id.PUPVLayout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ODRadio) {
                    PUPVLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.PUPVRadio) {
                    PUPVLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonSelectImage = findViewById(R.id.addImageButton);
        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ImageView imageView=findViewById(R.id.imageViewProfile);
            imageView.setImageURI(selectedImage);

        }
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


    private void setupTimePickComponents(){
        mondayComponent = findViewById(R.id.monInputView);
        mondayComponent.setDayText("Monday:");
        tuesdayComponent = findViewById(R.id.tueInputView);
        tuesdayComponent.setDayText("Tuesday:");
        wednesdayComponent = findViewById(R.id.wedInputView);
        wednesdayComponent.setDayText("Wednesday:");
        thursdayComponent = findViewById(R.id.thuInputView);
        thursdayComponent.setDayText("Thursday");
        fridayComponent = findViewById(R.id.friInputView);
        fridayComponent.setDayText("Friday");
        saturdayComponent = findViewById(R.id.satInputView);
        saturdayComponent.setDayText("Saturday");
        sundayComponent = findViewById(R.id.sunInputView);
        sundayComponent.setDayText("Sunday");
    }


}