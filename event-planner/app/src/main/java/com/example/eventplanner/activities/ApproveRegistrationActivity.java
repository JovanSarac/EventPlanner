package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PupvUserCardAdapter;
import com.example.eventplanner.adapters.SubcategoriesCardAdapter;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.UserPUPV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ApproveRegistrationActivity extends AppCompatActivity {
    List<UserPUPV> dataList = new ArrayList<>();
    List<UserPUPV> filteredList = new ArrayList<>();

    List<String> categoriesFilter = new ArrayList<>();
    List<String> eventTypesFilter = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    PupvUserCardAdapter adapter;

    String selectedCategory;
    String selectedEvent;
    @Override
    protected void onResume() {

        super.onResume();
        getUsers();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_approve_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        adapter = new PupvUserCardAdapter(filteredList, ApproveRegistrationActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ApproveRegistrationActivity.this));
        recyclerView.setAdapter(adapter);

        TextInputEditText searchEditText = findViewById(R.id.search);

        findViewById(R.id.searchButton).setOnClickListener(v->{
            String s=searchEditText.getText().toString();
            filter(s);

            filterByCategoryAndEventType();

            adapter.notifyDataSetChanged();
        });


        getAllCategories();
        getAllEventTypes();

    }

    private void filterByCategoryAndEventType() {
        List<UserPUPV> temp=new ArrayList<>();
        for (UserPUPV item : filteredList) {
            if (item.getCategories().stream().anyMatch(category -> category.getName().equals(selectedCategory)) ||
                    item.getEventTypes().stream().anyMatch(category -> category.getTypeName().equals(selectedEvent))) {

                temp.add(item);
            }
        }
        filteredList.clear();
        filteredList.addAll(temp);

    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(dataList);
        } else {
            text = text.toLowerCase();
            for (UserPUPV item : dataList) {
                if (item.getFirstName().toLowerCase().contains(text) || item.getLastName().toLowerCase().contains(text) || item.getCompanyemail().toLowerCase().contains(text) || item.getEmail().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }

        }
    }

    public void getUsers() {
        dataList = new ArrayList<>();
        db.collection("User")
                .whereEqualTo("IsValid", false)
                .whereEqualTo("UserType", "PUPV")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                UserPUPV user = new UserPUPV(
                                        doc.getId(),
                                        doc.getString("FirstName"),
                                        doc.getString("LastName"),
                                        doc.getString("E-mail"),
                                        doc.getString("Password"),
                                        doc.getString("Phone"),
                                        doc.getString("Address"),
                                        doc.getBoolean("IsValid"),
                                        doc.getString("CompanyName"),
                                        doc.getString("CompanyDescription"),
                                        doc.getString("CompanyAddress"),
                                        doc.getString("CompanyEmail"),
                                        doc.getString("CompanyPhone"),
                                        doc.getString("WorkTime"),
                                        (List<String>) doc.get("EventTypes"),
                                        (List<String>) doc.get("Categories")


                                );
                                dataList.add(user);

                            }
                            getEventTypes();

                        } else {
                            Toast.makeText(ApproveRegistrationActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getEventTypes() {
        for (UserPUPV user : dataList) {
            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

            for (String id : user.getEventTypesIds()) {
                tasks.add(db.collection("EventTypes").document(id).get());
            }
            Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
            allTasks.addOnCompleteListener(new OnCompleteListener<List<DocumentSnapshot>>() {
                @Override
                public void onComplete(Task<List<DocumentSnapshot>> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> snapshots = task.getResult();

                        List<EventType> eventTypes = new ArrayList<>();

                        for (DocumentSnapshot doc : snapshots) {
                            if (doc.exists()) {
                                EventType type = new EventType(
                                        Long.parseLong(doc.getId()),
                                        doc.getBoolean("InUse"),
                                        doc.getString("Name"),
                                        doc.getString("Description"),
                                        null
                                );
                                eventTypes.add(type);
                            }
                        }
                        user.setEventTypes(eventTypes);
                        getCategories(user);
                    }
                }
            });
        }
    }

    public void getCategories(UserPUPV user){
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (String id : user.getCategoriesIds()) {
            tasks.add(db.collection("Categories").document(id).get());
        }
        Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks.addOnCompleteListener(new OnCompleteListener<List<DocumentSnapshot>>() {
            @Override
            public void onComplete(Task<List<DocumentSnapshot>> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> snapshots = task.getResult();

                    List<Category> categories = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Category category = new Category(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("Name"),
                                    doc.getString("Description")
                            );
                            categories.add(category);

                        }
                    }
                    user.setCategories(categories);
                    filteredList.add(user);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    void getAllCategories(){
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                categoriesFilter.add(doc.getString("Name"));
                            }
                            TextInputLayout textInputLayout = findViewById(R.id.category_filter);
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
                            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(ApproveRegistrationActivity.this, android.R.layout.simple_dropdown_item_1line, categoriesFilter);
                            autoCompleteTextView.setAdapter(categoryAdapter);

                            autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                                selectedCategory = (String) parent.getItemAtPosition(position);
                            });
                        } else {
                            Toast.makeText(ApproveRegistrationActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void getAllEventTypes(){
        db.collection("EventTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                eventTypesFilter.add(doc.getString("Name"));
                            }
                            TextInputLayout textInputLayout = findViewById(R.id.event_type_filter);
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
                            ArrayAdapter<String> eventTypesAdapter = new ArrayAdapter<>(ApproveRegistrationActivity.this, android.R.layout.simple_dropdown_item_1line, eventTypesFilter);
                            autoCompleteTextView.setAdapter(eventTypesAdapter);

                            autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                                selectedEvent = (String) parent.getItemAtPosition(position);
                            });
                        } else {
                            Toast.makeText(ApproveRegistrationActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}