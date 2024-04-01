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
import com.example.eventplanner.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private Button buttonProductsManagementPUPZ;
    private Button buttonProductsManagementPUPV;
    private Button buttonCreateEventOD;

    private Button butonSearchAndFilter;

    private Button buttonHome;
    private Button buttonServicesManagementPUPZ;
    private Button buttonServicesManagementPUPV;
    private Button buttonPackgesManagementPUPZ;
    private Button buttonPackgesManagementPUPV;


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

        ActivityHomeBinding binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonProductsManagementPUPZ = findViewById(R.id.productsManagmentPUPZ);
        buttonProductsManagementPUPV = findViewById(R.id.productsManagmentPUPV);
        buttonServicesManagementPUPZ = findViewById(R.id.serviceManagmentPUPZ);
        buttonServicesManagementPUPV = findViewById(R.id.serviceManagmentPUPV);
        buttonPackgesManagementPUPZ = findViewById(R.id.packageManagmentPUPZ);
        buttonPackgesManagementPUPV = findViewById(R.id.packageManagmentPUPV);

        binding.productsManagmentPUPV.setOnClickListener(v ->{
            Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
            intent.putExtra("used_fragment", "product_list_pupv");
            startActivity(intent);
        });

        binding.productsManagmentPUPZ.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
            intent.putExtra("used_fragment", "product_list_pupz");
            startActivity(intent);
        });

        binding.serviceManagmentPUPZ.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ServicesManagementActivity.class);
            intent.putExtra("used_fragment", "service_list_pupz");
            startActivity(intent);
        });

        binding.serviceManagmentPUPV.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ServicesManagementActivity.class);
            intent.putExtra("used_fragment", "service_list_pupv");
            startActivity(intent);
        });

        binding.packageManagmentPUPZ.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PackagesManagementActivity.class);
            intent.putExtra("used_fragment", "package_list_pupz");
            startActivity(intent);
        });

        binding.packageManagmentPUPV.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PackagesManagementActivity.class);
            intent.putExtra("used_fragment", "package_list_pupv");
            startActivity(intent);
        });

        binding.registerButton.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, OD_RegisterActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.categoriesButton.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        binding.typesOfEventsButton.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, EventTypesActivity.class);
            startActivity(intent);
        });

        binding.homeact.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this,HomeTwoActivity.class);
            startActivity(intent);
        });
        binding.ownerDashboardBtn.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, OwnerDashboard.class);
            startActivity(intent);
        });


    }
}