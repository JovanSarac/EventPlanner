package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductListPupvAdapter;
import com.example.eventplanner.databinding.FragmentProductListPupzBinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class ProductListPupzFragment extends Fragment {
    View view;
    FragmentProductListPupzBinding binding;
    ArrayList<Product> products;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_list_pupz, container,false);
        binding = FragmentProductListPupzBinding.inflate(getLayoutInflater());

        db = FirebaseFirestore.getInstance();

        getProducts();

        return binding.getRoot();
    }

    private void getProducts() {
        products = new ArrayList<>();

        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Product product = new Product(
                                    Long.parseLong(doc.getId()),
                                    doc.getLong("categoryId"),
                                    doc.getLong("subcategoryId"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    ((Number) doc.get("price")).doubleValue() ,
                                    ((Number) doc.get("discount")).doubleValue(),
                                    new ArrayList<>(), //images
                                    (ArrayList<Long>) doc.get("eventIds"),
                                    doc.getBoolean("available"),
                                    doc.getBoolean("visible"));

                            products.add(product);
                        }

                        ProductListAdapter productListAdapter = new ProductListAdapter(requireContext(), products);

                        binding.productsListPupz.setAdapter(productListAdapter);
                        binding.productsListPupz.setClickable(true);
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