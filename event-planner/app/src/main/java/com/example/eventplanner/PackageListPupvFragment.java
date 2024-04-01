package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.activities.CreatePackageActivity;

import java.util.ArrayList;
import java.util.Arrays;

import adapters.PackageListPupvAdapter;
import model.Package;
import model.Product;
import model.Service;

public class PackageListPupvFragment extends Fragment {

    View view;
    FragmentPackageListPupvBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackageListPupvBinding.inflate(getLayoutInflater());

        ArrayList<Package> packages = getPackages();

        PackageListPupvAdapter packageListAdapter = new PackageListPupvAdapter(requireContext(), packages);

        binding.packageListPupv.setAdapter(packageListAdapter);
        binding.packageListPupv.setClickable(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CreatePackageActivity.class);
                startActivity(intent);
            }
        });

    }

    @NonNull
    private static ArrayList<Package> getPackages() {
        ArrayList<Product> products = getProducts();
        ArrayList<Service> services = getServices();
        ArrayList<Package> packages = new ArrayList<>();

        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] names = {"Service 1", "Service 2", "Service 3", "Service 4", "Service 5"};
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

    @NonNull
    private static ArrayList<Service> getServices() {
        Long[] ids = {1l, 2l, 3l, 4l, 5l};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        String[] names = {"Service 1", "Service 2", "Service 3", "Service 4", "Service 5"};
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
                    names[i], description[i], imageIds[i], specifics[i], pricesPerHour[i], fullPrices[i],
                    durations[i], locations[i], discounts[i], serviceProviders, serviceEvents, reservationDues[i],
                    cancelationDues[i], automaticAffirmations[i], available[i], visible[i]));
        }

        return services;
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
                    names[i], description[i], prices[i], discounts[i], imageIds[i], productEvents, available[i], visible[i]));
        }
        return products;
    }
}