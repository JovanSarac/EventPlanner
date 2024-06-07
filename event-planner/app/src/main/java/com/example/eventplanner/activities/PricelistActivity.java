package com.example.eventplanner.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PricelistAdapter;
import com.example.eventplanner.databinding.ActivityPricelistBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.model.UserPUPZ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class PricelistActivity extends AppCompatActivity {

    View view;
    ActivityPricelistBinding binding;
    ArrayList<Product> products;
    ArrayList<Service> services;
    ArrayList<Package> packages;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Object userFromDb;
    String pupvId;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPricelistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        services = new ArrayList<>();
        packages = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getUser();
    }

    private void components(){
        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
    }

    private void createPdf(){
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(42);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        String title = "Pricelist";
        float xTitle = 500;
        float yTitle = 100;
        canvas.drawText(title, xTitle, yTitle, paint);

        paint.setTextSize(36);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        String productsTitle = "Products";
        float xSubTitle = 100;
        float yProductsTitle = 200;
        canvas.drawText(productsTitle, xSubTitle, yProductsTitle, paint);

        String servicesTitle = "Services";
        float yServicesTitle = yProductsTitle + 400;
        canvas.drawText(servicesTitle, xSubTitle, yServicesTitle, paint);

        String packagesTitle = "Packages";
        float yPackagesTitle = yServicesTitle + 400;
        canvas.drawText(packagesTitle, xSubTitle, yPackagesTitle, paint);

        paint.setTextSize(32);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        float yItem = yProductsTitle + 50;
        int i = 0;
        for (Product product : products) {
            Double price = product.getPrice();
            Double discount = product.getDiscount();
            Double priceWithDiscount = price * (1 - discount * 0.01);

            canvas.drawText((++i) + ".", xSubTitle, yItem, paint);
            canvas.drawText(product.getName(), xSubTitle + 10, yItem, paint);
            canvas.drawText(price.toString(), xSubTitle, yItem + 50, paint);
            canvas.drawText("-" + discount.toString() + "%", xSubTitle, yItem + 100, paint);
            canvas.drawText("=" + priceWithDiscount.toString(), xSubTitle, yItem + 150, paint);
            yItem += 50;
        }

        yItem = yServicesTitle + 50;
        i = 0;
        for (Service service : services) {
            Double price = service.getFullPrice();
            Double discount = service.getDiscount();
            Double priceWithDiscount = price * (1 - discount * 0.01);

            canvas.drawText((++i) + ".", xSubTitle, yItem, paint);
            canvas.drawText(service.getName(), xSubTitle + 10, yItem, paint);
            canvas.drawText(price.toString(), xSubTitle, yItem + 50, paint);
            canvas.drawText("-" + discount.toString() + "%", xSubTitle, yItem + 100, paint);
            canvas.drawText("=" + priceWithDiscount.toString(), xSubTitle, yItem + 150, paint);
            yItem += 50;
        }

        yItem = yPackagesTitle + 50;
        i = 0;
        for (Package packageItem : packages) {
            Double price = packageItem.getPrice();
            Double discount = packageItem.getDiscount();
            Double priceWithDiscount = price * (1 - discount * 0.01);

            canvas.drawText((++i) + ".", xSubTitle, yItem, paint);
            canvas.drawText(packageItem.getName(), xSubTitle + 10, yItem, paint);
            canvas.drawText(price.toString(), xSubTitle, yItem + 50, paint);
            canvas.drawText("-" + discount.toString() + "%", xSubTitle, yItem + 100, paint);
            canvas.drawText("=" + priceWithDiscount.toString(), xSubTitle, yItem + 150, paint);
            yItem += 50;
        }

        document.finishPage(page);

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(directoryPath, "pricelist - " + new Date().toString() + ".pdf");

        try {
            document.writeTo(new FileOutputStream(file));
            document.close();
            Toast.makeText(this, "PDF file downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            System.out.println("Error while writing " + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getUser(){
        db.collection("User")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(user.getDisplayName().equals("PUPZ")){
                            userFromDb = new UserPUPZ(
                                    documentSnapshot.getLong("id"),
                                    documentSnapshot.getString("ownerId"),
                                    documentSnapshot.getString("firstName"),
                                    documentSnapshot.getString("lastName"),
                                    documentSnapshot.getString("email"),
                                    documentSnapshot.getString("password"),
                                    documentSnapshot.getString("phone"),
                                    documentSnapshot.getString("address"),
                                    documentSnapshot.getBoolean("valid"),
                                    documentSnapshot.getString("userType"));

                            pupvId = documentSnapshot.getString("ownerId");
                        }
                        else{
                            userFromDb = new UserPUPV(
                                    documentSnapshot.getString("FirstName"),
                                    documentSnapshot.getString("LastName"),
                                    documentSnapshot.getString("Email"),
                                    documentSnapshot.getString("Password"),
                                    documentSnapshot.getString("Phone"),
                                    documentSnapshot.getString("Address"),
                                    documentSnapshot.getBoolean("IsValid"),
                                    documentSnapshot.getString("CompanyName"),
                                    documentSnapshot.getString("CompanyDescription"),
                                    documentSnapshot.getString("CompanyAddress"),
                                    documentSnapshot.getString("CompanyEmail"),
                                    documentSnapshot.getString("CompanyPhone"),
                                    documentSnapshot.getString("WorkTime"));

                                    pupvId = documentSnapshot.getId();
                        }

                        getProducts();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getProducts(){
        db.collection("Products")
                .whereEqualTo("pupvId", pupvId)
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Product product = new Product(
                                    Long.parseLong(doc.getId()),
                                    pupvId,
                                    Long.parseLong(doc.getString("categoryId")),
                                    Long.parseLong(doc.getString("subcategoryId")),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getDouble("price"),
                                    doc.getDouble("discount"),
                                    new ArrayList<>(),
                                    new ArrayList<>(), //convertStringArrayToLong((ArrayList<String>) doc.get("eventTypeIds")),
                                    doc.getBoolean("available"),
                                    doc.getBoolean("visible"),
                                    doc.getBoolean("pending"),
                                    doc.getBoolean("deleted")
                            );

                            products.add(product);
                        }

                        PricelistAdapter<Product> productPricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, products);
                        binding.productList.setAdapter(productPricelistAdapter);

                        getServices();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private ArrayList<Long> convertStringArrayToLong(ArrayList<String> list){
        ArrayList<Long> ids = new ArrayList<>();

        for(String item: list){
            ids.add(Long.parseLong(item));
        }

        return ids;
    }

    private void getServices(){
        db.collection("Services")
                .whereEqualTo("pupvId", pupvId)
                .whereEqualTo("pending", false)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Service service = new Service(
                                        Long.parseLong(doc.getId()),
                                        pupvId,
                                        Long.parseLong(doc.getString("categoryId")),
                                        Long.parseLong(doc.getString("subcategoryId")),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        new ArrayList<>(), //images
                                        doc.getString("specific"),
                                        ((Number) doc.get("pricePerHour")).doubleValue(),
                                        ((Number) doc.get("fullPrice")).doubleValue(),
                                        doc.get("duration") != null ? ((Number) doc.get("duration")).doubleValue() : null,
                                        doc.get("durationMin") != null ? ((Number) doc.get("durationMin")).doubleValue() : null,
                                        doc.get("durationMax") != null ? ((Number) doc.get("durationMax")).doubleValue() : null,
                                        doc.getString("location"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        (ArrayList<String>) doc.get("pupIds"),
                                        convertStringArrayToLong((ArrayList<String>) doc.get("eventTypeIds")),
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        doc.getBoolean("pending"),
                                        doc.getBoolean("deleted"));

                            services.add(service);
                        }

                        PricelistAdapter<Service> servicePricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, services);
                        binding.serviceList.setAdapter(servicePricelistAdapter);

                        getPackages();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getPackages(){
        db.collection("Packages")
                .whereEqualTo("pupvId", pupvId)
                .whereEqualTo("deleted", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Package packagee = new Package(
                                        Long.parseLong(doc.getId()),
                                        pupvId,
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        ((Number) doc.get("discount")).doubleValue(),
                                        doc.getBoolean("available"),
                                        doc.getBoolean("visible"),
                                        Long.parseLong(doc.getString("categoryId")),
                                        convertStringArrayToLong((ArrayList<String>) doc.get("subcategoryIds")),
                                        convertStringArrayToLong((ArrayList<String>)doc.get("productIds")),
                                        convertStringArrayToLong((ArrayList<String>)doc.get("serviceIds")),
                                        convertStringArrayToLong((ArrayList<String>) doc.get("eventTypeIds")),
                                        ((Number) doc.get("price")).doubleValue(),
                                        new ArrayList<>(), //images
                                        doc.getString("reservationDue"),
                                        doc.getString("cancelationDue"),
                                        doc.getBoolean("automaticAffirmation"),
                                        doc.getBoolean("deleted"));

                            packages.add(packagee);

                            components();
                        }

                        PricelistAdapter<Package> packagePricelistAdapter = new PricelistAdapter<>(PricelistActivity.this, R.layout.pricelist_card, packages);
                        binding.packageList.setAdapter(packagePricelistAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PricelistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}