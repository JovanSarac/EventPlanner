package com.example.eventplanner.activities;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityAddCommentBinding;
import com.example.eventplanner.databinding.ActivityOwnerDashboardBinding;
import com.example.eventplanner.model.ServiceReservationRequest;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.model.UserPUPZ;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddCommentActivity extends AppCompatActivity {

    private LinearLayout companiesContainer;
    ActivityAddCommentBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding= ActivityAddCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        companiesContainer = binding.companiesContainer;

        binding.backBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        retrieveCompaniesByLoggedUserId(mAuth.getCurrentUser() == null? "error" : mAuth.getCurrentUser().getUid());
    }

    private void retrieveCompaniesByLoggedUserId(String userId){
        Set<String> companyIds = new HashSet<>();

        db.collection("ServiceReservationRequest")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        companyIds.add(documentSnapshot.toObject(ServiceReservationRequest.class).getUserId());
                    }

                    companyIds.forEach(id -> {
                        db.collection("User")
                                .document(id)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshot -> {
                                    displayCompany(queryDocumentSnapshot.toObject(UserPUPV.class));
                                });
                    });
                });
    }
    private void displayCompany(UserPUPV user){
        View cardView = LayoutInflater.from(this).inflate(R.layout.company_card, companiesContainer, false);

        TextView companyNmae = cardView.findViewById(R.id.company_name_value);
        companyNmae.setText(user.getCompanyName());

        TextView companyAddress = cardView.findViewById(R.id.company_location_value);
        companyAddress.setText(user.getCompanyAddress());

        TextView companyEmail = cardView.findViewById(R.id.company_email_value);
        companyAddress.setText(user.getCompanyemail());

        ImageButton showAddCommentBtn = cardView.findViewById(R.id.show_add_comment_btn);
        LinearLayout commentContainer = cardView.findViewById(R.id.comment_container);
        showAddCommentBtn.setOnClickListener(v ->{
            commentContainer.setVisibility(View.VISIBLE);
        });

        Button addCommentBtn = cardView.findViewById(R.id.add_comment_btn);
        EditText description = cardView.findViewById(R.id.comment_description_value);
        EditText grade = cardView.findViewById(R.id.comment_grade_value);
        addCommentBtn.setOnClickListener(v -> {

        });
    }
}