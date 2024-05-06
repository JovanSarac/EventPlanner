package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.eventplanner.adapters.EventTypesListAdapter;
import com.example.eventplanner.adapters.ExpandableListAdapter;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventTypesActivity extends AppCompatActivity {
    private ListView listView;
    private EventTypesListAdapter adapter;
    private List<EventType> itemList=new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_types);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button addEventType = (Button) findViewById(R.id.addType);
        addEventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to navigate to the second activity
                Intent intent = new Intent(EventTypesActivity.this, AddEventTypesActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_view);
        getEventTypes();

    }


    private void getEventTypes() {
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

                                                setUpAdapter();
                                            } else {
                                                // Handle subcategories query failure
                                                Toast.makeText(EventTypesActivity.this, "Error fetching subcategories: " + taskSubcategories.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Handle eventTypes query failure
                            Toast.makeText(EventTypesActivity.this, "Error fetching event types: " + taskEvent.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void setUpAdapter(){
        adapter = new EventTypesListAdapter(EventTypesActivity.this, itemList);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EventTypesListAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(EventTypesActivity.this, AddEventTypesActivity.class);
                intent.putExtra("editButtonFlag", true);
                intent.putExtra("typeName", itemList.get(position).getTypeName());
                intent.putExtra("typeDecription", itemList.get(position).getTypeDescription());

                ArrayList<String> temp_list=new ArrayList<>();
                for (Subcategory sub:itemList.get(position).getRecomendedSubcategories()) {
                    temp_list.add(sub.getName());
                }

                intent.putStringArrayListExtra("recomendedSubcategories",temp_list);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                Toast.makeText(EventTypesActivity.this, "Delete is clicked on"+ itemList.get(position).getTypeName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}