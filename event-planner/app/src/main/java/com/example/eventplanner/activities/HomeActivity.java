package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;

public class HomeActivity extends AppCompatActivity {

    private Button buttonProductsManagementPUPZ;
    private Button buttonProductsManagementPUPV;
    private Button buttonCreateEventOD;

    private Button butonSearchAndFilter;

    private Button buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonProductsManagementPUPZ = findViewById(R.id.productsManagmentPUPZ);
        buttonProductsManagementPUPV = findViewById(R.id.productsManagmentPUPV);
        buttonCreateEventOD = findViewById(R.id.createEventOD);
        butonSearchAndFilter = findViewById((R.id.filterAndSearchpsp));
        buttonHome = findViewById(R.id.homeact);

        buttonProductsManagementPUPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonProductsManagementPUPZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCreateEventOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateEventODActivity.class);
                startActivity(intent);
                finish();
            }
        });

        butonSearchAndFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchAndFilterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}