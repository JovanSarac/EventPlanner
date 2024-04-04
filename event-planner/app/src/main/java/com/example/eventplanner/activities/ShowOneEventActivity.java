package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditProductBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import adapters.SubAndCategoryTableRowAdapter;
import adapters.SubcategoryListAdapter;
import model.Subcategory;
import model.SubcategoryPlanner;

public class ShowOneEventActivity extends AppCompatActivity {

    com.example.eventplanner.databinding.ActivityShowOneEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = com.example.eventplanner.databinding.ActivityShowOneEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

        RecyclerView recyclerView = binding.subcategoryAndCategoryRow;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<SubcategoryPlanner> subcategories = createSubcategoryPlanner();
        SubAndCategoryTableRowAdapter adapterRecycle = new SubAndCategoryTableRowAdapter(subcategories);
        recyclerView.setAdapter(adapterRecycle);


        binding.addSubCategory.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ShowOneEventActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_subcategory_on_budget_planner, null);

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

    }

    private ArrayList<SubcategoryPlanner> createSubcategoryPlanner() {
        ArrayList<SubcategoryPlanner> subcategoryPlanners = new ArrayList<>();

        subcategoryPlanners.add(new SubcategoryPlanner("1","Foto i video","Fotografisanje", "5000"));
        subcategoryPlanners.add(new SubcategoryPlanner("2","Foto i video","Fotografije i albumi", "5000"));
        subcategoryPlanners.add(new SubcategoryPlanner("3","Ugostiteljski objekti,\n" +
                "hrana, ketering, torte\n" +
                "i kolači","Iznajmljivanje\n" +
                "ugostiteljskih\n" +
                "objekata za događaje", "400000"));
        subcategoryPlanners.add(new SubcategoryPlanner("4","Dekoracija i rasvjeta","Dekoracija stolova", "0"));

        return  subcategoryPlanners;
    }
}