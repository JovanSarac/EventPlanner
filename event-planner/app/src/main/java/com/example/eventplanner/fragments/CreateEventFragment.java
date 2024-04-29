package com.example.eventplanner.fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eventplanner.databinding.FragmentCreateEventBinding;
import com.example.eventplanner.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.eventplanner.adapters.SubcategoryListAdapter;
import com.example.eventplanner.model.Subcategory;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateEventFragment extends Fragment {

    private FragmentCreateEventBinding binding;
    TextInputEditText datetimeEventInput;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AutoCompleteTextView eventType;
    private TextInputEditText nameEvent;
    private TextInputEditText descriptionEvent;

    private  TextInputEditText maxNumberPeople;
    private RadioButton availableEventOpen;
    private RadioButton availableEventClose;

    private TextInputEditText placeEvent;
    private TextInputEditText maxDistance;

    private  TextInputEditText dateEvent;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        eventType = binding.autoCompleteTextView;
        nameEvent = binding.nameEventInuput;
        descriptionEvent = binding.descriptionEventInput;
        maxNumberPeople = binding.maxNumberPeopleInput;
        availableEventOpen = binding.radioButton1;
        availableEventClose = binding.radioButton2;
        placeEvent = binding.placeEventInput;
        maxDistance = binding.placeDistanceEventInput;
        dateEvent = binding.datetimeEventInput;



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

        Button createButton = binding.createButton;

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addNewEvent(new Event(eventType.getText().toString(),
                            nameEvent.getText().toString(),descriptionEvent.getText().toString(),
                            Integer.parseInt(maxNumberPeople.getText().toString()),placeEvent.getText().toString(),
                            Integer.parseInt(maxDistance.getText().toString()), sdf.parse(dateEvent.getText().toString()),
                            true));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return root;
    }

    private void addNewEvent(Event event) {

        Map<String, Object> elememt = new HashMap<>();
        elememt.put("typeEvent", event.getTypeEvent());
        elememt.put("name", event.getName());
        elememt.put("description", event.getDescription());
        elememt.put("maxPeople", event.getMaxPeople());
        elememt.put("locationPlace", event.getLocationPlace());
        elememt.put("maxDistance", event.getMaxDistance());
        elememt.put("available", event.isAvailble());
        elememt.put("dateEvent", event.getDateEvent());

        // Add a new document with a generated ID
        db.collection("Events")
                .add(elememt)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(),"Uspjesno dodat event" ,Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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
