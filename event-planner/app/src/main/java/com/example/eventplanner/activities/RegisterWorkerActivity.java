package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityRegisterWorkerBinding;
import com.example.eventplanner.model.UserPUPZ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class RegisterWorkerActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_worker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityRegisterWorkerBinding binding= ActivityRegisterWorkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.continueBtn.setOnClickListener(v->{
            
            if(binding.passwordInput.getText().equals(binding.confirmPasswordInput.getText())) {
                Toast.makeText(this, "Please make sure the passwords match!"+ binding.passwordInput.getText() +" "+ binding.confirmPasswordInput.getText(), Toast.LENGTH_SHORT).show();
                return;
            }

            String ownerId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "error";

            getNumberOfItemsInUsersCollection().thenAccept(numberOfItems -> {

                UserPUPZ user = new UserPUPZ(
                        numberOfItems + 1,
                        ownerId,
                        binding.nameInput.getText().toString(),
                        binding.surnameInput.getText().toString(),
                        binding.emailInput.getText().toString(),
                        binding.passwordInput.getText().toString(),
                        binding.phoneInput.getText().toString(),
                        binding.locationInput.getText().toString(),
                        false
                );

                user.setUserType("PUPZ");

                mAuth.createUserWithEmailAndPassword(binding.emailInput.getText().toString(), binding.passwordInput.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    db.collection("User").add(user);
                                    Toast.makeText(RegisterWorkerActivity.this, "Successfully registered a new worker!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }).exceptionally(e -> {
                Log.e("Firestore", "Error occurred while trying to register PUPZ: ", e);
                return null;
            });

            Intent intent = new Intent(this, AddWorkerScheduleActivity.class);
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, OwnerDashboard.class);
            startActivity(intent);
        });
    }

    private CompletableFuture<Long> getNumberOfItemsInUsersCollection() {
        CollectionReference usersCollection = db.collection("User");

        CompletableFuture<Long> future = new CompletableFuture<>();

        usersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            long numberOfItems;
            if (queryDocumentSnapshots.isEmpty()) {
                numberOfItems = 1L;
            } else {
                numberOfItems = (long) queryDocumentSnapshots.size();
            }
            future.complete(numberOfItems);
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting documents: ", e);
            future.completeExceptionally(e);
        });

        return future;
    }

}