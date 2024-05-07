package com.example.eventplanner.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.model.UserPUPV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OD_RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                    Map<String, Object> item=createUserPUPV();
                    Intent intent = new Intent(OD_RegisterActivity.this, PUPV_RegisterCategoryActivity.class);
                    intent.putExtra("object", (Serializable) item);
                    if(selectedImage!=null)intent.putExtra("pathImage",selectedImage.toString());
                    else intent.putExtra("pathImage","");
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
    private void uploadImage(String userId){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        Uri fileUri = selectedImage;
        StorageReference imageRef = storageRef.child("images/" + userId);

        UploadTask uploadTask = imageRef.putFile(fileUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image uploaded successfully
                Log.d("TAG", "Image uploaded successfully");

                // Get the download URL for the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Handle the download URL (e.g., save it to a database)
                        String imageUrl = downloadUri.toString();
                        Log.d("TAG", "Download URL: " + imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle unsuccessful uploads
                Log.e("TAG", "Error uploading image", e);
            }
        });
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
        item.put("UserType", "OD");
        if(selectedImage!=null){
            uploadImage(id.toString());
        }

        db.collection("User")
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
    private Map<String, Object>  createUserPUPV() {

        TextInputEditText firstNameTextField=findViewById(R.id.firstNameTextbox);
        TextInputEditText lastNameTextField=findViewById(R.id.lastNameTextbox);
        TextInputEditText addressTextField=findViewById(R.id.addressTextbox);
        TextInputEditText emailTextField=findViewById(R.id.emailTextbox);
        TextInputEditText passwordTextField=findViewById(R.id.passwordTextbox);
        TextInputEditText phoneNumberTextField=findViewById(R.id.phoneNumberTextbox);

        TextInputEditText companyNameTextField=findViewById(R.id.companyNameTextBox);
        TextInputEditText companyAddressTextField=findViewById(R.id.companyAddressTextBox);
        TextInputEditText companyEmailTextField=findViewById(R.id.companyEmailTextBox);
        TextInputEditText companyDescriptionTextField=findViewById(R.id.companyDescriptionTextBox);
        TextInputEditText companyPhoneNumberTextField=findViewById(R.id.companyPhoneTextBox);

        TextInputEditText startMon=findViewById(R.id.startMonday);
        TextInputEditText endMon=findViewById(R.id.endMonday);
        TextInputEditText startTue=findViewById(R.id.startTuesday);
        TextInputEditText endTue=findViewById(R.id.endTuesday);
        TextInputEditText startWed=findViewById(R.id.startWednesday);
        TextInputEditText endWed=findViewById(R.id.endWednesday);
        TextInputEditText startThu=findViewById(R.id.startThursday);
        TextInputEditText endThu=findViewById(R.id.endThursday);
        TextInputEditText startFri=findViewById(R.id.startFriday);
        TextInputEditText endFri=findViewById(R.id.endFriday);
        TextInputEditText startSat=findViewById(R.id.startSaturday);
        TextInputEditText endSat=findViewById(R.id.endSaturday);
        TextInputEditText startSun=findViewById(R.id.startSunday);
        TextInputEditText endSun=findViewById(R.id.endSunday);

        String workTime=startMon.getText().toString()+"-"+endMon.getText().toString();
        workTime+="?"+startTue.getText().toString()+"-"+endTue.getText().toString();
        workTime+="?"+startWed.getText().toString()+"-"+endWed.getText().toString();
        workTime+="?"+startThu.getText().toString()+"-"+endThu.getText().toString();
        workTime+="?"+startFri.getText().toString()+"-"+endFri.getText().toString();
        workTime+="?"+startSat.getText().toString()+"-"+endSat.getText().toString();
        workTime+="?"+startSun.getText().toString()+"-"+endSun.getText().toString();

        Map<String, Object> item = new HashMap<>();
        item.put("FirstName", firstNameTextField.getText().toString());
        item.put("LastName", lastNameTextField.getText().toString());
        item.put("E-mail", emailTextField.getText().toString());
        item.put("Address", addressTextField.getText().toString());
        item.put("Phone", phoneNumberTextField.getText().toString());
        item.put("Password", passwordTextField.getText().toString());
        item.put("IsValid", false);
        item.put("CompanyName", companyNameTextField.getText().toString());
        item.put("CompanyAddress", companyAddressTextField.getText().toString());
        item.put("CompanyEmail", companyEmailTextField.getText().toString());
        item.put("CompanyDescription", companyDescriptionTextField.getText().toString());
        item.put("CompanyPhone", companyPhoneNumberTextField.getText().toString());
        item.put("WorkTime", workTime);

        return item;

    }


}