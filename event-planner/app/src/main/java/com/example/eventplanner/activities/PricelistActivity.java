package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PricelistAdapter;
import com.example.eventplanner.databinding.ActivityPricelistBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Package;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PricelistActivity extends AppCompatActivity {

    View view;
    ActivityPricelistBinding binding;
    ArrayList<Product> products;
    ArrayList<Service> services;
    ArrayList<Package> packages;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPricelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        services = new ArrayList<>();
        packages = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        getProducts();
    }

    private void components(){
        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getProducts(){
        db.collection("Products")
                //.whereEqualTo("pupvId", 1)
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Product product = new Product(
                                    Long.parseLong(doc.getId()),
                                    1l,
                                    doc.getLong("categoryId"),
                                    doc.getLong("subcategoryId"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getDouble("price"),
                                    doc.getDouble("discount"),
                                    new ArrayList<>(),
                                    (ArrayList<Long>) doc.get("eventTypeIds"),
                                    doc.getBoolean("available"),
                                    doc.getBoolean("visible"),
                                    doc.getBoolean("pending"),
                                    doc.getBoolean("deleted")
                            );

                            products.add(product);
                        }

                        PricelistAdapter<Product> productPricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, products);
                        binding.productList.setAdapter(productPricelistAdapter);

                        getServices();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getServices(){
        db.collection("Services")
                //.whereEqualTo("pupvId", 1)
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Service service = new Service(
                                        Long.parseLong(doc.getId()),
                                        1l,
                                        doc.getLong("categoryId"),
                                        doc.getLong("subcategoryId"),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        new ArrayList<>(), //images
                                        doc.getString("specific"),
                                        ((Number) doc.get("pricePerHour")).doubleValue(),
                                        ((Number) doc.get("fullPrice")).doubleValue(),
                                        ((Number) doc.get("duration")).doubleValue(),
                                        ((Number) doc.get("durationMin")).doubleValue(),
                                        ((Number) doc.get("durationMax")).doubleValue(),
                                        doc.getString("location"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        (ArrayList<Long>) doc.get("pupIds"),
                                        (ArrayList<Long>) doc.get("eventTypeIds"),
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getBoolean("pending"),
                                        doc.getBoolean("deleted"));

                            services.add(service);
                        }

                        PricelistAdapter<Service> servicePricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, services);
                        binding.serviceList.setAdapter(servicePricelistAdapter);

                        getPackages();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getPackages(){
        db.collection("Packages")
                //.whereEqualTo("pupvId", 1)
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Package packagee = new Package(
                                        Long.parseLong(doc.getId()),
                                        1l,
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getLong("categoryId"),
                                        (ArrayList<Long>) doc.get("subcategoryIds"),
                                        (ArrayList<Long>) doc.get("productIds"),
                                        (ArrayList<Long>) doc.get("serviceIds"),
                                        (ArrayList<Long>) doc.get("eventTypeIds"),
                                        ((Number) doc.get("price")).doubleValue(),
                                        new ArrayList<>(), //images
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("deleted"));

                            packages.add(packagee);

                            components();
                        }

                        PricelistAdapter<Package> packagePricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, packages);
                        binding.packageList.setAdapter(packagePricelistAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}