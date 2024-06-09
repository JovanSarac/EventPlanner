package com.example.eventplanner.activities;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ChatRecyclerAdapter;
import com.example.eventplanner.databinding.ActivityShowOneChatBinding;
import com.example.eventplanner.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowOneChatActivity extends AppCompatActivity {

    ActivityShowOneChatBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String recipientId;
    String senderId;
    String recipientFullname;
    String senderFullname;
    ArrayList<Message> messages;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowOneChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = mAuth.getCurrentUser();

        recipientId = getIntent().getStringExtra("recipientId");
        loadImage(recipientId,binding.imageRecipientProfile);
        senderId = getIntent().getStringExtra("senderId");

        recipientFullname = getIntent().getStringExtra("recipientFullname");
        senderFullname = getIntent().getStringExtra("senderFullname");

        binding.recipcientFullName.setText(recipientFullname);

        recyclerView = binding.chatMessageRecyclerRow;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowOneChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        getAllMessages(senderId,recipientId);

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(binding.inputMessage.getText().toString());
                binding.inputMessage.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        Map<String, Object> elememt = new HashMap<>();
        elememt.put("senderId", senderId);
        elememt.put("senderFullName", senderFullname);
        elememt.put("recipientId", recipientId);
        elememt.put("fullnameRecipientId", recipientFullname);
        elememt.put("dateOfSending", new Date());
        elememt.put("content", message);
        elememt.put("status", false);
        elememt.put("participants", Arrays.asList(senderId, recipientId));

        // Dodajte novi dokument sa generisanim ID-om
        db.collection("Messages").document()
                .set(elememt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ShowOneChatActivity", "Message send successfully");
                        getAllMessages(senderId,recipientId);

                        recyclerView.scrollToPosition(messages.size() - 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                });

    }

    private void getAllMessages(String senderId, String recipientId) {
        messages = new ArrayList<>();

        // Kreiramo listu sa svim mogućim kombinacijama participants niza
        List<List<String>> participantsCombinations = new ArrayList<>();
        participantsCombinations.add(Arrays.asList(senderId, recipientId));
        participantsCombinations.add(Arrays.asList(recipientId, senderId));

        db.collection("Messages")
                .whereIn("participants", participantsCombinations)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot doc : task.getResult()) {
                                String senderId = doc.getString("senderId");
                                String senderFullaname = doc.getString("senderFullName");
                                String recipientId = doc.getString("recipientId");
                                String recipientFullname = doc.getString("fullnameRecipientId");
                                Date dateOfSending = doc.getDate("dateOfSending");
                                String content = doc.getString("content");
                                boolean status = doc.getBoolean("status");


                                Message message = new Message(senderId, senderFullaname, recipientId, recipientFullname, dateOfSending, content, status);

                                messages.add(message);
                            }

                            // Sortiranje poruka po datumu od najstarije ka najnovijoj
                            Collections.sort(messages, new Comparator<Message>() {
                                @Override
                                public int compare(Message m1, Message m2) {
                                    return m1.getDateOfSending().compareTo(m2.getDateOfSending());
                                }
                            });

                            // Inicijalizacija adaptera nakon sortiranja
                            ChatRecyclerAdapter adapterChats = new ChatRecyclerAdapter(messages);
                            recyclerView.setAdapter(adapterChats);

                            recyclerView.scrollToPosition(messages.size() - 1);



                        } else {
                            Toast.makeText(ShowOneChatActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowOneChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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