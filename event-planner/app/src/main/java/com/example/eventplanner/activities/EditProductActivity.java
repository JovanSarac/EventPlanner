package com.example.eventplanner.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.SubcategoryAdapter;
import com.example.eventplanner.databinding.ActivityEditProductBinding;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import com.example.eventplanner.adapters.EventListAdapter;
import com.example.eventplanner.adapters.ImageAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProductActivity extends AppCompatActivity {

    ActivityEditProductBinding binding;
    FloatingActionButton addImage;

    ArrayList<Subcategory> subcategoriesFromDb;
    ArrayList<Uri> productImage;

    Long subcategoryId;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();


        Long productId = getIntent().getLongExtra("productId", 0);
        Long productCategory = getIntent().getLongExtra("productCategory", 0);
        Long productSubcategory = getIntent().getLongExtra("productSubcategory", 0);
        String productName = getIntent().getStringExtra("productName");
        String productDescription = getIntent().getStringExtra("productDescription");
        Double productPrice = getIntent().getDoubleExtra("productPrice", 0);
        Double productDiscount = getIntent().getDoubleExtra("productDiscount", 0);
        ArrayList<String> imageStrings = getIntent().getStringArrayListExtra("productImage");
        if (imageStrings != null) {
            for (String imageString : imageStrings) {
                //productImage.add(Uri.parse(imageString));
            }
        }
        ArrayList<Long> productEvents = (ArrayList<Long>)getIntent().getSerializableExtra("productEvents");
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

        db.collection("Categories")
                .document(productCategory.toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("Name");
                            TextInputLayout category = findViewById(R.id.category);
                            TextInputEditText categoryAutoComplete = (TextInputEditText) category.getEditText();
                            categoryAutoComplete.setText(name);

                            //getSubcategories(name);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        /*TextInputLayout subcategoryTextInputLayout = findViewById(R.id.subcategories);
        AutoCompleteTextView subcategoryAutoCompleteTextView = (AutoCompleteTextView) subcategoryTextInputLayout.getEditText();
        SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(EditProductActivity.this, android.R.layout.simple_dropdown_item_1line, subcategoriesFromDb);
        subcategoryAutoCompleteTextView.setAdapter(subcategoryAdapter);

        subcategoryAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subcategory selectedSubcategory = (Subcategory) parent.getItemAtPosition(position);
                subcategoryId = selectedSubcategory.getId();
            }
        });*/

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

        /*RecyclerView recyclerView = findViewById(R.id.recycler);
        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < productImage.size(); i++){
            arrayList.add(productImage.get(i));
        }

        ImageAdapter adapter = new ImageAdapter(EditProductActivity.this, arrayList);
        recyclerView.setAdapter(adapter);*/

        /*EventListAdapter eventListAdapter = new EventListAdapter(this, productEvents);
        binding.events.setAdapter(eventListAdapter);*/

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

    private void getSubcategories(String catName){
        db.collection("Subcategories")
                .whereEqualTo("CategoryName", catName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Subcategory subcategory = new Subcategory(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("CategoryName"),
                                    doc.getString("Name"),
                                    doc.getString("Description"),
                                    (int) (long)doc.getLong("Type"));

                            subcategoriesFromDb.add(subcategory);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }
}