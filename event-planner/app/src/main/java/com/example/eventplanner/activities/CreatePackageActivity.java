package com.example.eventplanner.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CategoryListAdapter;
import com.example.eventplanner.adapters.ImageAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.ProductListPupvAdapter;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.adapters.ServiceListPupvAdapter;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.eventplanner.adapters.ProductListAddAdapter;
import com.example.eventplanner.adapters.ServiceListAddAdapter;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreatePackageActivity extends AppCompatActivity {

    Button addProduct, addService, createBtn;
    FloatingActionButton addImage;
    TextInputLayout name, description, price, discount, eventTextInputLayout;
    CheckBox available, visible;
    MultiAutoCompleteTextView eventMultiAutoCompleteTextView;
    RecyclerView recyclerView;

    ArrayList<Event> eventsFromDb;
    ArrayList<Category> categoriesFromDb;
    ArrayList<Subcategory> subcategoriesFromDb;

    ArrayList<Long> eventIds;
    Long categoryId;
    String categoryName;
    Long subcategoryId;
    ArrayList<Uri> images;
    ArrayList<String> imageUrls;
    Boolean pending;

    ArrayList<Product> products;
    ArrayList<Service> services;

    ArrayList<Product> productsFromDb;
    ArrayList<Service> servicesFromDb;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRootReference;

    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        images = new ArrayList<>();
        imageUrls = new ArrayList<>();
        subcategoriesFromDb = new ArrayList<>();
        products = new ArrayList<>();
        services = new ArrayList<>();
        productsFromDb = new ArrayList<>();
        servicesFromDb = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        storageRootReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        getCategories();

        TextInputLayout textInputLayout = findViewById(R.id.categories);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        CategoryListAdapter categoryAdapter = new CategoryListAdapter(CreatePackageActivity.this, android.R.layout.simple_dropdown_item_1line, categoriesFromDb);
        autoCompleteTextView.setAdapter(categoryAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                categoryId = selectedCategory.getId();
                categoryName = selectedCategory.getName();
                subcategoriesFromDb.clear();

                getProducts();
                getServices();
            }
        });

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

        /*LayoutInflater inflater = getLayoutInflater();
        View otherLayout = inflater.inflate(R.layout.add_product, null);

        ListView listView = otherLayout.findViewById(R.id.productsList);
        listView.setAdapter(productListAddAdapter);*/

        ProductListAdapter productListAdapter = new ProductListAdapter(CreatePackageActivity.this, products);
        ListView listview = findViewById(R.id.product_list);
        listview.setAdapter(productListAdapter);

        addProduct = findViewById(R.id.add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CreatePackageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.add_product, null);

                ListView listView = popUpView.findViewById(R.id.productsList);
                ProductListAddAdapter productListAddAdapter = new ProductListAddAdapter(CreatePackageActivity.this, new ProductListAddAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        products.add(item);
                    }
                }, productsFromDb);
                listView.setAdapter(productListAddAdapter);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        /*LayoutInflater inflaterService = getLayoutInflater();
        View otherLayoutService = inflaterService.inflate(R.layout.add_service, null);

        ListView listViewService = otherLayoutService.findViewById(R.id.service_list);
        listViewService.setAdapter(serviceListAddAdapter);*/

        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(CreatePackageActivity.this, services);
        ListView serviceListview = findViewById(R.id.service_list);
        serviceListview.setAdapter(serviceListAdapter);

        addService = findViewById(R.id.add_service);

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) CreatePackageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.add_service, null);

                ListView listView = popUpView.findViewById(R.id.service_list);
                ServiceListAddAdapter serviceListAddAdapter = new ServiceListAddAdapter(CreatePackageActivity.this, new ServiceListAddAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Service item) {
                        services.add(item);
                    }
                }, servicesFromDb);
                listView.setAdapter(serviceListAddAdapter);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void getCategories() {
        categoriesFromDb = new ArrayList<>();

        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Category category = new Category(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("Name"),
                                    doc.getString("Description")
                            );

                            categoriesFromDb.add(category);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePackageActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }

    private void getProducts(){
        productsFromDb = new ArrayList<>();

        db.collection("Products")
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<DocumentSnapshot> productDocs = task.getResult().getDocuments();
                            final int numProducts = productDocs.size();
                            final int[] productsProcessed = {0};

                            for (DocumentSnapshot doc : productDocs) {
                                Product product = new Product(
                                        /*Long.parseLong(doc.getId()),
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
                                                            ProductListPupvAdapter productListAdapter = new ProductListPupvAdapter(CreatePackageActivity.this, products);
                                                            ListView productsListView = findViewById(R.id.product_list);
                                                            productsListView.setAdapter(productListAdapter);
                                                            productsListView.setClickable(true);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CreatePackageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }

                                productsFromDb.add(product);
                            }
                        } else {
                            Toast.makeText(CreatePackageActivity.this, "Failed to fetch products: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePackageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getServices(){
        servicesFromDb = new ArrayList<>();

        db.collection("Services")
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<DocumentSnapshot> serviceDocs = task.getResult().getDocuments();
                            final int numServices = serviceDocs.size();
                            final int[] servicesProccessed = {0};

                            for (DocumentSnapshot doc : serviceDocs) {
                                Service service = new Service(
                                        /*Long.parseLong(doc.getId()),
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
                                                            ServiceListPupvAdapter productListAdapter = new ServiceListPupvAdapter(CreatePackageActivity.this, services);
                                                            ListView serviceListView = findViewById(R.id.service_list);
                                                            serviceListView.setAdapter(productListAdapter);
                                                            serviceListView.setClickable(true);
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CreatePackageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }

                                servicesFromDb.add(service);
                            }
                        } else {
                            Toast.makeText(CreatePackageActivity.this, "Failed to fetch services: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePackageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}