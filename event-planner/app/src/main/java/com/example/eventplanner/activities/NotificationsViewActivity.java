package com.example.eventplanner.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.NotificationAdapter;
import com.example.eventplanner.databinding.ActivityNotificationsViewBinding;
import com.example.eventplanner.model.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsViewActivity extends AppCompatActivity {

    ActivityNotificationsViewBinding binding;
    ArrayList<Notification> notifications;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notifications = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        getNotifictions();
    }

    private void getNotifictions(){
        db.collection("Notifications")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            Notification notification = new Notification(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("title"),
                                    doc.getString("body"),
                                    doc.getBoolean("read"),
                                    doc.getString("userId")
                            );

                            notifications.add(notification);
                        }

                        NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationsViewActivity.this, R.layout.notification_card, notifications);
                        binding.notificationList.setAdapter(notificationAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NotificationsViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}