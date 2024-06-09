package com.example.eventplanner.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityShowOneChatBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShowOneChatActivity extends AppCompatActivity {

    ActivityShowOneChatBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String recipientId;
    String senderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowOneChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = mAuth.getCurrentUser();

        recipientId = getIntent().getStringExtra("recipientId");
        loadImage(recipientId,binding.imageRecipientProfile);
        senderId = getIntent().getStringExtra("senderId");

        String recipientFullname = getIntent().getStringExtra("recipientFullname");

        binding.recipcientFullName.setText(recipientFullname);
    }

    private void loadImage(String userId, ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images/" + userId);

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Koristimo Glide za učitavanje slike
                Glide.with(imageView.getContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("HomeTwoActivity", "Error loading image", exception);
                imageView.setImageResource(R.drawable.defaultprofilepicture);
            }
        });
    }
}