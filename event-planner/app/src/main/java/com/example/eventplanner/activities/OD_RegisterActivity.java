package com.example.eventplanner.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OD_RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference reference = storage.getReference();

    Button buttonSelectImage;
    ImageView imageViewProfile;
    Uri selectedImage;


    static final int REQUEST_IMAGE_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_od_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setupTimePickComponents();


        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        LinearLayout PUPVLayout=findViewById(R.id.PUPVLayout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ODRadio) {
                    PUPVLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.PUPVRadio) {
                    PUPVLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonSelectImage = findViewById(R.id.addImageButton);
        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        findViewById(R.id.registerUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.PUPVRadio) {
                    Intent intent = new Intent(OD_RegisterActivity.this, PUPV_RegisterCategoryActivity.class);
                    startActivity(intent);
                }else{
                    createUserOd();
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            ImageView imageView=findViewById(R.id.imageViewProfile);
            imageView.setImageURI(selectedImage);

        }
    }
    private String getByteArrayFromImageURL(String url) {

        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }
    private void  createUserOd(){
        Long id= new Random().nextLong();
        TextInputEditText firstNameTextField=findViewById(R.id.firstNameTextbox);
        TextInputEditText lastNameTextField=findViewById(R.id.lastNameTextbox);
        TextInputEditText addressTextField=findViewById(R.id.addressTextbox);
        TextInputEditText emailTextField=findViewById(R.id.emailTextbox);
        TextInputEditText passwordTextField=findViewById(R.id.passwordTextbox);
        TextInputEditText phoneNumberTextField=findViewById(R.id.phoneNumberTextbox);


        Map<String, Object> item = new HashMap<>();
        item.put("FirstName", firstNameTextField.getText().toString());
        item.put("LastName", lastNameTextField.getText().toString());
        item.put("E-mail", emailTextField.getText().toString());
        item.put("Address", addressTextField.getText().toString());
        item.put("Phone", phoneNumberTextField.getText().toString());
        item.put("Password", passwordTextField.getText().toString());
        item.put("IsValid", false);
        if(selectedImage!=null){
            item.put("ProfilePicture",getByteArrayFromImageURL(selectedImage.toString()));
        }

        db.collection("UserOD")
                .document(id.toString())
                .set(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(OD_RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

//    private void setupTimePickComponents(){
//
//    }
//    public void showHourPicker() {
//        final Calendar myCalender = Calendar.getInstance();
//        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
//        int minute = myCalender.get(Calendar.MINUTE);
//        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                if (view.isShown()) {
//                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    myCalender.set(Calendar.MINUTE, minute);
//
//                }
//            }
//        };
//        TimePickerDialog timePickerDialog = new TimePickerDialog(OD_RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
//        timePickerDialog.setTitle("Choose hour:");
//        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        timePickerDialog.show();
//    }
}