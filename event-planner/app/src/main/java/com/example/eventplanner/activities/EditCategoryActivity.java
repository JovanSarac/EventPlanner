package com.example.eventplanner.activities;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EditCategoryActivity extends AppCompatActivity {
    TextInputEditText descriptionInput;
    TextInputEditText nameInput;
    boolean isCategoryActive;
    Long categoryId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        String categoryDescription = intent.getStringExtra("categoryDescription");
        categoryId = intent.getLongExtra("categoryId",0);

        nameInput= findViewById(R.id.categoryName);
        nameInput.setText(categoryName);
        descriptionInput= findViewById(R.id.categoryDescription);
        descriptionInput.setText(categoryDescription);

        isCategoryActive = intent.getBooleanExtra("isAdd", false);
        if(isCategoryActive){
            Button button=findViewById(R.id.editCategory);
            button.setText("Add");
        }

        findViewById(R.id.editCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditCategory(v);
            }
        });
    }

    private void addEditCategory(View v){
        Long id;
        if(isCategoryActive){
            id = new Random().nextLong();
        }
        else{
            id=categoryId;
        }


        Map<String, Object> item = new HashMap<>();
        item.put("Name", nameInput.getText().toString());
        item.put("Description", descriptionInput.getText().toString());

        db.collection("Categories")
                .document(id.toString())
                .set(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Product created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
}