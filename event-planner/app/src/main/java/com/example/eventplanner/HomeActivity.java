package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    private Button buttonProductsManagementPUPZ;
    private Button buttonProductsManagementPUPV;
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

        buttonProductsManagementPUPZ = findViewById(R.id.productsManagmentPUPZ);
        buttonProductsManagementPUPV = findViewById(R.id.productsManagmentPUPV);
        buttonServicesManagementPUPZ = findViewById(R.id.serviceManagmentPUPZ);
        buttonServicesManagementPUPV = findViewById(R.id.serviceManagmentPUPV);
        buttonPackgesManagementPUPZ = findViewById(R.id.packageManagmentPUPZ);
        buttonPackgesManagementPUPV = findViewById(R.id.packageManagmentPUPV);

        buttonProductsManagementPUPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
                intent.putExtra("used_fragment", "product_list_pupv");
                startActivity(intent);
                finish();
            }
        });

        buttonProductsManagementPUPZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductsManegementActivity.class);
                intent.putExtra("used_fragment", "product_list_pupz");
                startActivity(intent);
                finish();
            }
        });

        buttonServicesManagementPUPZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServicesManagementActivity.class);
                intent.putExtra("used_fragment", "service_list_pupz");
                startActivity(intent);
                finish();
            }
        });

        buttonServicesManagementPUPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServicesManagementActivity.class);
                intent.putExtra("used_fragment", "service_list_pupv");
                startActivity(intent);
                finish();
            }
        });

        buttonPackgesManagementPUPZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PackagesManagementActivity.class);
                intent.putExtra("used_fragment", "package_list_pupz");
                startActivity(intent);
                finish();
            }
        });

        buttonPackgesManagementPUPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PackagesManagementActivity.class);
                intent.putExtra("used_fragment", "package_list_pupv");
                startActivity(intent);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.product_list_framelayout, fragment);
        fragmentTransaction.commit();
    }
}