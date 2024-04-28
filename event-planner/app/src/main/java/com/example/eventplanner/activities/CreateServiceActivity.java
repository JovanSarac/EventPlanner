package com.example.eventplanner.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCreateServiceBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateServiceActivity extends AppCompatActivity {

    Button addCategory;
    FloatingActionButton addImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayList<String> events = new ArrayList<>(Arrays.asList(
                "Event 11", "Event 12", "Event 13", "Event 14", "Event 15",
                "Event 21", "Event 22", "Event 23", "Event 24", "Event 25",
                "Event 31", "Event 32", "Event 33", "Event 34", "Event 35",
                "Event 41", "Event 42", "Event 43", "Event 44", "Event 45",
                "Event 51", "Event 52", "Event 53", "Event 54", "Event 55"));
        String[] providers = {"Provider 1", "Provider 2", "Provider 3", "Provider 4", "Provider 5"};

        TextInputLayout textInputLayout = findViewById(R.id.categories);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(CreateServiceActivity.this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(categoryAdapter);

        TextInputLayout subcategoryTextInputLayout = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoCompleteTextView = (AutoCompleteTextView) subcategoryTextInputLayout.getEditText();
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(CreateServiceActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoCompleteTextView.setAdapter(subcategoryAdapter);

        TextInputLayout eventTextInputLayout = findViewById(R.id.events);
        MultiAutoCompleteTextView multiAutoCompleteTextViewEvents = (MultiAutoCompleteTextView) eventTextInputLayout.getEditText();
        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(CreateServiceActivity.this, android.R.layout.simple_dropdown_item_1line, events);
        multiAutoCompleteTextViewEvents.setAdapter(eventAdapter);
        multiAutoCompleteTextViewEvents.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextViewEvents.setInputType(InputType.TYPE_NULL);

        TextInputLayout providerTextInputLayout = findViewById(R.id.providers);
        MultiAutoCompleteTextView multiAutoCompleteTextViewProviders = (MultiAutoCompleteTextView) providerTextInputLayout.getEditText();
        ArrayAdapter<String> providerAdapter = new ArrayAdapter<>(CreateServiceActivity.this, android.R.layout.simple_dropdown_item_1line, providers);
        multiAutoCompleteTextViewProviders.setAdapter(providerAdapter);
        multiAutoCompleteTextViewProviders.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextViewProviders.setInputType(InputType.TYPE_NULL);

        addCategory = findViewById(R.id.add_subcategory);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CreateServiceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.request_new_subcategory, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        addImage = findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.carousel_image_view);
            imageView.setImageURI(selectedImage);
        }
    }
}