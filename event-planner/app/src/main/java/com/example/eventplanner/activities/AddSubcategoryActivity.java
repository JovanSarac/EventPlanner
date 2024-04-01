package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddSubcategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subcategory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        String subcategoryName = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        int type = intent.getIntExtra("type",-1);
        if(type==0){
            RadioButton radioButtonService = findViewById(R.id.serviceRadio);
            radioButtonService.setChecked(true);
        }else if(type==1){
            RadioButton radioButtonProduct = findViewById(R.id.productRadio);
            radioButtonProduct.setChecked(true);
        }else{
            Log.i("NEKITAG","Error with radio");
        }
        boolean editButtonFlag = intent.getBooleanExtra("editButtonFlag",false);

        if(editButtonFlag){
            Button button = findViewById(R.id.addSubcategory);
            button.setText("Edit");
        }

        TextInputEditText categoryNameTextField= findViewById(R.id.categoryName);
        categoryNameTextField.setText(categoryName);
        TextInputEditText subcategoryNameTextField= findViewById(R.id.subcategoryName);
        subcategoryNameTextField.setText(subcategoryName);
        TextInputEditText descriptionNameTextField= findViewById(R.id.subcategoryDescription);
        descriptionNameTextField.setText(description);


    }
}