package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import adapters.EventTypesListAdapter;
import model.EventType;
import model.Subcategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventTypesActivity extends AppCompatActivity {
    private ListView listView;
    private EventTypesListAdapter adapter;
    private List<EventType> itemList;
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

        itemList = new ArrayList<>();
        itemList.add(new EventType("Ime","Opis",new ArrayList<>()));
        itemList.add(new EventType("Ime1","Opis1",new ArrayList<>()));
        itemList.add(new EventType("Ime2","Opis2",new ArrayList<>()));
        itemList.add(new EventType("Ime3","Opis3",new ArrayList<>()));
        itemList.add(new EventType("Ime11","Opis11",new ArrayList<>()));
        itemList.add(new EventType("Ime12","Opis12",new ArrayList<>()));
        itemList.add(new EventType("Ime22","Opis22",new ArrayList<>()));
        itemList.add(new EventType("Ime32","Opis32",new ArrayList<>()));
        itemList.add(new EventType("Ime112","Opis11",new ArrayList<>()));
        itemList.add(new EventType("Ime122","Opis12",new ArrayList<>()));
        itemList.add(new EventType("Ime222","Opis22",new ArrayList<>()));
        itemList.add(new EventType("Ime322","Opis32",new ArrayList<>()));
        itemList.add(new EventType("Ime422","Opis42",Arrays.asList(new Subcategory("neest","Home Cleaning","des",0),
                new Subcategory("neest","Pest Control Services","des",0))));

        adapter = new EventTypesListAdapter(this, itemList);
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