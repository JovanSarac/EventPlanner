package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditCategoryActivity extends AppCompatActivity {

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
        TextInputEditText name= findViewById(R.id.categoryName);
        name.setText(categoryName);
        TextInputEditText description= findViewById(R.id.categoryDescription);
        description.setText(categoryDescription);

        boolean isCategoryActive = intent.getBooleanExtra("isAdd", false);
        if(isCategoryActive){
            Button button=findViewById(R.id.editCategory);
            button.setText("Add");
        }
    }
}