package com.example.eventplanner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityRegisterWorkerBinding;
import com.example.eventplanner.databinding.ActivityWorkerScheduleBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.security.acl.Owner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkerScheduleActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worker_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityWorkerScheduleBinding binding= ActivityWorkerScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dateRangePicker.setOnClickListener((v) -> {

            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            long currentMillis = MaterialDatePicker.todayInUtcMilliseconds();

            constraintsBuilder.setStart(currentMillis);

            long maxDate = currentMillis + TimeUnit.DAYS.toMillis(7);
            constraintsBuilder.setEnd(maxDate);

            CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(currentMillis);
            constraintsBuilder.setValidator(dateValidator);

            MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select date range")
                    .setTheme(R.style.ThemeMaterialCalendar)
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();

            picker.show(getSupportFragmentManager(), picker.toString());

            picker.addOnPositiveButtonClickListener((w)->{
                Pair<Long, Long> selectedDates = (Pair<Long, Long>) picker.getSelection();

                long startDate = selectedDates.first;
                long endDate = selectedDates.second;

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedStartDate = dateFormat.format(new Date(startDate));
                String formattedEndDate = dateFormat.format(new Date(endDate));

                binding.dateRangePicker.setText(formattedStartDate +"-"+formattedEndDate);
            });

            picker.addOnNegativeButtonClickListener((w)->{
                picker.dismiss();
            });
        });

        Spinner spinner  = binding.daysSpinner;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(),
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.days_array));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(arrayAdapter);

        binding.addScheduleBtn.setOnClickListener((v)->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

        binding.cancelBtn.setOnClickListener((v)->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener((v)->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });
    }
}