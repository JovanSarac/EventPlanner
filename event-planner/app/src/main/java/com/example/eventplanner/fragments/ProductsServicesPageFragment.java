package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentProductsServicesPageBinding;
import com.example.eventplanner.databinding.FragmentSearchPspBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.eventplanner.adapters.PackageListAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;


public class ProductsServicesPageFragment extends Fragment {


    private FragmentProductsServicesPageBinding binding;

    private FragmentSearchPspBinding bindingSearchPsp;
    TextInputEditText datetimeRangeEventInput;

    RangeSlider slider;

    String selectedCategory;
    String selectedSubcategory;

    public static ProductsServicesPageFragment newInstance() {
        return new ProductsServicesPageFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductsServicesPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner spinner = binding.btnSort;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sort_array)) {
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
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_search_psp, null);

            bindingSearchPsp = FragmentSearchPspBinding.bind(dialogView);

            AutoCompleteTextView atv = dialogView.findViewById(R.id.inputEventType);
            String[] eventTypes = {"Svadbe", "Veridbe", "Rodjendani", "Godiscnjice", "Krstenja", "Rodjenja",
                    "Porodicna okupljanja i proslave", "Mature i proslave diploma", "Bebine zabave i krstenja",
                    "Konferencije i seminari", "Godisnje korporativne zabave", "Sajmovi i izlozbe"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, eventTypes);
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
            String[] Categories = {"Category", "Ugostiteljski objekti, hrana, ketering, torte i kolači", "Muzika i zabava", "Smjestaj", "Logistika i obezbeđenje"};
            ArrayAdapter<String> arrayAdapterForSubcategory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Subcategories) {
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
            ArrayAdapter<String> arrayAdapterForCattegory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Categories) {
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
            slider.setValues(1.0f, 1000.0f);


            bindingSearchPsp.btnSort1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedCategory = parentView.getItemAtPosition(position).toString();
                    // Ovde možete koristiti selectedItem prema potrebi
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Implementacija ako nije izabrana ni jedna stavka
                }
            });

            bindingSearchPsp.btnSort2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedSubcategory = parentView.getItemAtPosition(position).toString();
                    // Ovde možete koristiti selectedItem prema potrebi
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Implementacija ako nije izabrana ni jedna stavka
                }
            });
            dialogView.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Kod za prikupljanje podataka o pretrazi

                    String searchByName = bindingSearchPsp.searchByNameInput.getText().toString();
                    String searchByLocation = bindingSearchPsp.searchByLocationInput.getText().toString();
                    String eventType = bindingSearchPsp.inputEventType.getText().toString();
                    String category = selectedCategory;
                    String subcategory = selectedSubcategory;
                    String searchByNamePup = bindingSearchPsp.searchByNamePUPInput.getText().toString();
                    String dateTimeRange = bindingSearchPsp.datetimeRangeEventInput.getText().toString();
                    List<Float> range = bindingSearchPsp.sliderMultipleThumbs.getValues();
                    Float priceFrom = range.get(0);
                    Float priceTo = range.get(1);
                    boolean available = bindingSearchPsp.radioButton1.isChecked();


                    System.out.println(searchByName);
                    System.out.println(category);
                    System.out.println(subcategory);
                    System.out.println(eventType);
                    System.out.println(priceFrom);
                    System.out.println(priceTo);
                    System.out.println(available);
                    System.out.println(dateTimeRange);






                    // Zatvaranje BottomSheetDialog-a
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();


        });


        ArrayList<Product> products = getProducts();
        ProductListAdapter productListAdapter = new ProductListAdapter(requireContext(), products);
        binding.productList.setAdapter(productListAdapter);
        binding.productList.setClickable(true);


        ArrayList<Service> services = getServices();
        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(requireContext(), services);
        binding.serviceList.setAdapter(serviceListAdapter);
        binding.serviceList.setClickable(true);


        ArrayList<Package> packages = getPackages();
        PackageListAdapter packageListAdapter = new PackageListAdapter(requireContext(), packages);
        binding.packageList.setAdapter(packageListAdapter);
        binding.packageList.setClickable(true);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Prikazati listu proizvoda, sakriti ostale
                        binding.productList.setVisibility(View.VISIBLE);
                        binding.serviceList.setVisibility(View.GONE);
                        binding.packageList.setVisibility(View.GONE);
                        break;
                    case 1:
                        // Prikazati listu usluga, sakriti ostale
                        binding.productList.setVisibility(View.GONE);
                        binding.serviceList.setVisibility(View.VISIBLE);
                        binding.packageList.setVisibility(View.GONE);
                        break;
                    case 2:
                        // Prikazati listu paketa, sakriti ostale
                        binding.productList.setVisibility(View.GONE);
                        binding.serviceList.setVisibility(View.GONE);
                        binding.packageList.setVisibility(View.VISIBLE);
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        datePicker.show(getActivity().getSupportFragmentManager() , "DATE_PICKER");
    }

    @NonNull
    private static ArrayList<Product> getProducts() {
        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        String[] names = {"Product 1", "Product 2", "Product 3", "Product 4", "Product 5"};
        String[] description = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5"};
        Double[] prices = {10.0, 20.0, 30.0, 40.0, 50.0};
        Double[] discounts = {1.0, 2.0, 3.0, 4.0, 5.0};
        Integer[] imageIds = {R.drawable.product_1, R.drawable.product_2, R.drawable.product_3,
                R.drawable.product_4, R.drawable.product_5};
        ArrayList<String> events = new ArrayList<>(Arrays.asList(
                "Event 11", "Event 12", "Event 13", "Event 14", "Event 15",
                "Event 21", "Event 22", "Event 23", "Event 24", "Event 25",
                "Event 31", "Event 32", "Event 33", "Event 34", "Event 35",
                "Event 41", "Event 42", "Event 43", "Event 44", "Event 45",
                "Event 51", "Event 52", "Event 53", "Event 54", "Event 55"));

        Boolean[] available = {true, true, true, true, true};
        Boolean[] visible = {true, true, true, true, true};

        ArrayList<Product> products = new ArrayList<>();

        for(int i = 0; i < ids.length; i++){
            ArrayList<String> productEvents = new ArrayList<>();
            for(int j = 5 * i; j < 5 * i + 5; j++){
                productEvents.add(events.get(j));
            }

            products.add(new Product(ids[i], categories[i], subcategories[i],
                    names[i], description[i], prices[i], discounts[i], new ArrayList<>(Arrays.asList(imageIds)), productEvents, available[i], visible[i]));
        }
        return products;
    }

    @NonNull
    private static ArrayList<Service> getServices() {
        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        String[] names = {"Product 1", "Product 2", "Product 3", "Product 4", "Product 5"};
        String[] description = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5"};
        Integer[] imageIds = {R.drawable.product_1, R.drawable.product_2, R.drawable.product_3,
                R.drawable.product_4, R.drawable.product_5};
        String[] specifics = {"Specific 1", "Specific 2", "Specific 3", "Specific 4", "Specific 5"};
        Double [] pricesPerHour = {1.0, 2.0, 3.0 , 4.0, 5.0};
        Double[] fullPrices = {10.0, 20.0, 30.0, 40.0, 50.0};
        Double[] durations = {1.0, 2.0, 3.0, 4.0, 5.0};
        String[] locations = {"Location 1", "Location 2", "Location 3", "Location 4", "Location 5"};
        Double[] discounts = {1.0, 2.0, 3.0, 4.0, 5.0};
        ArrayList<String> providers = new ArrayList<>(Arrays.asList(
                "Provider 11", "Provider 12", "Provider 13", "Provider 14", "Provider 15",
                "Provider 21", "Provider 22", "Provider 23", "Provider 24", "Provider 25",
                "Provider 31", "Provider 32", "Provider 33", "Provider 34", "Provider 35",
                "Provider 41", "Provider 42", "Provider 43", "Provider 44", "Provider 45",
                "Provider 51", "Provider 52", "Provider 53", "Provider 54", "Provider 55"));
        ArrayList<String> events = new ArrayList<>(Arrays.asList(
                "Event 11", "Event 12", "Event 13", "Event 14", "Event 15",
                "Event 21", "Event 22", "Event 23", "Event 24", "Event 25",
                "Event 31", "Event 32", "Event 33", "Event 34", "Event 35",
                "Event 41", "Event 42", "Event 43", "Event 44", "Event 45",
                "Event 51", "Event 52", "Event 53", "Event 54", "Event 55"));
        String[] reservationDues = {"1 day", "2 days", "3 days", "4 days", "5 days"};
        String[] cancelationDues = {"1 day", "2 days", "3 days", "4 days", "5 days"};
        Boolean [] automaticAffirmations = {true, true, false, false, false};
        Boolean[] available = {true, true, true, true, true};
        Boolean[] visible = {true, true, true, true, true};

        ArrayList<Service> services = new ArrayList<>();

        for(int i = 0; i < ids.length; i++){
            ArrayList<String> serviceEvents = new ArrayList<>();
            ArrayList<String> serviceProviders = new ArrayList<>();
            for(int j = i; j < i + 5; j++){
                serviceEvents.add(events.get(j));
                serviceProviders.add(providers.get(j));
            }

            services.add(new Service(ids[i], categories[i], subcategories[i],
                    names[i], description[i], new ArrayList<>(Arrays.asList(imageIds)), specifics[i], pricesPerHour[i], fullPrices[i],
                    durations[i], locations[i], discounts[i], serviceProviders, serviceEvents, reservationDues[i],
                    cancelationDues[i], automaticAffirmations[i], available[i], visible[i]));
        }

        return services;
    }

    @NonNull
    private static ArrayList<Package> getPackages() {
        ArrayList<Product> products = getProducts();
        ArrayList<Service> services = getServices();
        ArrayList<Package> packages = new ArrayList<>();

        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] names = {"Package 1", "Package 2", "Package 3", "Package 4", "Package 5"};
        String[] description = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5"};
        Double[] discounts = {1.0, 2.0, 3.0, 4.0, 5.0};
        Boolean[] available = {true, true, true, true, true};
        Boolean[] visible = {true, true, true, true, true};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayList<String> events = new ArrayList<>(Arrays.asList(
                "Event 11", "Event 12", "Event 13", "Event 14", "Event 15",
                "Event 21", "Event 22", "Event 23", "Event 24", "Event 25",
                "Event 31", "Event 32", "Event 33", "Event 34", "Event 35",
                "Event 41", "Event 42", "Event 43", "Event 44", "Event 45",
                "Event 51", "Event 52", "Event 53", "Event 54", "Event 55"));
        Double[] prices = {10.0, 20.0, 30.0, 40.0, 50.0};
        ArrayList<Integer> imageIds = new ArrayList<>(Arrays.asList(R.drawable.product_1, R.drawable.product_2, R.drawable.product_3,
                R.drawable.product_4, R.drawable.product_5));
        String[] reservationDues = {"1 day", "2 days", "3 days", "4 days", "5 days"};
        String[] cancelationDues = {"1 day", "2 days", "3 days", "4 days", "5 days"};
        Boolean [] automaticAffirmations = {true, true, false, false, false};

        for(int i = 0; i < ids.length; i++){
            ArrayList<String> packageEvents = new ArrayList<>();
            for(int j = i; j < i + 5; j++){
                packageEvents.add(events.get(j));
            }

            packages.add(new Package(ids[i], names[i], description[i], discounts[i], available[i], visible[i],
                    categories[i], subcategories[i], products, services, packageEvents, prices[i], imageIds, reservationDues[i], cancelationDues[i], automaticAffirmations[i]));
        }

        return  packages;
    }



}