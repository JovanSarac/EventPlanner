package com.example.eventplanner.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ExpandableListAdapter;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PUPV_RegisterCategoryActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Category> categories =new ArrayList<>();
    List<String> categoriesList =new ArrayList<>(); //Arrays.asList(
//            "Plumbing Services",
//            "Electrical Services",
//            "Home Cleaning",
//            "Gardening Services",
//            "Painting Services",
//            "Appliance Repair",
//            "Computer Repair",
//            "Mobile Phone Repair",
//            "Car Mechanics",
//            "Locksmith Services",
//            "Carpentry Services",
//            "Roofing Services",
//            "Pest Control Services",
//            "Moving Services",
//            "Event Planning",
//            "Photography Services",
//            "Tutoring Services",
//            "Fitness Training",
//            "Legal Services",
//            "Financial Consultancy"
//    );
    List<EventType> eventTypes =new ArrayList<>();
    List<String> eventNames =new ArrayList<>();// Arrays.asList(
//            "Birthday Party",
//            "Wedding Ceremony",
//            "Graduation Celebration",
//            "Baby Shower",
//            "Housewarming Party",
//            "Anniversary Dinner",
//            "Retirement Party",
//            "Engagement Party",
//            "Prom Night",
//            "Holiday Gathering",
//            "Farewell Party",
//            "Reunion",
//            "Barbecue Cookout",
//            "Fundraising Gala",
//            "Business Conference",
//            "Product Launch",
//            "Music Concert",
//            "Art Exhibition",
//            "Film Screening",
//            "Fashion Show"
//    );
    String selectedImage;
    ListView selectedCategories;
    ListView selectedEvents;
    Map<String,Object> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pupv_register_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setupListViewForCategories();
        getCategories();
        //setupListViewForEvents();
        getEventTypes();

        item = new HashMap<>();
        item = (Map) getIntent().getSerializableExtra("object");
        selectedImage=getIntent().getStringExtra("pathImage");

        selectedCategories = findViewById(R.id.categoryView);
        Button registerButton = (Button) findViewById(R.id.registerUser);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSelectedItems(v);
                createUserPUPV();
            }
        });
    }
    private void createUserPUPV(){
        SparseBooleanArray checkedPositionsCategories = selectedCategories.getCheckedItemPositions();
        List<String> idCategories=new ArrayList<>();
        for (int i=0;i<categories.size();i++) {
            if(checkedPositionsCategories.get(i)){
                idCategories.add(categories.get(i).getId().toString());
            }

        }
        SparseBooleanArray checkedPositionsEvents = selectedEvents.getCheckedItemPositions();
        List<String> idEventTypes=new ArrayList<>();
        for (int i=0;i<eventTypes.size();i++) {
            if(checkedPositionsEvents.get(i)){
                idEventTypes.add(eventTypes.get(i).getId().toString());
            }
        }

        item.put("Categories",idCategories);
        item.put("EventTypes",idEventTypes);
        item.put("UserType","PUPV");
        Long id = new Random().nextLong();
        db.collection("User")
                .document(id.toString())
                .set(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(selectedImage!="")uploadImage(id.toString());
                        Toast.makeText(PUPV_RegisterCategoryActivity.this, "User registered", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void setupListViewForCategories() {
        selectedCategories = findViewById(R.id.categoryView);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categoriesList);
        selectedCategories.setAdapter(adapterCategories);
    }
    private void setupListViewForEvents() {
        selectedEvents = findViewById(R.id.eventView);
        ArrayAdapter<String> adapterEvents = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, eventNames);
        selectedEvents.setAdapter(adapterEvents);
    }

    private void uploadImage(String userId){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        Uri fileUri = Uri.parse(selectedImage);
        StorageReference imageRef = storageRef.child("images/" + userId);

        UploadTask uploadTask = imageRef.putFile(fileUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image uploaded successfully
                Log.d("TAG", "Image uploaded successfully");

                // Get the download URL for the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Handle the download URL (e.g., save it to a database)
                        String imageUrl = downloadUri.toString();
                        Log.d("TAG", "Download URL: " + imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle unsuccessful uploads
                Log.e("TAG", "Error uploading image", e);
            }
        });
    }

    private void getCategories() {
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Category category = new Category(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("Name"),
                                    doc.getString("Description")
                            );
                            categoriesList.add(category.getName());
                            categories.add(category);

                        }
                        setupListViewForCategories();

                    }
                });

    }
    private void getEventTypes(){
        db.collection("EventTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            EventType type = new EventType(
                                    Long.parseLong(doc.getId()),
                                    doc.getBoolean("InUse"),
                                    doc.getString("Name"),
                                    doc.getString("Description"),
                                    new ArrayList<>()
                            );
                            eventNames.add(type.getTypeName());
                            eventTypes.add(type);

                        }
                        setupListViewForEvents();

                    }
                });
    }
}