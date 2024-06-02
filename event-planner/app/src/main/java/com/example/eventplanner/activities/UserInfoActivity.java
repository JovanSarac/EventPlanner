package com.example.eventplanner.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityUserInfoBinding;
import com.example.eventplanner.model.UserOD;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.model.UserPUPZ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserInfoActivity extends AppCompatActivity {

    ActivityUserInfoBinding binding;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    UserOD userOD;
    String userODId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        getOD();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("AdminChannel", "AdminChannel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        binding.reportOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) UserInfoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.popup_report_company, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                TextInputLayout report = popUpView.findViewById(R.id.report);
                Button sendReport = popUpView.findViewById(R.id.send);

                sendReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Long id = new Random().nextLong();
                        Map<String, Object> doc = new HashMap<>();

                        doc.put("pupvId", user.getUid());
                        doc.put("reasonOfReport", report.getEditText().getText().toString());
                        doc.put("ODId", userODId);

                        db.collection("UserODReports")
                                .document(id.toString())
                                .set(doc)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        popupWindow.dismiss();
                                        sendNotificationToAdmin();
                                        Toast.makeText(UserInfoActivity.this, "Report sent successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }

    private void sendNotificationToAdmin(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserInfoActivity.this, "AdminChannel");
        builder.setContentTitle("New userOD report");
        builder.setContentText("UserOD" + userOD.getFirstName() + " " + userOD.getLastName() + " has been reported");
        builder.setSmallIcon(R.drawable.ic_approve);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UserInfoActivity.this);
        //managerCompat.notify(1, builder.build());
    }

    private void getOD(){
        db.collection("User")
                .document("vfzH3r61kWgPI9ehrS25Mdj2j2A2")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userOD = new UserOD(
                                0l,
                                documentSnapshot.getString("FirstName"),
                                documentSnapshot.getString("LastName"),
                                documentSnapshot.getString("E-mail"),
                                documentSnapshot.getString("Password"),
                                documentSnapshot.getString("Phone"),
                                documentSnapshot.getString("Address"),
                                documentSnapshot.getBoolean("IsValid")
                        );

                        userODId = documentSnapshot.getId();

                        initializeComponents();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeComponents(){
        binding.name.getEditText().setText(userOD.getFirstName());
        binding.lastname.getEditText().setText(userOD.getLastName());
        binding.email.getEditText().setText(userOD.getEmail());
        binding.phone.getEditText().setText(userOD.getPhone());
        binding.address.getEditText().setText(userOD.getAddress());

        //skloniti ovo jpg
        StorageReference imageRef = storage.getReference().child("images/" + userODId + ".jpg");
        imageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(UserInfoActivity.this)
                                .load(uri)
                                .into(binding.profilePic);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}