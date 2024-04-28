package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ExpandableListAdapter;

import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Category> listDataHeader;
    HashMap<Category, List<Subcategory>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.addRecomendedCategoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SuggestedSubcategoriesActivity.class);
                startActivity(intent);
            }
        });


        expandableListView = findViewById(R.id.expandableListView);
        prepareListData();

        expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;

            }
        });

        Button addCategory = findViewById(R.id.addCategoryButton);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, EditCategoryActivity.class);
                intent.putExtra("categoryName", "");
                intent.putExtra("categoryDescription", "");
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });

    }
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add(new Category("Group 1","Opis 1"));
        listDataHeader.add(new Category("Group 2","Opis 2"));
        listDataHeader.add(new Category("Group 3","Opis 3"));
        listDataHeader.add(new Category("Group 11","Opis 1"));
        listDataHeader.add(new Category("Group 21","Opis 2"));
        listDataHeader.add(new Category("Group 31","Opis 3"));
        listDataHeader.add(new Category("Group 12","Opis 1"));
        listDataHeader.add(new Category("Group 22","Opis 2"));
        listDataHeader.add(new Category("Group 32","Opis 3"));
        listDataHeader.add(new Category("Group 13","Opis 1"));
        listDataHeader.add(new Category("Group 23","Opis 2"));
        listDataHeader.add(new Category("Group 33","Opis 3"));
        // Adding child data
        List<Subcategory> group1 = new ArrayList<>();
        group1.add(new Subcategory("Group 1","Item 1","opis1",0));
        group1.add(new Subcategory("Group 1","Item 2","opis2",0));
        group1.add(new Subcategory("Group 1","Item 3","opis3",0));

        List<Subcategory> group2 = new ArrayList<>();
        group2.add(new Subcategory("Group 2","Item 4","opis1",0));
        group2.add(new Subcategory("Group 2","Item 5","opis1",1));
        group2.add(new Subcategory("Group 2","Item 6","opis1",1));

        List<Subcategory> group3 = new ArrayList<>();
        group3.add(new Subcategory("Group 3","Item 7","opis1",1));
        group3.add(new Subcategory("Group 3","Item 8","opis1",1));
        group3.add(new Subcategory("Group 3","Item 9","opis1",0));

        listDataChild.put(listDataHeader.get(0), group1);
        listDataChild.put(listDataHeader.get(1), group2);
        listDataChild.put(listDataHeader.get(2), group3);
    }



}