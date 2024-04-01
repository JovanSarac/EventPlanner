package com.example.eventplanner.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentCreateEventBinding;
import com.example.eventplanner.databinding.FragmentProductsServicesPageBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import adapters.SubcategoryListAdapter;
import model.Subcategory;

public class CreateEventFragment extends Fragment {

    private FragmentCreateEventBinding binding;
    TextInputEditText datetimeEventInput;

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] eventTypes = {"Svadbe", "Veridbe", "Rodjendani", "Godiscnjice", "Krstenja" , "Rodjenja",
                "Porodicna okupljanja i proslave", "Mature i proslave diploma", "Bebine zabave i krstenja",
                "Konferencije i seminari", "Godisnje korporativne zabave", "Sajmovi i izlozbe"};


        AutoCompleteTextView autoCompleteTextView = binding.autoCompleteTextView;


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, eventTypes);
        autoCompleteTextView.setAdapter(adapter);

        // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
            String selectedEventType = (String) parent.getItemAtPosition(position);

            System.out.println("Selected event type: " + selectedEventType);
        });

        datetimeEventInput = binding.datetimeEventInput;
        datetimeEventInput.setKeyListener(null);

        datetimeEventInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDatePicker();
            }
        });


        RecyclerView recyclerView = binding.categoryAndSubcategoryList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Subcategory> subcategories = createSubcategory();
        SubcategoryListAdapter adapterRecycle = new SubcategoryListAdapter(subcategories);
        recyclerView.setAdapter(adapterRecycle);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity() , new DatePickerDialog.OnDateSetListener() {
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
                        "profesionalne usluge posluživanja", 0));

        subcategories.add(new Subcategory("Ugostiteljski objekti, hrana, ketering, torte i kolači","Iznajmljivanje ugostiteljskih objekata za događaje",
                "Izbor lokacije sa ugostiteljskim kapacitetima, sala za bankete i restoran za\n" +
                        "privatne događaje, ugostiteljske usluge na otvorenom.", 0));

        subcategories.add(new Subcategory("Foto i video","Videografija",
                "Snimanje događaja u visokoj rezoluciji. Izrada kratkih filmskih priča i\n" +
                        "highlight videa. Livestreaming događaja", 0));

        subcategories.add(new Subcategory("Foto i video","Video materijali",
                "Finalizovani video snimci na USB uređajima ili digitalnim platformama.\n" +
                        "Personalizovane kutije i pakovanja za USB uređaje", 1));

        subcategories.add(new Subcategory("Foto i video","Digitalni proizvod",
                "Online galerije i cloud skladištenje za laku distribuciju i deljenje. Digitalni\n" +
                        "download linkovi za fotografije i video materijale", 1));

        return subcategories;

    }
}