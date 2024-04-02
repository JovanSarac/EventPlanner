package com.example.eventplanner.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditProductBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import adapters.EventListAdapter;
import adapters.ImageAdapter;

public class EditProductActivity extends AppCompatActivity {

    ActivityEditProductBinding binding;
    FloatingActionButton addImage;
    ArrayList<Integer> productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Long productId = getIntent().getLongExtra("productId", -1);
        String productCategory = getIntent().getStringExtra("productCategory");
        String productSubcategory = getIntent().getStringExtra("productSubcategory");
        String productName = getIntent().getStringExtra("productName");
        String productDescription = getIntent().getStringExtra("productDescription");
        Double productPrice = getIntent().getDoubleExtra("productPrice", 0);
        Double productDiscount = getIntent().getDoubleExtra("productDiscount", 0);
        productImage = getIntent().getIntegerArrayListExtra("productImage");
        ArrayList<String> productEvents = getIntent().getStringArrayListExtra("productEvents");
        Boolean productAvailability = getIntent().getBooleanExtra("productAvailability", true);
        Boolean productVisibility = getIntent().getBooleanExtra("productVisibility", true);

        addImage = findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });


        TextInputLayout category = findViewById(R.id.category);
        TextInputEditText categoryAutoComplete = (TextInputEditText) category.getEditText();
        categoryAutoComplete.setText(productCategory);

        TextInputLayout subcategory = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoComplete = (AutoCompleteTextView) subcategory.getEditText();
        String[] subcategories = {"Subcategory 1", "Subcategory 2", "Subcategory 3", "Subcategory 4", "Subcategory 5"};
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_dropdown_item_1line, subcategories);
        subcategoryAutoComplete.setAdapter(subcategoryAdapter);
        subcategoryAutoComplete.setText(productSubcategory, false);
        subcategoryAutoComplete.setSelection(subcategoryAutoComplete.getText().length());

        TextInputLayout name = findViewById(R.id.name);
        TextInputEditText nameAutoComplete = (TextInputEditText) name.getEditText();
        nameAutoComplete.setText(productName);

        TextInputLayout description = findViewById(R.id.description);
        TextInputEditText descriptionAutoComplete = (TextInputEditText) description.getEditText();
        descriptionAutoComplete.setText(productDescription);

        TextInputLayout price = findViewById(R.id.price);
        TextInputEditText priceAutoComplete = (TextInputEditText) price.getEditText();
        priceAutoComplete.setText(productPrice.toString());

        TextInputLayout discount = findViewById(R.id.discount);
        TextInputEditText discountAutoComplete = (TextInputEditText) discount.getEditText();
        discountAutoComplete.setText(productDiscount.toString());

        RecyclerView recyclerView = findViewById(R.id.recycler);
        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < productImage.size(); i++){
            arrayList.add(productImage.get(i));
        }

        ImageAdapter adapter = new ImageAdapter(EditProductActivity.this, arrayList);
        recyclerView.setAdapter(adapter);

        EventListAdapter eventListAdapter = new EventListAdapter(this, productEvents);
        binding.events.setAdapter(eventListAdapter);

        CheckBox available = findViewById(R.id.availability);
        available.setChecked(productAvailability);

        CheckBox visibility = findViewById(R.id.visibility);
        visibility.setChecked(productVisibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            /*ImageView imageView = findViewById(R.id.carousel_image_view);
            imageView.setImageURI(selectedImage);*/
        }
    }
}