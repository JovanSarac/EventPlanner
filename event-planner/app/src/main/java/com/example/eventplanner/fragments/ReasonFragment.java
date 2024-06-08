package com.example.eventplanner.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.ApproveRegistrationActivity;
import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.activities.OwnerDashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ReasonFragment extends BottomSheetDialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id;
    View view;
    public ReasonFragment(String id) {
        this.id=id;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.removeUser).setOnClickListener(v->{
            removeUser();
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_reason, container, false);
        return view;
    }

    private void removeUser(){
        TextInputEditText text=view.findViewById(R.id.reason);
        if(text.getText()==null){
            Toast.makeText(view.getContext(), "Reason is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("IsValid", true);
        updates.put("Reason", text.getText().toString());
        db.collection("User").document(id).update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully updated!");
                        sendEmail();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Log.w("Firestore", "Error updating document", e);
                    }
                });
    }

    private void sendEmail() {
        String[] recipients = new String[]{"jovan.katanic204@gmail.com"};
        String subject = "Subject of the email";
        String message = "Body of the email";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(view.getContext(), "mail failed", Toast.LENGTH_SHORT).show();
        }
    }
}