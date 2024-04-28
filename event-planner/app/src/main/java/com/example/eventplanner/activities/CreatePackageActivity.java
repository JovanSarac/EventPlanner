package com.example.eventplanner.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.eventplanner.adapters.ProductListAddAdapter;
import com.example.eventplanner.adapters.ServiceListAddAdapter;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;

public class CreatePackageActivity extends AppCompatActivity {

    Button addProduct;
    Button addService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};

        TextInputLayout textInputLayout = findViewById(R.id.categories);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(CreatePackageActivity.this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(categoryAdapter);

        TextInputLayout priceTextInputLayout = findViewById(R.id.price);
        TextInputEditText editPrice = findViewById(R.id.editPrice);
        editPrice.setFocusable(false);
        editPrice.setClickable(false);

        TextInputLayout reservationDueTextInputLayout = findViewById(R.id.reservation_due);
        TextInputEditText editReservationDue = findViewById(R.id.edit_reservation_due);
        editReservationDue.setFocusable(false);
        editReservationDue.setClickable(false);

        TextInputLayout cancelationDueTextInputLayout = findViewById(R.id.cancelation_due);
        TextInputEditText editCancelationDue = findViewById(R.id.edit_cancelation_due);
        editCancelationDue.setFocusable(false);
        editCancelationDue.setClickable(false);

        ArrayList<Product> products = getProducts();
        ProductListAddAdapter productListAddAdapter = new ProductListAddAdapter(this, products);

        LayoutInflater inflater = getLayoutInflater();
        View otherLayout = inflater.inflate(R.layout.add_product, null);

        ListView listView = otherLayout.findViewById(R.id.productsList);
        listView.setAdapter(productListAddAdapter);

        addProduct = findViewById(R.id.add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CreatePackageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.add_product, null);

                ListView listView = popUpView.findViewById(R.id.productsList);
                listView.setAdapter(productListAddAdapter);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        ArrayList<Service> services = getServices();
        ServiceListAddAdapter serviceListAddAdapter = new ServiceListAddAdapter(this, services);

        LayoutInflater inflaterService = getLayoutInflater();
        View otherLayoutService = inflaterService.inflate(R.layout.add_service, null);

        ListView listViewService = otherLayoutService.findViewById(R.id.service_list);
        listViewService.setAdapter(serviceListAddAdapter);

        addService = findViewById(R.id.add_service);

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CreatePackageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.add_service, null);

                ListView listView = popUpView.findViewById(R.id.service_list);
                listView.setAdapter(serviceListAddAdapter);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }

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
                    names[i], description[i], new ArrayList<>(Arrays.asList(imageIds)), specifics[i], pricesPerHour[i], fullPrices[i],
                    durations[i], locations[i], discounts[i], serviceProviders, serviceEvents, reservationDues[i],
                    cancelationDues[i], automaticAffirmations[i], available[i], visible[i]));
        }

        return services;
    }
}