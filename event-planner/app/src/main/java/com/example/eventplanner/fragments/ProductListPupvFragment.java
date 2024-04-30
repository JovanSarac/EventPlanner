package com.example.eventplanner.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.CreateProductActivity;
import com.example.eventplanner.databinding.FragmentProductListPupvBinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.eventplanner.adapters.ProductListPupvAdapter;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductListPupvFragment extends Fragment{
    View view;
    FragmentProductListPupvBinding binding;
    ArrayList<Product> products;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list_pupv, container, false);
        binding = FragmentProductListPupvBinding.inflate(getLayoutInflater());

        db = FirebaseFirestore.getInstance();

        getProducts();

        ProductListPupvAdapter productListAdapter = new ProductListPupvAdapter(requireContext(), products);

        binding.productsListPupv.setAdapter(productListAdapter);
        binding.productsListPupv.setClickable(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CreateProductActivity.class);
                startActivity(intent);
            }
        });

    }
    private void getProducts() {
        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Product product = new Product(doc.getLong("id"),
                                    doc.getLong("categoryId"),
                                    doc.getLong("subcategoryId"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getDouble("price"),
                                    doc.getDouble("discount"),
                                    new ArrayList<>(), //images
                                    new ArrayList<>(), //eventIds
                                    doc.getBoolean("availabale"),
                                    doc.getBoolean("visible"));

                            products.add(product);
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