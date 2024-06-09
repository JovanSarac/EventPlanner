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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityHomeBinding;
import com.example.eventplanner.databinding.ActivityReservationViewBinding;
import com.example.eventplanner.model.EventPUPZ;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceReservationRequest;
import com.example.eventplanner.model.UserOD;
import com.example.eventplanner.model.UserPUPZ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ReservationView extends AppCompatActivity {

    ActivityReservationViewBinding binding;
    FirebaseFirestore db;
    List<ServiceReservationRequest> serviceReservations;
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

        serviceReservations = new ArrayList<>();

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
                                serviceReservations.add(document.toObject(ServiceReservationRequest.class));
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
            retrieveAllServiceReservationRequests();
            dialog.dismiss();
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton checkedRadioButton = dialog.findViewById(checkedId);
            String option = checkedRadioButton.getText().toString().toUpperCase().replace(" ", "");

            serviceReservationsContainer.removeAllViews();

            CollectionReference collectionRef = db.collection("ServiceReservationRequest");

            collectionRef.whereEqualTo("status", option)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addDocumentToView(document);
                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error querying documents", e);
                    });

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
        LinearLayout btnLayout = view.findViewById(R.id.reservation_btns_layout);

        if(!document.getString("status").equals("NEW"))
            btnLayout.setVisibility(View.GONE);

        com.example.eventplanner.model.Service service = document.get("service", Service.class);
        serviceName.setText(service.getName());

        getUserById(Double.parseDouble(document.getString("workerId"))).thenAccept(full ->{
            workerName.setText(full);
        });
        getUserDocument(document.getString("userId")).thenAccept(fullName -> {
            clientNmae.setText(fullName);
        });
        occurrenceDate.setText(parseDate(document.getString("occurenceDate")));
        duration.setText(document.getString("startHours").concat("-").concat(document.getString("endHours")));

        approveButton.setOnClickListener(v -> {
            getDocumentCount("Event").thenAccept(count ->{
                ServiceReservationRequest reservation = document.toObject(ServiceReservationRequest.class);

                EventPUPZ event = new EventPUPZ(new Long(count+1), reservation);
                reservation.setStatus("APPROVED");

                db.collection("Event").add(event).addOnSuccessListener(t -> {
                    String reservationDocumentId = document.getId();

                    db.collection("ServiceReservationRequest")
                            .document(reservationDocumentId)
                            .set(reservation, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Reservation updated successfully!");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Firestore", "Error updating reservation", e);
                            });
                });
            });
        });

        denyButton.setOnClickListener(v -> {
        });

        serviceReservationsContainer.addView(view);
    }

    public CompletableFuture<Integer> getDocumentCount(String collectionName) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CollectionReference collectionRef = db.collection(collectionName);

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = task.getResult().size();
                    future.complete(count);
                } else {
                    future.completeExceptionally(task.getException());
                }
            }
        });

        return future;
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

    public CompletableFuture<String> getUserDocument(String documentId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DocumentReference docRef = db.collection("User").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String firstName = document.getString("FirstName") == null ? document.getString("firstName") : document.getString("FirstName");
                        String lastName = document.getString("LastName") == null ? document.getString("lastName") : document.getString("LastName");

                        future.complete(firstName.concat(" ").concat(lastName));
                    } else {
                        future.completeExceptionally(new Exception("No such document"));
                    }
                } else {
                    future.completeExceptionally(task.getException());
                }
            }
        });
        return future;
    }

    public CompletableFuture<String> getUserById(Double id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        db.collection("User")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String firstName = document.getString("FirstName") == null ? document.getString("firstName") : document.getString("FirstName");
                                String lastName = document.getString("LastName") == null ? document.getString("lastName") : document.getString("LastName");

                                future.complete(firstName.concat(" ").concat(lastName));
                                return;
                            }
                            future.completeExceptionally(new Exception("No such document"));
                        } else {
                            future.completeExceptionally(task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReservationView.this, "ne radi", Toast.LENGTH_SHORT).show();
                    }
                });
        return future;
    }
}