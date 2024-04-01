package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityAddWorkerScheduleBinding;
import com.example.eventplanner.databinding.ActivityHomeBinding;

public class AddWorkerScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_worker_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityAddWorkerScheduleBinding binding= ActivityAddWorkerScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner spinner  = binding.daysSpinner;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(),
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.days_array));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(arrayAdapter);

        binding.finishBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

        binding.skipBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, RegisterWorkerActivity.class);
            startActivity(intent);
        });
    }

}