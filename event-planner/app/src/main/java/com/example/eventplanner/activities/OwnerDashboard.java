package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityHomeBinding;
import com.example.eventplanner.databinding.ActivityOwnerDashboardBinding;

public class OwnerDashboard extends AppCompatActivity {

    private boolean showSearchInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityOwnerDashboardBinding binding= ActivityOwnerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addWorkerBtn.setOnClickListener(v->{
            Intent intent = new Intent(OwnerDashboard.this, RegisterWorkerActivity.class);
            startActivity(intent);
        });

        binding.tableRow.setOnClickListener(v->{
            Intent intent = new Intent(OwnerDashboard.this, WorkerScheduleActivity.class);
            startActivity(intent);
        });

        binding.card.setOnClickListener(v->{
            Intent intent = new Intent(OwnerDashboard.this, WorkerDashboardActivity.class);
            startActivity(intent);
        });

        binding.searchBtn.setOnClickListener(v->{
            showSearchInput = !showSearchInput;
            binding.searchInput.setVisibility(showSearchInput ? View.VISIBLE : View.GONE);
        });

    }
}