package com.example.eventplanner.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCompanyViewBinding;
import com.example.eventplanner.model.CompanyReport;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.services.FCMHttpClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CompanyViewActivity extends AppCompatActivity {

    ActivityCompanyViewBinding binding;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    UserPUPV company;
    String pupvId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        pupvId = getIntent().getStringExtra("pupvId");

        getCompany();

        binding.reportCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CompanyViewActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        String reasonOfReport = report.getEditText().getText().toString();
                        String ODId = user.getUid();
                        String dateOfReport = LocalDate.now().toString();

                        doc.put("pupvId", pupvId);
                        doc.put("reasonOfReport", reasonOfReport);
                        doc.put("ODId", ODId);
                        doc.put("dateOfReport", dateOfReport);
                        doc.put("status", "reported");

                        db.collection("CompanyReports")
                                .document(id.toString())
                                .set(doc)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        popupWindow.dismiss();
                                        createNotification(ODId);
                                        Toast.makeText(CompanyViewActivity.this, "Report sent successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CompanyViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }

    private void createNotification(String ODId){
        Long id = new Random().nextLong();
        Map<String, Object> doc = new HashMap<>();

        doc.put("title", "New company report");
        doc.put("body", "Company " + company.getCompanyName() + " has been reported");
        doc.put("read", false);
        doc.put("userId", "e1ktzSoZY9ZdfEuL7PyShaRWI522");

        db.collection("Notifications")
                .document(id.toString())
                .set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sendNotification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompanyViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendNotification(){
        String serverKey="AAAA8GYmoZ8:APA91bHsjyzOSa2JtO_cQWFO-X1p9nMuHRO8DTfD1zhcY4mnqZ-2EZmIn8tMf1ISmnM31WB68Mzn2soeUgEISXlSc9WjRvcRhyYbmBgi7whJuYXX-24wkODByasquofLaMZydpg78esK";
        String jsonPayload = "{\"data\":{\"title\":\"New company report\",\"body\":\"Company "
                + company.getCompanyName() +
                " has been reported\"},\"to\":\"/topics/" + "AdminTopic" + "\"}";

        FCMHttpClient httpClient = new FCMHttpClient();
        httpClient.sendMessageToTopic(serverKey, "AdminTopic", jsonPayload);
    }

    private void getCompany(){
        db.collection("User")
                .document(pupvId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        company = new UserPUPV(
                                documentSnapshot.getString("FirstName"),
                                documentSnapshot.getString("LastName"),
                                documentSnapshot.getString("E-mail"),
                                documentSnapshot.getString("Password"),
                                documentSnapshot.getString("Phone"),
                                documentSnapshot.getString("Address"),
                                true,
                                documentSnapshot.getString("CompanyName"),
                                documentSnapshot.getString("CompanyDescription"),
                                documentSnapshot.getString("CompanyAddress"),
                                documentSnapshot.getString("CompanyEmail"),
                                documentSnapshot.getString("CompanyPhone"),
                                documentSnapshot.getString("WorkTime"));

                        initializeComponents();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompanyViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeComponents(){
        binding.name.getEditText().setText(company.getCompanyName());
        binding.description.getEditText().setText(company.getCompanyDescription());
        binding.email.getEditText().setText(company.getEmail());
        binding.address.getEditText().setText(company.getCompanyAddress());
        binding.phone.getEditText().setText(company.getCompanyPhone());

        String[] workTime =company.getWorkTime().split("\\?");
        if(workTime[0].equals("free")){
            binding.mondayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startMonday.setText(workTime[0].split("\\-")[0]);
            binding.endMonday.setText(workTime[0].split("\\-")[1]);
        }
        if(workTime[1].equals("free")){
            binding.tuesdayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startTuesday.setText(workTime[1].split("\\-")[0]);
            binding.endTuesday.setText(workTime[1].split("\\-")[1]);
        }
        if(workTime[2].equals("free")){
            binding.wednesdayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startWednesday.setText(workTime[2].split("\\-")[0]);
            binding.endWednesday.setText(workTime[2].split("\\-")[1]);
        }
        if(workTime[3].equals("free")){
            binding.thursdayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startThursday.setText(workTime[3].split("\\-")[0]);
            binding.endThursday.setText(workTime[3].split("\\-")[1]);
        }
        if(workTime[4].equals("free")){
            binding.fridayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startFriday.setText(workTime[4].split("\\-")[0]);
            binding.endFriday.setText(workTime[4].split("\\-")[1]);
        }
        if(workTime[5].equals("free")){
            binding.saturdayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startSaturday.setText(workTime[5].split("\\-")[0]);
            binding.endSaturday.setText(workTime[5].split("\\-")[1]);
        }
        if(workTime[6].equals("free")){
            binding.sundayLayout.setVisibility(View.GONE);
        }
        else{
            binding.startSunday.setText(workTime[6].split("\\-")[0]);
            binding.endSunday.setText(workTime[6].split("\\-")[1]);
        }
    }
}