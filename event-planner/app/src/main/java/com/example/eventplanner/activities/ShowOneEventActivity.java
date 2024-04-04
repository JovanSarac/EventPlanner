package com.example.eventplanner.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditProductBinding;

import java.util.ArrayList;

public class ShowOneEventActivity extends AppCompatActivity {

    com.example.eventplanner.databinding.ActivityShowOneEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = com.example.eventplanner.databinding.ActivityShowOneEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String eventName = getIntent().getStringExtra("eventName");
        String eventDescription = getIntent().getStringExtra("eventDescription");
        String eventLocation = getIntent().getStringExtra("eventLocation");
        String eventDistanceLocation = getIntent().getStringExtra("eventDistanceLocation");
        String eventType = getIntent().getStringExtra("eventType");
        String eventDate = getIntent().getStringExtra("eventDate");

        binding.nameEvent.setText(eventName);
        binding.descriptionEvent.setText(eventDescription);
        binding.locationEvent.setText(eventLocation);
        binding.locationDistanceEvent.setText(eventDistanceLocation);
        binding.typeEvent.setText(eventType);
        binding.dateEvent.setText(eventDate);

    }
}