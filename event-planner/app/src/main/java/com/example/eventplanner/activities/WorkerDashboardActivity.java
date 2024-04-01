package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.fragments.WorkerWeeklyScheduleFragment;
import com.example.eventplanner.databinding.ActivityWorkerDashboardBinding;

public class WorkerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worker_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityWorkerDashboardBinding binding= ActivityWorkerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WorkerWeeklyScheduleFragment fragment = new WorkerWeeklyScheduleFragment();
        fragmentTransaction.add(R.id.weekly_schedule_fragment, fragment);
        fragmentTransaction.commit();

        binding.backBtn.setOnClickListener((v)->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

    }
}