package com.example.eventplanner.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityHomeBinding;
import com.example.eventplanner.databinding.ActivityReservationViewBinding;
import com.example.eventplanner.model.Service;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationView extends AppCompatActivity {

    ActivityReservationViewBinding binding;
    FirebaseFirestore db;

    private LinearLayout serviceReservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding= ActivityReservationViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceReservationsContainer = binding.reservationContainer;

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        binding.serviceFilterBtn.setOnClickListener(v -> {
            showFilterDialog();
        });

        binding.packageFilterBtn.setOnClickListener(v -> {
            showFilterDialog();
        });

        db = FirebaseFirestore.getInstance();

        retrieveAllServiceReservationRequests();

    }

    private void retrieveAllServiceReservationRequests() {
        db.collection("ServiceReservationRequest")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                addDocumentToView(document);
                                Log.d("ServiceReservation", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("ServiceReservation", "No documents found in the collection.");
                        }
                    } else {
                        Log.w("ServiceReservation", "Error getting documents.", task.getException());
                    }
                });
    }

    void showFilterDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_reservation_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

        final RadioGroup radioGroup = dialog.findViewById(R.id.filter_radio_grp);
        final Button refreshButton = dialog.findViewById(R.id.refresh_btn);

        refreshButton.setOnClickListener(v->{
            dialog.dismiss();
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton checkedRadioButton = dialog.findViewById(checkedId);
            String option = checkedRadioButton.getText().toString().toUpperCase().replace(" ", "");

            dialog.dismiss();
        });

    };

    private void addDocumentToView(QueryDocumentSnapshot document) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.reservation_card, serviceReservationsContainer, false);

        TextView workerName = view.findViewById(R.id.worker_name_value);
        TextView clientNmae = view.findViewById(R.id.client_name_value);
        TextView occurrenceDate = view.findViewById(R.id.occurrence_date_value);
        TextView duration = view.findViewById(R.id.duration_value);
        MaterialButton approveButton = view.findViewById(R.id.approve_reservation_btn);
        MaterialButton denyButton = view.findViewById(R.id.deny_reservation_btn);
        TextView serviceName = view.findViewById(R.id.service_name);

        com.example.eventplanner.model.Service service = document.get("service", Service.class);
        serviceName.setText(service.getName());

        clientNmae.setText(document.getString("worker_name"));
        workerName.setText(document.getString("worker_name"));
        occurrenceDate.setText(parseDate(document.getString("occurenceDate")));
        duration.setText(document.getString("startHours").concat("-").concat(document.getString("endHours")));

        approveButton.setOnClickListener(v -> {
        });

        denyButton.setOnClickListener(v -> {
        });

        serviceReservationsContainer.addView(view);
    }

    private String parseDate(String documentDate){

        if (documentDate == null || documentDate.isEmpty()) {
            return null;
        }

        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = originalFormat.parse(documentDate);

            String formattedDateStr = targetFormat.format(date);

            return formattedDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}