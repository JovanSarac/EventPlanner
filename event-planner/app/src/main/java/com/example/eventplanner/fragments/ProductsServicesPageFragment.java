package com.example.eventplanner.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.databinding.FragmentProductsServicesPageBinding;
import com.example.eventplanner.databinding.FragmentSearchPspBinding;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProductsServicesPageFragment extends Fragment {


    private FragmentProductsServicesPageBinding binding;

    private FragmentSearchPspBinding bindingSearchPsp;
    TextInputEditText datetimeRangeEventInput;

    RangeSlider slider;

    String selectedCategory;
    String selectedSubcategory;

    ArrayList<Product> products;

    ArrayList<Service> services;
    FirebaseFirestore db;
    FirebaseStorage storage;

    private List<EventType> itemList=new ArrayList<>();
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

            getEventTypes(dialogView);


            Spinner spinner1 = dialogView.findViewById(R.id.btnSort1);
            getCategory(spinner1);
            Spinner spinner2 = dialogView.findViewById(R.id.btnSort2);
            getSubcategory(spinner2);




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
                    boolean dontavailable = bindingSearchPsp.radioButton2.isChecked();


                    System.out.println(searchByName);
                    System.out.println(category);
                    System.out.println(subcategory);
                    System.out.println(eventType);
                    System.out.println(priceFrom);
                    System.out.println(priceTo);
                    System.out.println(available);
                    System.out.println(dateTimeRange);

                    SearchPsp(searchByName,searchByLocation,eventType,category,subcategory,searchByNamePup,priceFrom,priceTo,available,dontavailable,dateTimeRange,bottomSheetDialog);


                }
            });

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();


        });

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //getProducts();
        //getServices();


        /*ArrayList<Package> packages = getPackages();
        PackageListAdapter packageListAdapter = new PackageListAdapter(requireContext(), packages);
        binding.packageList.setAdapter(packageListAdapter);
        binding.packageList.setClickable(true);*/

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
    private void SearchPsp(String searchByName, String searchByLocation, String eventType, String category, String subcategory, String searchByNamepup,
                           Float priceFrom, Float priceTo, boolean available, boolean dontavilable, String dateTimeRange, BottomSheetDialog btm) {
        ArrayList<Product> searchProducts = new ArrayList<>();
        ArrayList<Service> searchServices = new ArrayList<>();
        if(searchByName.equals("") && searchByLocation.equals("") && category.equals("Category") && subcategory.equals("Subcategory") && eventType.equals("")
                && searchByNamepup.equals("") && priceFrom == 1.0f && priceTo == 1000.0f
           && !available && !dontavilable && dateTimeRange.equals("")){
            getProducts();
            getServices();
            btm.dismiss();
            return;
        }

        if(!searchByName.equals("")){
            for(Product p: products){
                if(p.getName().contains(searchByName)){
                    searchProducts.add(p);
                }
            }
            products = searchProducts;

            for(Service s : services){
                if(s.getName().contains(searchByName)){
                    searchServices.add(s);
                }
            }
            services = searchServices;
        }
        if(priceFrom > 1.0f || priceTo < 1000.0f){
            for(Product p: products){
                if(p.getPrice() >= priceFrom && p.getPrice() <= priceTo){
                    searchProducts.add(p);
                }
            }
            products = searchProducts;

            for(Service s : services){
                if(s.getFullPrice() >= priceFrom && s.getFullPrice() <= priceTo){
                    searchServices.add(s);
                }
            }
            services = searchServices;

        }
        if(available){
            for(Product p: products){
                if(p.getAvailable()){
                    searchProducts.add(p);
                }
            }
            products = searchProducts;

            for(Service s : services){
                if(s.getAvailable()){
                    searchServices.add(s);
                }
            }
            services = searchServices;

        }

        if(dontavilable){
            for(Product p: products){
                if(!p.getAvailable()){
                    searchProducts.add(p);
                }
            }
            products = searchProducts;

            for(Service s : services){
                if(!s.getAvailable()){
                    searchServices.add(s);
                }
            }
            services = searchServices;

        }

        ProductListAdapter productListAdapter = new ProductListAdapter(requireContext(), R.layout.product_card, products);
        binding.productList.setAdapter(productListAdapter);
        binding.productList.setClickable(true);

        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(requireContext(), R.layout.service_card, services);
        binding.serviceList.setAdapter(serviceListAdapter);
        binding.serviceList.setClickable(true);

        btm.dismiss();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void DatePickerdialog() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {

            Long startDate = selection.first;
            Long endDate = selection.second;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            String selectedDateRange = startDateString + " - " + endDateString;

            datetimeRangeEventInput.setText(selectedDateRange);
        });

        datePicker.show(getActivity().getSupportFragmentManager() , "DATE_PICKER");
    }

    private void getProducts() {
        products = new ArrayList<>();

        db.collection("Products")
                .whereEqualTo("pending", false)
                .whereEqualTo("visible", true)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<DocumentSnapshot> productDocs = task.getResult().getDocuments();
                            final int numProducts = productDocs.size();
                            final int[] productsProcessed = {0};

                            for (DocumentSnapshot doc : productDocs) {
                                Product product = new Product(/*
                                        Long.parseLong(doc.getId()),
                                        doc.getLong("categoryId"),
                                        doc.getLong("subcategoryId"),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        ((Number) doc.get("price")).doubleValue(),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        new ArrayList<>(), //images
                                        (ArrayList<Long>) doc.get("eventIds"),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getBoolean("pending"),
                                        doc.getBoolean("deleted")*/);

                                ArrayList<String> imageUrls = (ArrayList<String>) doc.get("imageUrls");
                                final int numImages = imageUrls.size();

                                for (String imageUrl : imageUrls) {
                                    StorageReference imageRef = storage.getReference().child(imageUrl);
                                    imageRef.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    product.getImages().add(uri);

                                                    if (product.getImages().size() == numImages) {
                                                        productsProcessed[0]++;

                                                        if (productsProcessed[0] == numProducts) {
                                                            ProductListAdapter productListAdapter = new ProductListAdapter(requireContext(), R.layout.product_card, products);
                                                            binding.productList.setAdapter(productListAdapter);
                                                            binding.productList.setClickable(true);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }

                                products.add(product);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch products: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    @NonNull
    private void getServices() {
        services = new ArrayList<>();
        db.collection("Services")
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .whereEqualTo("visible", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<DocumentSnapshot> serviceDocs = task.getResult().getDocuments();
                            final int numServices = serviceDocs.size();
                            final int[] servicesProccessed = {0};

                            for (DocumentSnapshot doc : serviceDocs) {
                                Service service = new Service(/*
                                        Long.parseLong(doc.getId()),
                                        doc.getLong("categoryId"),
                                        doc.getLong("subcategoryId"),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        new ArrayList<>(), //images
                                        doc.getString("specific"),
                                        ((Number) doc.get("pricePerHour")).doubleValue(),
                                        ((Number) doc.get("fullPrice")).doubleValue(),
                                        ((Number) doc.get("duration")).doubleValue(),
                                        ((Number) doc.get("durationMin")).doubleValue(),
                                        ((Number) doc.get("durationMax")).doubleValue(),
                                        doc.getString("location"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        (ArrayList<String>) doc.get("providers"),
                                        (ArrayList<Long>) doc.get("eventIds"),
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getBoolean("pending"),
                                        doc.getBoolean("deleted")*/);

                                ArrayList<String> imageUrls = (ArrayList<String>) doc.get("imageUrls");
                                final int numImages = imageUrls.size();

                                for (String imageUrl : imageUrls) {
                                    StorageReference imageRef = storage.getReference().child(imageUrl);
                                    imageRef.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    service.getImages().add(uri);

                                                    if (service.getImages().size() == numImages) {
                                                        servicesProccessed[0]++;

                                                        if (servicesProccessed[0] == numServices) {
                                                            ServiceListAdapter productListAdapter = new ServiceListAdapter(requireContext(), R.layout.service_card, services);
                                                            binding.serviceList.setAdapter(productListAdapter);
                                                            binding.serviceList.setClickable(true);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }

                                services.add(service);
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch services: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /*@NonNull
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
    }*/

    private void getEventTypes(View dialogView) {
        itemList=new ArrayList<>();
        db.collection("EventTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> taskEvent) {
                        if (taskEvent.isSuccessful()) {
                            // Process eventType documents

                            // Perform subcategories query
                            db.collection("Subcategories")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskSubcategories) {
                                            if (taskSubcategories.isSuccessful()) {
                                                for(DocumentSnapshot docEvent: taskEvent.getResult()){
                                                    List<String> subcategoryIds=(List<String>)docEvent.get("Subcategories");

                                                    List<Subcategory> subcategories = new ArrayList<>();
                                                    for(DocumentSnapshot doc: taskSubcategories.getResult()){
                                                        Long num=Long.parseLong(doc.getId());
                                                        if(subcategoryIds.contains(num.toString())){
                                                            Subcategory subcategory = new Subcategory(
                                                                    Long.parseLong(doc.getId()),
                                                                    doc.getString("CategoryName"),
                                                                    doc.getString("Name"),
                                                                    doc.getString("Description"),
                                                                    doc.getLong("Type").intValue()
                                                            );
                                                            subcategories.add(subcategory);
                                                        }

                                                    }
                                                    EventType type = new EventType(
                                                            Long.parseLong(docEvent.getId()),
                                                            docEvent.getBoolean("InUse"),
                                                            docEvent.getString("Name"),
                                                            docEvent.getString("Description"),
                                                            subcategories
                                                    );
                                                    itemList.add(type);

                                                }

                                                ArrayList<String> eventTypesList = new ArrayList<>();
                                                for(EventType et : itemList){
                                                    eventTypesList.add(et.getTypeName());
                                                }

                                                AutoCompleteTextView atv = dialogView.findViewById(R.id.inputEventType);

                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, eventTypesList);
                                                atv.setAdapter(adapter);

                                                // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
                                                atv.setOnItemClickListener((parent, view, position, id) -> {
                                                    //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                                                    String selectedEventType = (String) parent.getItemAtPosition(position);

                                                    System.out.println("Selected event type: " + selectedEventType);
                                                });


                                            }
                                        }

                                    });
                        }
                    }
                });
    }

    private void getCategory(Spinner spinner1) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Category");
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            categories.add(doc.getString("Name"));
                        }
                        ArrayAdapter<String> arrayAdapterForCattegory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories) {
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getSubcategory(Spinner spinner2) {
        ArrayList<String> subcategories = new ArrayList<>();
        subcategories.add("Subcategory");
        db.collection("Subcategories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            subcategories.add(doc.getString("Name"));
                        }
                        ArrayAdapter<String> arrayAdapterForSubcategory = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subcategories) {
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

                        spinner2.setAdapter(arrayAdapterForSubcategory);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }




}