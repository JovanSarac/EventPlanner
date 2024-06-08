package com.example.eventplanner.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ImageAdapter;
import com.example.eventplanner.databinding.ActivityShowOneProductBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.UserPUPV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ShowOneProductActivity extends AppCompatActivity {

    ActivityShowOneProductBinding binding;
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;

    Long idProduct;
    String idPupv;

    Category category;
    Subcategory subcategory;

    UserPUPV userPUPV;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowOneProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idProduct = getIntent().getLongExtra("productId", 0L);
        idPupv = getIntent().getStringExtra("pupvId");
        getUserPupv(idPupv).thenAccept(userPUPV -> {
            this.userPUPV = userPUPV;
        });
        Long idCategory = getIntent().getLongExtra("categoryId", 0L);
        Long idSubcategory = getIntent().getLongExtra("subcategoryId", 0L);
        ArrayList<Uri> images = getIntent().getParcelableArrayListExtra("images");
        getCategory(idCategory);
        getSubcategory(idSubcategory);

        recyclerView = findViewById(R.id.recycler);
        imageAdapter = new ImageAdapter(ShowOneProductActivity.this, R.layout.image_carousel_card_without_button,images);
        recyclerView.setAdapter(imageAdapter);



        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        double discount = getIntent().getDoubleExtra("discount", 0.0);
        boolean available = getIntent().getBooleanExtra("available", false);
        ArrayList<String> eventTypeIds = getIntent().getStringArrayListExtra("eventTypeIds");
        getEventTypesName(eventTypeIds);




        binding.nameProductt.setText(name);
        binding.descriptionProduct.setText(description);
        binding.priceProduct.setText(String.valueOf(price) + " $");
        binding.discountPrice.setText(String.valueOf(discount) + " %");
        binding.priceWithDiscount.setText(String.valueOf(price - (price * discount/100)) + " $");
        if(available){
            binding.availability.setChecked(true);

            //TO DO implementirati kupovinu proizovda
            binding.buyProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else{
            binding.buyProduct.setVisibility(View.GONE);
        }


    }

    private CompletableFuture<UserPUPV> getUserPupv(String uid) {
        CompletableFuture<UserPUPV> future = new CompletableFuture<>();

        db.collection("User").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserPUPV userPupvv = new UserPUPV();
                                userPupvv.setFirstName((String) document.get("FirstName"));
                                userPupvv.setLastName((String) document.get("LastName"));
                                userPupvv.setEmail((String) document.get("E-mail"));
                                userPupvv.setPassword((String) document.get("Password"));
                                userPupvv.setPhone((String) document.get("Phone"));
                                userPupvv.setAddress((String) document.get("Address"));
                                userPupvv.setValid((Boolean) document.get("IsValid"));
                                userPupvv.setCompanyName((String) document.get("CompanyName"));
                                userPupvv.setCompanyDescription((String) document.get("CompanyDescription"));
                                userPupvv.setCompanyAddress((String) document.get("CompanyAddress"));
                                userPupvv.setCompanyemail((String) document.get("CompanyEmail"));
                                userPupvv.setCompanyPhone((String) document.get("CompanyPhone"));
                                userPupvv.setWorkTime((String) document.get("WorkTime"));

                                userPUPV = userPupvv;

                                future.complete(userPupvv);
                            } else {
                                Log.e("HomeTwoActivity", "No such document");
                                future.completeExceptionally(new Exception("No such document"));
                            }
                        } else {
                            Log.e("HomeTwoActivity", "Error getting document", task.getException());
                            future.completeExceptionally(task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("HomeTwoActivity", "Error getting document", e);
                        future.completeExceptionally(e);
                    }
                });

        return future;
    }

    private void getEventTypesName(ArrayList<String> eventTypeIds) {
        if(!eventTypeIds.isEmpty()){
            db.collection("EventTypes")
                    .whereIn(FieldPath.documentId(), eventTypeIds)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<String> eventTypeName = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("Name");
                                eventTypeName.add(name);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    this,
                                    R.layout.list_item_layout_white,
                                    eventTypeName
                            );


                            binding.eventTypesList.setAdapter(adapter);

                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    });
        }
    }

    private void getSubcategory(Long idSubcategory) {
        db.collection("Subcategories").document(idSubcategory.toString()).
                get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String categoryName = document.getString("CategoryName");
                                String name = document.getString("Name");
                                String description = document.getString("Description");
                                Long type1 = document.getLong("Type");
                                subcategory = new Subcategory(idSubcategory, categoryName, name, description, type1.intValue());
                                binding.subcategoryProduct.setText(subcategory.getName());
                            } else {
                                Log.d("Category", "No such document");
                            }
                        } else {
                            Log.d("Category", "get failed with ", task.getException());
                        }
                    }
                });

    }

    private void getCategory(Long idCategory) {
        db.collection("Categories").document(idCategory.toString()).
                get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("Name");
                                String description = document.getString("Description");
                                category = new Category(idCategory, name, description);
                                binding.categoryProduct.setText(category.getName());
                            } else {
                                Log.d("Category", "No such document");
                            }
                        } else {
                            Log.d("Category", "get failed with ", task.getException());
                        }
                    }
                });


    }
}