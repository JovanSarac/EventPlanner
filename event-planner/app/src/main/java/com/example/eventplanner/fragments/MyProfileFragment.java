package com.example.eventplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.activities.HomeTwoActivity;
import com.example.eventplanner.databinding.ActivityHomeTwoBinding;
import com.example.eventplanner.databinding.FragmentMyProfileBinding;
import com.example.eventplanner.model.UserOD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;

    private HomeTwoActivity activityHomeTwo;

    private ActivityHomeTwoBinding activityHomeTwoBinding;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private UserOD userOd;

    private boolean checkUpdate = false;

    Uri selectedImage;

    private String pass;


    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activityHomeTwo = new HomeTwoActivity();
        activityHomeTwoBinding = ActivityHomeTwoBinding.inflate(getLayoutInflater());

        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if(user!= null ){
            loadImage(user.getUid(),binding.imageProfile);
        }

        if(user!= null && user.getDisplayName().equals("OD")){
            getUserOd(user.getUid()).thenAccept(userOD -> {
                this.userOd = userOD;

                this.pass = this.userOd.getPassword();

                binding.firstNameInput.setText(this.userOd.getFirstName());
                binding.lastNameInput.setText(this.userOd.getLastName());
                binding.emailInput.setText(this.userOd.getEmail());
                binding.addressInput.setText(this.userOd.getAddress());
                binding.phoneInput.setText(this.userOd.getPhone());

            });
        }

        binding.firstNameInput.setEnabled(false);
        binding.lastNameInput.setEnabled(false);
        binding.emailInput.setEnabled(false);
        binding.phoneInput.setEnabled(false);
        binding.addressInput.setEnabled(false);
        binding.firstNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.lastNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.emailInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.phoneInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.addressInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));

        binding.editMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkUpdate){
                    binding.firstNameInput.setEnabled(true);
                    binding.lastNameInput.setEnabled(true);
                    binding.phoneInput.setEnabled(true);
                    binding.addressInput.setEnabled(true);

                    binding.firstNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.lastNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.phoneInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.addressInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                    binding.editMyProfile.setText("Update");
                    checkUpdate = true;

                }else{
                    checkUpdate = false;

                    binding.firstNameInput.setEnabled(false);
                    binding.lastNameInput.setEnabled(false);
                    binding.phoneInput.setEnabled(false);
                    binding.addressInput.setEnabled(false);
                    binding.firstNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.lastNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.phoneInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.addressInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));

                    binding.editMyProfile.setText("Edit");

                    if(user.getDisplayName().equals("OD")){
                        updateUserOd();

                    }
                }

            }
        });

        binding.editImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        return root;
    }

    private AlertDialog changePasswordDialog;

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView)
                .setTitle("Change password")
                .setPositiveButton("Confirm", null) // Null omogućava da se dijalog ne zatvara automatski
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        changePasswordDialog = builder.create();

        changePasswordDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = changePasswordDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldPassword = ((EditText) dialogView.findViewById(R.id.oldPasswordInput)).getText().toString();
                        String newPassword = ((EditText) dialogView.findViewById(R.id.newPasswordInput)).getText().toString();
                        String confirmNewPassword = ((EditText) dialogView.findViewById(R.id.confirmNewPasswordInput)).getText().toString();

                        if (oldPassword.equals(pass)) {
                            if(newPassword.equals("") || confirmNewPassword.equals("")){
                                Toast.makeText(getContext(), "New password dont be empty.", Toast.LENGTH_SHORT).show();
                            }else{
                                if (newPassword.equals(confirmNewPassword)) {
                                    changePassword(oldPassword, newPassword);
                                    changePasswordDialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Toast.makeText(getContext(), "Old password is not correct!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        changePasswordDialog.show();
    }

    private void changePassword(String oldPassword, String newPassword) {
        Map<String, Object> passwordUpdate = new HashMap<>();
        passwordUpdate.put("Password", newPassword);
        db.collection("User").document(user.getUid())
                .update(passwordUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        getUserOd(user.getUid());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error while changing password.", Toast.LENGTH_LONG).show();

                    }
                });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();

            binding.imageProfile.setImageURI(selectedImage);
            uploadImage(user.getUid());

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
                Log.d("TAG", "Image uploaded successfully");


                //activityHomeTwo.loadImage(user.getUid(), activityHomeTwoBinding.navView.getHeaderView(0).findViewById(R.id.user_image));
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

    private void updateUserOd() {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("Address", binding.addressInput.getText().toString());
        userUpdates.put("E-mail", binding.emailInput.getText().toString());
        userUpdates.put("FirstName", binding.firstNameInput.getText().toString());
        userUpdates.put("IsValid", false);
        userUpdates.put("LastName", binding.lastNameInput.getText().toString());
        userUpdates.put("Phone", binding.phoneInput.getText().toString());

        db.collection("User").document(user.getUid())
                .update(userUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Your profile is updated", Toast.LENGTH_LONG).show();
                        getUserOd(user.getUid());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error while updated profile.", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private CompletableFuture<UserOD> getUserOd(String uid) {
        CompletableFuture<UserOD> future = new CompletableFuture<>();

        db.collection("User").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("HomeTwoActivity", "DocumentSnapshot data: " + document.getData());
                                UserOD userOdd = new UserOD();
                                userOdd.setFirstName((String) document.get("FirstName"));
                                userOdd.setLastName((String) document.get("LastName"));
                                userOdd.setEmail((String) document.get("E-mail"));
                                userOdd.setPassword((String) document.get("Password"));
                                userOdd.setPhone((String) document.get("Phone"));
                                userOdd.setAddress((String) document.get("Address"));
                                userOdd.setValid((Boolean) document.get("IsValid"));

                                userOd = userOdd;

                                pass = userOd.getPassword();

                                future.complete(userOdd);
                            } else {
                                Log.e("HomeTwoActivity", "No such document");
                                future.completeExceptionally(new Exception("No such document"));
                            }
                        } else {
                            Log.e("HomeTwoActivity", "Error getting document", task.getException());
                            future.completeExceptionally(task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("HomeTwoActivity", "Error getting document", e);
                        future.completeExceptionally(e);
                    }
                });

        return future;
    }

    private void loadImage(String userId, ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Referenca na sliku u Firebase Storage
        StorageReference imageRef = storageRef.child("images/" + userId);

        // Korišćenje Glide za učitavanje slike i postavljanje u ImageView
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Koristimo Glide za učitavanje slike
                Glide.with(imageView.getContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Rukovanje greškom prilikom učitavanja slike
                Log.e("HomeTwoActivity", "Error loading image", exception);
                // Možete postaviti default sliku ili prikazati poruku o grešci
                imageView.setImageResource(R.drawable.defaultprofilepicture); // Pretpostavljamo da imate default_image u drawable resursima
            }
        });
    }
}