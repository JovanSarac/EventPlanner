package com.example.eventplanner.activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.eventplanner.adapters.AgendaListAdapter;
import com.example.eventplanner.adapters.EventRecyclerViewAdapter;
import com.example.eventplanner.adapters.SubcategoryListAdapter;
import com.example.eventplanner.databinding.FragmentAddGuestBinding;
import com.example.eventplanner.databinding.FragmentAddSubcategoryOnBudgetPlannerBinding;
import com.example.eventplanner.databinding.FragmentCreateAgendaBinding;
import com.example.eventplanner.databinding.FragmentSearchPspBinding;
import com.example.eventplanner.fragments.AddSubcategoryOnBudgetPlannerFragment;
import com.example.eventplanner.model.AgendaActivity;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventType;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowOneEventActivity extends AppCompatActivity {

    com.example.eventplanner.databinding.ActivityShowOneEventBinding binding;

    FragmentAddSubcategoryOnBudgetPlannerBinding bindingAdd;

    FragmentCreateAgendaBinding bindingAgenda;

    FragmentAddGuestBinding bindingGuest;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;

    RecyclerView recyclerViewAgenda;
    Long eventId;

    private List<EventType> itemList=new ArrayList<>();

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

        recyclerViewAgenda = binding.agendaActivityView;
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerViewAgenda.setLayoutManager(layoutManager2);
        getAgendaActivities(eventId);




        binding.addSubCategory.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ShowOneEventActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_subcategory_on_budget_planner, null);

            bindingAdd = FragmentAddSubcategoryOnBudgetPlannerBinding.bind(dialogView);

            getEventTypes();

            bindingAdd.saveAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCreteSubcategoryPlanner(bindingAdd.categoryInput, bindingAdd.subcategoryInput, bindingAdd.priceInput))
                        return;

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

        binding.addAgendaActivity.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ShowOneEventActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_create_agenda, null);

            bindingAgenda = FragmentCreateAgendaBinding.bind(dialogView);
            bindingAgenda.createAgendaActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = bindingAgenda.nameAgendaActivity.getText().toString();
                    String description = bindingAgenda.descriptionAgendaActivity.getText().toString();
                    String durationFrom = bindingAgenda.durationFrom.getText().toString();
                    String durationTo = bindingAgenda.durationTo.getText().toString();
                    String address = bindingAgenda.addressAgendaActivity.getText().toString();

                    getAgendaActivityId(new AgendaActivity(0L, eventId, name,description,durationFrom,durationTo,address));

                    bottomSheetDialog.dismiss();

                }
            });

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.addGuest.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ShowOneEventActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_guest, null);

            bindingGuest = FragmentAddGuestBinding.bind(dialogView);
            /*bindingAgenda.createAgendaActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = bindingAgenda.nameAgendaActivity.getText().toString();
                    String description = bindingAgenda.descriptionAgendaActivity.getText().toString();
                    String durationFrom = bindingAgenda.durationFrom.getText().toString();
                    String durationTo = bindingAgenda.durationTo.getText().toString();
                    String address = bindingAgenda.addressAgendaActivity.getText().toString();

                    getAgendaActivityId(new AgendaActivity(0L, eventId, name,description,durationFrom,durationTo,address));

                    bottomSheetDialog.dismiss();

                }
            });*/

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });



    }

    private Long getAgendaActivityId(AgendaActivity agendaActivity){
        Long idActivity = 0L;
        db.collection("AgendaActivities")
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
                            }
                            long newSubcategoryPlannerId = Long.parseLong(lastDocumentId) + 1;
                            agendaActivity.setId(newSubcategoryPlannerId);
                            createAgendaActivity(agendaActivity);
                        }else{
                            agendaActivity.setId(1L);
                            createAgendaActivity(agendaActivity);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error counting existing events", e);
                    }
                });

        return idActivity;
    }

    private void createAgendaActivity(AgendaActivity agendaActivity) {
        Map<String, Object> elememt = new HashMap<>();
        elememt.put("id",agendaActivity.getId());
        elememt.put("eventId",agendaActivity.getEventId());
        elememt.put("name", agendaActivity.getName());
        elememt.put("description", agendaActivity.getDescription());
        elememt.put("durationFrom", agendaActivity.getDurationFrom());
        elememt.put("durationTo", agendaActivity.getDurationTo());
        elememt.put("address", agendaActivity.getAddress());



        // Add a new document with a generated ID
        db.collection("AgendaActivities").document(agendaActivity.getId().toString())
                .set(elememt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + agendaActivity.getId().toString());
                        getAgendaActivities(eventId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void getAgendaActivities(long eventId) {
        ArrayList<AgendaActivity> agendaActivities = new ArrayList<>();
        db.collection("AgendaActivities")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            AgendaActivity agendaActivity = new AgendaActivity(doc.getLong("id"),
                                    doc.getLong("eventId"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getString("durationFrom"),
                                    doc.getString("durationTo"),
                                    doc.getString("address"));

                            agendaActivities.add(agendaActivity);
                        }

                        AgendaListAdapter adapterRecycle = new AgendaListAdapter(agendaActivities);
                        recyclerViewAgenda.setAdapter(adapterRecycle);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private boolean validateCreteSubcategoryPlanner(AutoCompleteTextView categoryInput, AutoCompleteTextView subcategoryInput, TextInputEditText priceInput) {
        boolean error=false;
        if(TextUtils.isEmpty(categoryInput.getText())){
            categoryInput.setError("Select option!");
            error=true;
        }
        if(TextUtils.isEmpty(subcategoryInput.getText())){
            subcategoryInput.setError("Fill textfield!");
            error=true;
        }
        if(TextUtils.isEmpty(priceInput.getText())){
            priceInput.setError("Fill textfield!");
            error=true;
        }
        else{
            try {
                int price = Integer.parseInt(priceInput.getText().toString());
                if(price <= 0){
                    priceInput.setError("Price must be >0!");
                }
            }catch (Exception e){
                priceInput.setError("Price must be integer!");
                error = true;
            }
        }

        if(error) return true;

        return false;
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

        return  subcategoryPlanners;
    }

    private void getEventTypes() {
        itemList=new ArrayList<>();
        db.collection("EventTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> taskEvent) {
                        if (taskEvent.isSuccessful()) {
                            // Process eventType documents

                            // Perform subcategories query
                            db.collection("Subcategories")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskSubcategories) {
                                            if (taskSubcategories.isSuccessful()) {
                                                for(DocumentSnapshot docEvent: taskEvent.getResult()){
                                                    List<String> subcategoryIds=(List<String>)docEvent.get("Subcategories");

                                                    List<Subcategory> subcategories = new ArrayList<>();
                                                    for(DocumentSnapshot doc: taskSubcategories.getResult()){
                                                        Long num=Long.parseLong(doc.getId());
                                                        if(subcategoryIds.contains(num.toString())){
                                                            Subcategory subcategory = new Subcategory(
                                                                    Long.parseLong(doc.getId()),
                                                                    doc.getString("CategoryName"),
                                                                    doc.getString("Name"),
                                                                    doc.getString("Description"),
                                                                    doc.getLong("Type").intValue()
                                                            );
                                                            subcategories.add(subcategory);
                                                        }

                                                    }
                                                    EventType type = new EventType(
                                                            Long.parseLong(docEvent.getId()),
                                                            docEvent.getBoolean("InUse"),
                                                            docEvent.getString("Name"),
                                                            docEvent.getString("Description"),
                                                            subcategories
                                                    );
                                                    itemList.add(type);

                                                }

                                                EventType et = new EventType();
                                                System.out.println(getIntent().getStringExtra("eventType"));
                                                for(EventType e: itemList){
                                                    if(e.getTypeName().equals(getIntent().getStringExtra("eventType"))){
                                                        et = e;
                                                    }
                                                }

                                                List<Subcategory> subcat = et.getRecomendedSubcategories();
                                                ArrayList<Subcategory> subcategoriesArrayList = new ArrayList<>(subcat);

                                                ArrayList<String> Category = new ArrayList<>();
                                                ArrayList<String> Subcategories = new ArrayList<>();

                                                for(Subcategory sub : subcategoriesArrayList){
                                                    Category.add(sub.getCategoryName());
                                                    Subcategories.add(sub.getName());
                                                }

                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowOneEventActivity.this, android.R.layout.simple_dropdown_item_1line, Category);
                                                bindingAdd.categoryInput.setAdapter(adapter);

                                                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(ShowOneEventActivity.this, android.R.layout.simple_dropdown_item_1line, Subcategories);
                                                bindingAdd.subcategoryInput.setAdapter(adapter2);

                                                bindingAdd.categoryInput.setOnItemClickListener((parent, view, position, id) -> {
                                                    String selectedCategory = (String) parent.getItemAtPosition(position);
                                                    System.out.println("Selected category: " + selectedCategory);

                                                });


                                                bindingAdd.subcategoryInput.setOnItemClickListener((parent, view, position, id) -> {
                                                    String selectedSubcategory = (String) parent.getItemAtPosition(position);
                                                    System.out.println("Selected subcategory: " + selectedSubcategory);

                                                });



                                            }
                                        }

                                    });
                        }
                    }
                });
    }
}