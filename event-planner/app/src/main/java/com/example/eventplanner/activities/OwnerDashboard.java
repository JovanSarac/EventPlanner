package com.example.eventplanner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityHomeBinding;
import com.example.eventplanner.databinding.ActivityOwnerDashboardBinding;
import com.example.eventplanner.model.DateSchedule;
import com.example.eventplanner.model.UserPUPZ;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OwnerDashboard extends AppCompatActivity {

    private boolean showSearchInput = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        initializeScheduleTable(binding);

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

        binding.backBtn.setOnClickListener(v->{
            Intent intent = new Intent(OwnerDashboard.this, HomeActivity.class);
            startActivity(intent);
        });

    }

    private void initializeScheduleTable(ActivityOwnerDashboardBinding binding){

        TableLayout table = binding.workerSchedulesTable;

        db.collection("DateSchedule")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            DateSchedule schedule = DateSchedule.fromFirestoreData(document.getData());

                            TableRow tableRow = new TableRow(this);

                            getUserBySchedule(schedule)
                                    .thenAccept(user -> {
                                        insertColumn(user.getFirstName().concat(" ".concat(user.getLastName())), tableRow,true);
                                        insertColumn(schedule.getDateRange().getStartDate(), tableRow,false);
                                        insertColumn(schedule.getDateRange().getEndDate(), tableRow,false);

                                        table.addView(tableRow);
                                    })
                                    .exceptionally(e -> {
                                        Log.e("Firestore", "Error getting user: ", e);
                                        return null;
                                    });
                        }
                    } else {
                        Log.d("Error", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void insertColumn(Object data, TableRow tableRow, boolean isUsername) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.table_cell));
        textView.setTextColor(Color.BLACK);
        textView.setPadding(12, 12, 12, 12);
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setText(data.toString());
        if(isUsername)
            textView.setTypeface(null, Typeface.BOLD);

        tableRow.addView(textView);
    }

    private CompletableFuture<UserPUPZ> getUserBySchedule(DateSchedule schedule) {
        CompletableFuture<UserPUPZ> future = new CompletableFuture<>();

        Query query = db.collection("User").whereEqualTo("id", schedule.getWorkerId());

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot lastDocument = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                UserPUPZ user = lastDocument.toObject(UserPUPZ.class);
                future.complete(user);
            } else {
                future.completeExceptionally(new Exception("User not found"));
            }
        }).addOnFailureListener(e -> {
            future.completeExceptionally(e);
        });

        return future;
    }
}