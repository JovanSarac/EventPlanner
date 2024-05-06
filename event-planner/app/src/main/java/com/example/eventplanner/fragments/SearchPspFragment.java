package com.example.eventplanner.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentSearchPspBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;

import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchPspFragment extends Fragment {

    FragmentSearchPspBinding binding;
    TextInputEditText datetimeRangeEventInput;
    MaterialButton buttonSearch;

    TextInputEditText searchByNameInput;
    TextInputEditText searchByLocationInput;
    AutoCompleteTextView searchByEventType;

    TextInputEditText searchByNamePUP;

    TextInputEditText rangeDate;

    Spinner cat;
    Spinner subcat;

    RangeSlider price;

    RadioButton availab;

    String selectedCategory;
    String selectedSubcategory;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSearchPspBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("EventPlanner", "SearchPspFragment onAttach()");
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


                datetimeRangeEventInput.setText(String.valueOf(day)+ "."+String.valueOf(month + 1)+ "."+String.valueOf(year));

            }
        }, 2024, 03, 6);

        datePickerDialog.show();
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
        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

}