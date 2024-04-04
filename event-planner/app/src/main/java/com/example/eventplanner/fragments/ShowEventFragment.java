package com.example.eventplanner.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.CreateProductActivity;
import com.example.eventplanner.databinding.FragmentProductsServicesPageBinding;
import com.example.eventplanner.databinding.FragmentShowEventBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import adapters.EventListAdapter;
import adapters.EventRecyclerViewAdapter;
import adapters.SubcategoryListAdapter;
import model.Event;
import model.Subcategory;


public class ShowEventFragment extends Fragment {
    private FragmentShowEventBinding binding;


    public static ShowEventFragment newInstance() {
        return new ShowEventFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.eventsRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Event> events = createEvents();
        EventRecyclerViewAdapter adapterEvents = new EventRecyclerViewAdapter(events);
        recyclerView.setAdapter(adapterEvents);


        return root;
    }

    private ArrayList<Event> createEvents() {
        ArrayList<Event> events = new ArrayList<>();

        events.add((new Event("Vencanje", "Venčanje T i M" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec viverra" +
                " nulla eget ante malesuada gravida. Sed eget blandit sapien. Duis ultricies tellus sed sapien volutpat efficitur. Quisque " +
                "metus lectus, iaculis.", 200, "Novi Sad", 40, new Date(), true)));

        events.add((new Event("Veridba", "Venčanje T i M" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec viverra" +
                " nulla eget ante malesuada gravida. Sed eget blandit sapien. Duis ultricies tellus sed sapien volutpat efficitur. Quisque " +
                "metus lectus, iaculis.", 200, "Beograd", 50, new Date(), true)));

        return events;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}