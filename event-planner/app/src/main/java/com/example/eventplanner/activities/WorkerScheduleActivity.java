package com.example.eventplanner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityRegisterWorkerBinding;
import com.example.eventplanner.databinding.ActivityWorkerScheduleBinding;
import com.example.eventplanner.model.DateSchedule;
import com.example.eventplanner.utils.DateRange;
import com.example.eventplanner.utils.Days;
import com.example.eventplanner.utils.WorkingHours;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.acl.Owner;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WorkerScheduleActivity extends AppCompatActivity {

    DateSchedule schedule = new DateSchedule();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        schedule.setWorkerId(this.getIntent().getLongExtra("workerId", -1));

        ActivityWorkerScheduleBinding binding= ActivityWorkerScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Spinner spinner  = binding.daysSpinner;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(),
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.days_array));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(arrayAdapter);

        binding.addScheduleBtn.setOnClickListener((v)->{

            String dateSpan = binding.dateRangePicker.getText().toString();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {

                String[] dates = dateSpan.split("-");
                String startDateString = dates[0].trim();
                String endDateString = dates[1].trim();

                startDateString = startDateString.replace("/", "-");
                endDateString = endDateString.replace("/", "-");

                schedule.setDateRange(new DateRange(startDateString, endDateString));
                getNumberOfItemsInUsersCollection().thenAccept(numberOfItems ->{

                    for (Map.Entry<String, WorkingHours> entry : schedule.getSchedule().entrySet()) {
                        if (entry.getValue() == null) {
                            entry.setValue(new WorkingHours("9:00 AM", "17:00 PM"));
                        }
                    }
                    schedule.setId(numberOfItems+1);
                    db.collection("DateSchedule")
                            .add(schedule)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("Firestore", "Document added with ID: " + documentReference.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Error adding document", e);
                            });
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });

        binding.enterHoursBtn.setOnClickListener(v -> {
            String fromTime = binding.fromInput.getText().toString().trim();
            String toTime = binding.toInput.getText().toString().trim();

            String timePattern = "(0?[1-9]|(1[012])):[0-5][0-9] [APap][Mm]";

            /*if (!fromTime.matches(timePattern) || !toTime.matches(timePattern)) {
                Toast.makeText(this, "Invalid time format! Please enter time in HH:MM AM/PM format.", Toast.LENGTH_SHORT).show();
                return;
            }*/

            int position = binding.daysSpinner.getSelectedItemPosition();
            Days day = Days.values()[position];

            schedule.setItem(day.toString(), new WorkingHours(fromTime, toTime));
            Toast.makeText(this, "Added:" + new WorkingHours(fromTime, toTime).toString(), Toast.LENGTH_SHORT).show();
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

    private CompletableFuture<Long> getNumberOfItemsInUsersCollection() {
        CollectionReference dateScheduleCollection = db.collection("DateSchedule");

        CompletableFuture<Long> future = new CompletableFuture<>();

        dateScheduleCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long numberOfItems = task.getResult().size();
                future.complete(numberOfItems);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}