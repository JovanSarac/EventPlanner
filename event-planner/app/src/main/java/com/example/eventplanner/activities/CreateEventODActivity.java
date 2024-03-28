package com.example.eventplanner.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCreateEventOdactivityBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import adapters.SubcategoryListAdapter;
import model.Subcategory;

public class CreateEventODActivity extends AppCompatActivity {

    ActivityCreateEventOdactivityBinding binding;

    TextInputEditText datetimeEventInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateEventOdactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String[] eventTypes = {"Svadbe", "Veridbe", "Rodjendani", "Godiscnjice", "Krstenja" , "Rodjenja",
        "Porodicna okupljanja i proslave", "Mature i proslave diploma", "Bebine zabave i krstenja",
                "Konferencije i seminari", "Godisnje korporativne zabave", "Sajmovi i izlozbe"};


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventTypes);
        autoCompleteTextView.setAdapter(adapter);

        // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
             //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
            String selectedEventType = (String) parent.getItemAtPosition(position);

            System.out.println("Selected event type: " + selectedEventType);
        });



        datetimeEventInput = findViewById(R.id.datetimeEventInput);
        datetimeEventInput.setKeyListener(null);

        datetimeEventInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDatePicker();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.categoryAndSubcategoryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Subcategory> subcategories = createSubcategory();
        SubcategoryListAdapter adapterRecycle = new SubcategoryListAdapter(subcategories);
        recyclerView.setAdapter(adapterRecycle);




    }


    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                datetimeEventInput.setText(String.valueOf(day)+ "."+String.valueOf(month + 1)+ "."+String.valueOf(year));

            }
        }, 2024, 03, 6);

        datePickerDialog.show();
    }

    private ArrayList<Subcategory> createSubcategory(){
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(new Subcategory("Ugostiteljski objekti, hrana, ketering, torte i kolači","Ketering i priprema hrane",
                "Kompletna ketering usluga (hrana i piće), specijalizovani ketering\n" +
                        "(veganski, bez glutena), mobilni barovi i koktel usluge (gin-tonik bar, whiski bar),\n" +
                        "profesionalne usluge posluživanja", Subcategory.Type.SERVICE));

        subcategories.add(new Subcategory("Ugostiteljski objekti, hrana, ketering, torte i kolači","Iznajmljivanje ugostiteljskih objekata za događaje",
                "Izbor lokacije sa ugostiteljskim kapacitetima, sala za bankete i restoran za\n" +
                        "privatne događaje, ugostiteljske usluge na otvorenom.", Subcategory.Type.SERVICE));

        subcategories.add(new Subcategory("Foto i video","Videografija",
                "Snimanje događaja u visokoj rezoluciji. Izrada kratkih filmskih priča i\n" +
                        "highlight videa. Livestreaming događaja", Subcategory.Type.SERVICE));

        subcategories.add(new Subcategory("Foto i video","Video materijali",
                "Finalizovani video snimci na USB uređajima ili digitalnim platformama.\n" +
                        "Personalizovane kutije i pakovanja za USB uređaje", Subcategory.Type.PRODUCT));

        subcategories.add(new Subcategory("Foto i video","Digitalni proizvod",
                "Online galerije i cloud skladištenje za laku distribuciju i deljenje. Digitalni\n" +
                        "download linkovi za fotografije i video materijale", Subcategory.Type.PRODUCT));

        return subcategories;

    }
}