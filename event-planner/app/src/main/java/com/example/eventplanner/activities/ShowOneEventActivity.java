package com.example.eventplanner.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventRecyclerViewAdapter;
import com.example.eventplanner.databinding.FragmentAddSubcategoryOnBudgetPlannerBinding;
import com.example.eventplanner.databinding.FragmentSearchPspBinding;
import com.example.eventplanner.fragments.AddSubcategoryOnBudgetPlannerFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.eventplanner.adapters.SubAndCategoryTableRowAdapter;

import com.example.eventplanner.model.SubcategoryPlanner;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowOneEventActivity extends AppCompatActivity {

    com.example.eventplanner.databinding.ActivityShowOneEventBinding binding;

    FragmentAddSubcategoryOnBudgetPlannerBinding bindingAdd;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;
    Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = com.example.eventplanner.databinding.ActivityShowOneEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String idEvent = getIntent().getStringExtra("eventId");
        eventId = Long.parseLong(idEvent);
        String eventName = getIntent().getStringExtra("eventName");
        String eventDescription = getIntent().getStringExtra("eventDescription");
        String eventLocation = getIntent().getStringExtra("eventLocation");
        String eventDistanceLocation = getIntent().getStringExtra("eventDistanceLocation");
        String eventType = getIntent().getStringExtra("eventType");
        String eventDate = getIntent().getStringExtra("eventDate");

        binding.nameEvent.setText(eventName);
        binding.descriptionEvent.setText(eventDescription);
        binding.locationEvent.setText(eventLocation);
        binding.locationDistanceEvent.setText(eventDistanceLocation);
        binding.typeEvent.setText(eventType);
        binding.dateEvent.setText(eventDate);





        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.planned.setVisibility(View.VISIBLE);
                        binding.spent.setVisibility(View.GONE);
                        break;
                    case 1:
                        binding.planned.setVisibility(View.GONE);
                        binding.spent.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Ovdje ne trebate ništa raditi, jer se ne treba reagirati na odabir drugih tabova
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Ovdje ne trebate ništa raditi, jer se ne treba reagirati na ponovni odabir taba
            }
        });

        recyclerView = binding.subcategoryAndCategoryRow;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<SubcategoryPlanner> subcategories = getSubcategoryPlanner();



        binding.addSubCategory.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ShowOneEventActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_subcategory_on_budget_planner, null);

            bindingAdd = FragmentAddSubcategoryOnBudgetPlannerBinding.bind(dialogView);

            String[] Category = {"Ugostiteljski objekti, hrana, ketering, torte i kolači", "Muzika i zabava", "Smjestaj", "Logistika i obezbeđenje"};
            String[] Subcategories = {"Subcategory", "Hrana za događaje", "Ketering i priprema hrane", "Iznajmljivanje ugostiteljskih objekata za događaje", "Fotografisanje"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Category);
            bindingAdd.categoryInput.setAdapter(adapter);

            // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
            bindingAdd.categoryInput.setOnItemClickListener((parent, view, position, id) -> {
                //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                String selectedCategory = (String) parent.getItemAtPosition(position);

                System.out.println("Selected category: " + selectedCategory);
            });

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Subcategories);
            bindingAdd.subcategoryInput.setAdapter(adapter2);

            // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
            bindingAdd.subcategoryInput.setOnItemClickListener((parent, view, position, id) -> {
                //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                String selectedSubcategory = (String) parent.getItemAtPosition(position);

                System.out.println("Selected category: " + selectedSubcategory);
            });

            bindingAdd.saveAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = bindingAdd.categoryInput.getText().toString();
                    String subcategory = bindingAdd.subcategoryInput.getText().toString();
                    Float price = Float.parseFloat(String.valueOf(bindingAdd.priceInput.getText()));

                    createSubcategoryPlanner(new SubcategoryPlanner(0L,category,subcategory,price,eventId));

                    bottomSheetDialog.dismiss();

                }
            });


            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

    }

    private void createSubcategoryPlanner(SubcategoryPlanner subcategoryPlanner) {
        db.collection("SubcategoryPlanner")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                            String lastDocumentId = new String();
                            if (!documents.isEmpty()) {
                                DocumentSnapshot lastDocument = documents.get(documents.size() - 1);

                                lastDocumentId = lastDocument.getId();

                                System.out.println(lastDocumentId);
                            }
                            long newSubcategoryPlannerId = Long.parseLong(lastDocumentId) + 1;

                            subcategoryPlanner.setSerialNum(newSubcategoryPlannerId);
                            saveSubcategoryPlannerToFirestore(subcategoryPlanner);

                        }else{
                            subcategoryPlanner.setSerialNum(1L);
                            saveSubcategoryPlannerToFirestore(subcategoryPlanner);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error counting existing events", e);
                    }
                });
    }

    private void saveSubcategoryPlannerToFirestore(SubcategoryPlanner subcategoryPlanner){

        Map<String, Object> elememt = new HashMap<>();
        elememt.put("id",subcategoryPlanner.getSerialNum());
        elememt.put("nameCategory", subcategoryPlanner.getNameCategory());
        elememt.put("nameSubcategory", subcategoryPlanner.getNameSubcategory());
        elememt.put("price", subcategoryPlanner.getPrice());
        elememt.put("eventId", subcategoryPlanner.getEventId());


        // Add a new document with a generated ID
        db.collection("SubcategoryPlanner").document(subcategoryPlanner.getSerialNum().toString())
                .set(elememt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + subcategoryPlanner.getSerialNum());
                        getSubcategoryPlanner();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public ArrayList<SubcategoryPlanner> getSubcategoryPlanner() {
        ArrayList<SubcategoryPlanner> subcategoryPlanners = new ArrayList<>();

        db.collection("SubcategoryPlanner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            SubcategoryPlanner planner = new SubcategoryPlanner(doc.getLong("id"),
                                    doc.getString("nameCategory"),
                                    doc.getString("nameSubcategory"),
                                    Float.parseFloat(String.valueOf(doc.getLong("price"))),
                                    doc.getLong("eventId"));

                            //System.out.println(event.getName());


                            subcategoryPlanners.add(planner);
                        }
                        ArrayList<SubcategoryPlanner> filtriraniPlanners = new ArrayList<>();
                        double suma = 0;
                        for (SubcategoryPlanner plan : subcategoryPlanners) {
                            if (plan.getEventId() == eventId) {

                                filtriraniPlanners.add(plan);
                                suma += plan.getPrice();
                            }
                        }

                        SubAndCategoryTableRowAdapter adapterRecycle = new SubAndCategoryTableRowAdapter(filtriraniPlanners);
                        recyclerView.setAdapter(adapterRecycle);

                        binding.totalPlanned.setText(String.valueOf(suma));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        /*subcategoryPlanners.add(new SubcategoryPlanner("1","Foto i video","Fotografisanje", "5000"));
        subcategoryPlanners.add(new SubcategoryPlanner("2","Foto i video","Fotografije i albumi", "5000"));
        subcategoryPlanners.add(new SubcategoryPlanner("3","Ugostiteljski objekti,\n" +
                "hrana, ketering, torte\n" +
                "i kolači","Iznajmljivanje\n" +
                "ugostiteljskih\n" +
                "objekata za događaje", "400000"));
        subcategoryPlanners.add(new SubcategoryPlanner("4","Dekoracija i rasvjeta","Dekoracija stolova", "0"));*/

        return  subcategoryPlanners;
    }
}