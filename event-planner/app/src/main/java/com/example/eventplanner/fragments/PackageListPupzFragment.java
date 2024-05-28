package com.example.eventplanner.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PackageListPupvAdapter;
import com.example.eventplanner.databinding.FragmentPackageListPupzBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.eventplanner.adapters.PackageListAdapter;

import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PackageListPupzFragment extends Fragment {
    View view;
    FragmentPackageListPupzBinding binding;
    ArrayList<Package> packages;
    FirebaseFirestore db;
    FirebaseStorage storage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackageListPupzBinding.inflate(getLayoutInflater());

        packages = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        getPackages();

        return binding.getRoot();
    }

    @NonNull
    private void getPackages() {
        db.collection("Packages")
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<DocumentSnapshot> packageDocs = task.getResult().getDocuments();
                            final int numProducts = packageDocs.size();
                            final int[] productsProcessed = {0};

                            for (DocumentSnapshot doc : packageDocs) {
                                Package packagee = new Package(/*
                                        Long.parseLong(doc.getId()),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getLong("categoryId"),
                                        (ArrayList<Long>) doc.get("subcategoryIds"),
                                        (ArrayList<Long>) doc.get("productIds"),
                                        (ArrayList<Long>) doc.get("serviceIds"),
                                        (ArrayList<Long>) doc.get("eventIds"),
                                        ((Number) doc.get("price")).doubleValue(),
                                        new ArrayList<>(), //images
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("deleted")*/);

                                ArrayList<String> imageUrls = (ArrayList<String>) doc.get("imageUrls");
                                final int numImages = imageUrls.size();

                                for (String imageUrl : imageUrls) {
                                    StorageReference imageRef = storage.getReference().child(imageUrl);
                                    imageRef.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    packagee.getImages().add(uri);

                                                    if (packagee.getImages().size() == numImages) {
                                                        productsProcessed[0]++;

                                                        if (productsProcessed[0] == numProducts) {
                                                            PackageListAdapter productListAdapter = new PackageListAdapter(requireContext(), packages);
                                                            binding.packageListPupz.setAdapter(productListAdapter);
                                                            binding.packageListPupz.setClickable(true);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }

                                packages.add(packagee);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch products: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}