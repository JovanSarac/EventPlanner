package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.UserInfoActivity;
import com.example.eventplanner.model.UserOD;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.model.UserReport;
import com.example.eventplanner.services.FCMHttpClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReportedUsersAdapter extends ArrayAdapter<UserReport> {

    private ArrayList<UserReport> reports;
    private int resource;
    private Context context;
    private FirebaseFirestore db;
    public ReportedUsersAdapter(Context context, int resource, ArrayList<UserReport> reports){
        super(context, resource, reports);
        this.reports = reports;
        this.resource = resource;
        this.context = context;

        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        UserReport report = reports.get(position);

        TextView status = convertView.findViewById(R.id.status);
        TextView reason = convertView.findViewById(R.id.reason);
        TextView dateOfReport = convertView.findViewById(R.id.dateOfReport);

        getFullName(report.getReportedId(), "reported", convertView);
        getFullName(report.getReporterId(), "reporter", convertView);
        status.setText(report.getStatus().toString());
        reason.setText(report.getReason());
        String formattedDate = stringFormatedDate(report);
        dateOfReport.setText(formattedDate);

        Button approve = convertView.findViewById(R.id.approve);
        Button deny = convertView.findViewById(R.id.deny);

        checkStatus(approve, deny, report);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report.setStatus(UserReport.Status.APPROVED);
                notifyDataSetChanged();
                updateInDb(report);
                blockUser(report.getReportedId());
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.popup_report_company, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                TextView reasonOfReport = popUpView.findViewById(R.id.reason_of_report);
                reasonOfReport.setText("Reason of denying");

                TextInputLayout reason = popUpView.findViewById(R.id.report);
                reason.getEditText().setHint("Reason");

                Button send = popUpView.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFullNameForNotification(report, reason.getEditText().getText().toString());

                        report.setStatus(UserReport.Status.DENIED);
                        notifyDataSetChanged();
                        updateInDb(report);

                        popupWindow.dismiss();
                    }
                });
            }
        });

        Button reportedAccView = convertView.findViewById(R.id.reported_acc_view);
        Button reporterAccView = convertView.findViewById(R.id.reporter_acc_view);

        reportedAccView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userId", report.getReportedId());
                context.startActivity(intent);
            }
        });

        reporterAccView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userId", report.getReporterId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void blockUser(String userId){
        db.collection("User")
                .document(userId)
                .update("IsValid", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "User blocked", Toast.LENGTH_LONG).show();
                        checkUserType(userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserType(String userId){
        db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("UserType").equals("PUPV")){
                            changeAvailabilityOfProducts(userId);
                            changeAvailabilityOfServices(userId);
                            changeAvailabilityOfPackages(userId);
                            blockPupzs(userId);
                        }
                        //od
                        else{

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeAvailabilityOfProducts(String ownerId){
        db.collection("Products")
                .whereEqualTo("pupvId", ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            db.collection("Products")
                                    .document(doc.getId())
                                    .update("available", false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeAvailabilityOfServices(String ownerId){
        db.collection("Services")
                .whereEqualTo("pupvId", ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            db.collection("Services")
                                    .document(doc.getId())
                                    .update("available", false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changeAvailabilityOfPackages(String ownerId){
        db.collection("Packages")
                .whereEqualTo("pupvId", ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            db.collection("Packages")
                                    .document(doc.getId())
                                    .update("available", false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void blockPupzs(String userId){
        db.collection("User")
                .whereEqualTo("ownerId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            db.collection("User")
                                    .document(doc.getId())
                                    .update("valid", false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateInDb(UserReport report){
        db.collection("UserReports")
                .document(report.getId().toString())
                .update("status", report.getStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Report " + report.getStatus().toString().toLowerCase(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void sendNotification(UserReport report, String reasonOfDenying, String fullNameOfReported){
        String serverKey="AAAA8GYmoZ8:APA91bHsjyzOSa2JtO_cQWFO-X1p9nMuHRO8DTfD1zhcY4mnqZ-2EZmIn8tMf1ISmnM31WB68Mzn2soeUgEISXlSc9WjRvcRhyYbmBgi7whJuYXX-24wkODByasquofLaMZydpg78esK";

        String jsonPayload = "{\"data\":{\"title\":\"Update on your report\",\"body\":\"Your report on "
                + fullNameOfReported +
                " has been denied. \nReason: " + reasonOfDenying + " \"},\"to\":\"/topics/" + report.getReporterId() + "Topic" + "\"}";

        FCMHttpClient httpClient = new FCMHttpClient();
        httpClient.sendMessageToTopic(serverKey, report.getReporterId() + "Topic", jsonPayload);
    }

    private void getFullNameForNotification(UserReport report, String reasonOfDenying){
        db.collection("User")
                .document(report.getReportedId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        sendNotification(report, reasonOfDenying, documentSnapshot.getString("FirstName") + documentSnapshot.getString("LastName"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkStatus(Button approve, Button deny, UserReport report) {
        if(!report.getStatus().equals(UserReport.Status.REPORTED)){
            approve.setVisibility(View.GONE);
            deny.setVisibility(View.GONE);
        }
    }

    private String stringFormatedDate(UserReport report) {
        Date date = new Date(report.getDateOfReport());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    private void getFullName(String id, String type,@NonNull View convertView) {
        db.collection("User")
                .document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (type.equals("reported")) {
                            TextView reportedName = convertView.findViewById(R.id.reported_name);

                            reportedName.setText(documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName"));
                        } else {
                            TextView reporterName = convertView.findViewById(R.id.reporter_name);

                            reporterName.setText(documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName"));
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
