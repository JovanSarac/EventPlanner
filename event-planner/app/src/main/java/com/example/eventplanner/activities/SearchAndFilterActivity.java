package com.example.eventplanner.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCreateEventOdactivityBinding;
import com.example.eventplanner.databinding.ActivitySearchAndFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import adapters.ProductListAdapter;
import model.Product;

public class SearchAndFilterActivity extends AppCompatActivity {

    ActivitySearchAndFilterBinding binding;


    TextInputEditText datetimeRangeEventInput;

    RangeSlider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchAndFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spinner spinner = binding.btnSort;
        // Create an ArrayAdapter using the string array and a default spinner layout
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_array));
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        // Apply the adapter to the spinner


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sort_array)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.white));
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.white));
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchAndFilterActivity.this, R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_search_psp, null );

            AutoCompleteTextView atv = dialogView.findViewById(R.id.autoCompleteInputTextView);
            String[] eventTypes = {"Svadbe", "Veridbe", "Rodjendani", "Godiscnjice", "Krstenja" , "Rodjenja",
                    "Porodicna okupljanja i proslave", "Mature i proslave diploma", "Bebine zabave i krstenja",
                    "Konferencije i seminari", "Godisnje korporativne zabave", "Sajmovi i izlozbe"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventTypes);
            atv.setAdapter(adapter);

            // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
            atv.setOnItemClickListener((parent, view, position, id) -> {
                //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                String selectedEventType = (String) parent.getItemAtPosition(position);

                System.out.println("Selected event type: " + selectedEventType);
            });


            Spinner spinner1 = dialogView.findViewById(R.id.btnSort1);
            Spinner spinner2 = dialogView.findViewById(R.id.btnSort2);

            String[] Subcategories = {"Subcategory", "Hrana za događaje", "Ketering i priprema hrane", "Iznajmljivanje ugostiteljskih objekata za događaje", "Fotografisanje"};
            String[] Categories = {"Category", "Ugostiteljski objekti, hrana, ketering, torte i kolači", "Muzika i zabava", "Smjestaj" , "Logistika i obezbeđenje"};
            ArrayAdapter<String> arrayAdapterForSubcategory = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item, Subcategories) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.purple_light));
                    return view;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.purple_light));
                    return view;
                }
            };
            arrayAdapterForSubcategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<String> arrayAdapterForCattegory = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item, Categories) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.purple_light));
                    return view;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.purple_light));
                    return view;
                }
            };
            arrayAdapterForCattegory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner1.setAdapter(arrayAdapterForCattegory);
            spinner2.setAdapter(arrayAdapterForSubcategory);


            datetimeRangeEventInput = dialogView.findViewById(R.id.datetimeRangeEventInput);
            datetimeRangeEventInput.setKeyListener(null);

            datetimeRangeEventInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePickerdialog();
                }
            });

            slider = dialogView.findViewById(R.id.slider_multiple_thumbs);
            slider.setValues(1.0f,1000.0f);

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

    }

    private void DatePickerdialog() {
        // Creating a MaterialDatePicker builder for selecting a date range
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            Long startDate = selection.first;
            Long endDate = selection.second;

            // Formating the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            // Creating the date range string
            String selectedDateRange = startDateString + " - " + endDateString;

            // Displaying the selected date range in the TextView
            datetimeRangeEventInput.setText(selectedDateRange);
        });

        // Showing the date picker dialog
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
}