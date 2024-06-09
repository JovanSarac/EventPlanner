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
    private UserPUPZ userPupz;
    private UserPUPV userPupv;

    private boolean checkUpdate = false;
    private boolean checkUpdateCompany = false;

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
        }else if(user!= null && user.getDisplayName().equals("PUPZ")){
            getUserPupz(user.getUid()).thenAccept(userPUPZ -> {
                this.userPupz = userPUPZ;

                this.pass = this.userPupz.getPassword();

                binding.firstNameInput.setText(this.userPupz.getFirstName());
                binding.lastNameInput.setText(this.userPupz.getLastName());
                binding.emailInput.setText(this.userPupz.getEmail());
                binding.addressInput.setText(this.userPupz.getAddress());
                binding.phoneInput.setText(this.userPupz.getPhone());

            });
        }else if(user!= null && user.getDisplayName().equals("PUPV")){
            getUserPupv(user.getUid()).thenAccept(userPUPV -> {
                this.userPupv = userPUPV;

                this.pass = this.userPupv.getPassword();

                binding.firstNameInput.setText(this.userPupv.getFirstName());
                binding.lastNameInput.setText(this.userPupv.getLastName());
                binding.emailInput.setText(this.userPupv.getEmail());
                binding.addressInput.setText(this.userPupv.getAddress());
                binding.phoneInput.setText(this.userPupv.getPhone());

                binding.companyInfo.setVisibility(View.VISIBLE);

                binding.companyName.setText(this.userPupv.getCompanyName());
                binding.companyDescription.setText(this.userPupv.getCompanyDescription());
                binding.companyAddress.setText(this.userPupv.getCompanyAddress());
                binding.companyPhone.setText(this.userPupv.getCompanyPhone());
                binding.companyEmail.setText(this.userPupv.getCompanyemail());

                /*binding.worktimeCompany.titleWorkTime.setText("Work time company:");
                binding.worktimeCompany.titleWorkTime.setTextColor(Color.WHITE);
                binding.worktimeCompany.mondayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.tuesdayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.wednesdayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.thursdayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.fridayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.saturdayText.setTextColor(Color.WHITE);
                binding.worktimeCompany.sundayText.setTextColor(Color.WHITE);
                String[] workTimeArray = this.userPupv.getWorkTime().split("\\?");

                if(workTimeArray[0].equals("free")){
                    binding.worktimeCompany.mondayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[0].split("-");
                    binding.worktimeCompany.startMonday.setText(time[0]);
                    binding.worktimeCompany.endMonday.setText(time[1]);
                }

                if(workTimeArray[1].equals("free")){
                    binding.worktimeCompany.tuesdayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[1].split("-");
                    binding.worktimeCompany.startTuesday.setText(time[0]);
                    binding.worktimeCompany.endTuesday.setText(time[1]);
                }

                if(workTimeArray[2].equals("free")){
                    binding.worktimeCompany.wednesdayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[2].split("-");
                    binding.worktimeCompany.startWednesday.setText(time[0]);
                    binding.worktimeCompany.endWednesday.setText(time[1]);
                }

                if(workTimeArray[3].equals("free")){
                    binding.worktimeCompany.thursdayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[3].split("-");
                    binding.worktimeCompany.startThursday.setText(time[0]);
                    binding.worktimeCompany.endThursday.setText(time[1]);
                }

                if(workTimeArray[4].equals("free")){
                    binding.worktimeCompany.fridayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[4].split("-");
                    binding.worktimeCompany.startFriday.setText(time[0]);
                    binding.worktimeCompany.endFriday.setText(time[1]);
                }

                if(workTimeArray[5].equals("free")){
                    binding.worktimeCompany.saturdayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[5].split("-");
                    binding.worktimeCompany.startSaturday.setText(time[0]);
                    binding.worktimeCompany.endSaturday.setText(time[1]);
                }

                if(workTimeArray[6].equals("free")){
                    binding.worktimeCompany.sundayCheckbox.isChecked();
                }else{
                    String[] time = workTimeArray[6].split("-");
                    binding.worktimeCompany.startSunday.setText(time[0]);
                    binding.worktimeCompany.endSunday.setText(time[1]);
                }*/


            });
        }

        binding.firstNameInput.setEnabled(false);
        binding.lastNameInput.setEnabled(false);
        binding.emailInput.setEnabled(false);
        binding.phoneInput.setEnabled(false);
        binding.addressInput.setEnabled(false);
        binding.companyName.setEnabled(false);
        binding.companyDescription.setEnabled(false);
        binding.companyAddress.setEnabled(false);
        binding.companyPhone.setEnabled(false);
        binding.companyEmail.setEnabled(false);
        /*binding.worktimeCompany.startMonday.setEnabled(false);
        binding.worktimeCompany.endMonday.setEnabled(false);
        binding.worktimeCompany.startTuesday.setEnabled(false);
        binding.worktimeCompany.endTuesday.setEnabled(false);
        binding.worktimeCompany.startWednesday.setEnabled(false);
        binding.worktimeCompany.endWednesday.setEnabled(false);
        binding.worktimeCompany.startThursday.setEnabled(false);
        binding.worktimeCompany.endThursday.setEnabled(false);
        binding.worktimeCompany.startFriday.setEnabled(false);
        binding.worktimeCompany.endFriday.setEnabled(false);
        binding.worktimeCompany.startSunday.setEnabled(false);
        binding.worktimeCompany.endSunday.setEnabled(false);
        binding.worktimeCompany.startSaturday.setEnabled(false);
        binding.worktimeCompany.endSaturday.setEnabled(false);*/
        binding.firstNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.lastNameInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.emailInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.phoneInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.addressInput.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.companyName.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.companyDescription.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.companyAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.companyPhone.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.companyEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));

        /*binding.worktimeCompany.startMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.startSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        binding.worktimeCompany.endSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));*/

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

                    updateUser();

                }

            }
        });

        binding.editCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkUpdateCompany){
                    binding.companyDescription.setEnabled(true);
                    binding.companyAddress.setEnabled(true);
                    binding.companyPhone.setEnabled(true);
                    /*binding.worktimeCompany.startMonday.setEnabled(true);
                    binding.worktimeCompany.endMonday.setEnabled(true);
                    binding.worktimeCompany.startTuesday.setEnabled(true);
                    binding.worktimeCompany.endTuesday.setEnabled(true);
                    binding.worktimeCompany.startWednesday.setEnabled(true);
                    binding.worktimeCompany.endWednesday.setEnabled(true);
                    binding.worktimeCompany.startThursday.setEnabled(true);
                    binding.worktimeCompany.endThursday.setEnabled(true);
                    binding.worktimeCompany.startFriday.setEnabled(true);
                    binding.worktimeCompany.endFriday.setEnabled(true);
                    binding.worktimeCompany.startSunday.setEnabled(true);
                    binding.worktimeCompany.endSunday.setEnabled(true);
                    binding.worktimeCompany.startSaturday.setEnabled(true);
                    binding.worktimeCompany.endSaturday.setEnabled(true);*/

                    binding.companyDescription.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.companyAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.companyPhone.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                    /*binding.worktimeCompany.startMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.startSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.worktimeCompany.endSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));*/

                    binding.editCompany.setText("Update company");
                    checkUpdateCompany = true;
                }else{
                    checkUpdateCompany = false;

                    binding.companyDescription.setEnabled(false);
                    binding.companyAddress.setEnabled(false);
                    binding.companyPhone.setEnabled(false);

                    /*binding.worktimeCompany.startMonday.setEnabled(false);
                    binding.worktimeCompany.endMonday.setEnabled(false);
                    binding.worktimeCompany.startTuesday.setEnabled(false);
                    binding.worktimeCompany.endTuesday.setEnabled(false);
                    binding.worktimeCompany.startWednesday.setEnabled(false);
                    binding.worktimeCompany.endWednesday.setEnabled(false);
                    binding.worktimeCompany.startThursday.setEnabled(false);
                    binding.worktimeCompany.endThursday.setEnabled(false);
                    binding.worktimeCompany.startFriday.setEnabled(false);
                    binding.worktimeCompany.endFriday.setEnabled(false);
                    binding.worktimeCompany.startSunday.setEnabled(false);
                    binding.worktimeCompany.endSunday.setEnabled(false);
                    binding.worktimeCompany.startSaturday.setEnabled(false);
                    binding.worktimeCompany.endSaturday.setEnabled(false);*/

                    binding.companyDescription.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.companyAddress.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.companyPhone.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));

                    /*binding.worktimeCompany.startMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endMonday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endTuesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endWednesday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endThursday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endFriday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endSunday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.startSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
                    binding.worktimeCompany.endSaturday.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));*/



                    binding.editCompany.setText("Edit company");

                    updateCompany();
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

    private void updateCompany() {
        Map<String, Object> companyUpdate = new HashMap<>();
        companyUpdate.put("CompanyDescription", binding.companyDescription.getText().toString());
        companyUpdate.put("CompanyAddress", binding.companyAddress.getText().toString());
        companyUpdate.put("CompanyPhone", binding.companyPhone.getText().toString());

        db.collection("User").document(user.getUid())
                .update(companyUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Your profile is updated", Toast.LENGTH_LONG).show();
                        if(user.getDisplayName().equals("PUPV")){
                            getUserPupv(user.getUid());
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error while updated profile.", Toast.LENGTH_LONG).show();

                    }
                });
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

                        if(user.getDisplayName().equals("OD")){
                            getUserOd(user.getUid());
                        }else if(user.getDisplayName().equals("PUPZ")){
                            getUserPupz(user.getUid());
                        }else if(user.getDisplayName().equals("PUPV")){
                            getUserPupv(user.getUid());
                        }


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

    private void updateUser() {
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
                        if(user.getDisplayName().equals("OD")){
                            getUserOd(user.getUid());
                        }else if(user.getDisplayName().equals("PUPZ")){
                            getUserPupz(user.getUid());
                        }else if(user.getDisplayName().equals("PUPV")){
                            getUserPupv(user.getUid());
                        }


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

    private CompletableFuture<UserPUPZ> getUserPupz(String uid) {
        CompletableFuture<UserPUPZ> future = new CompletableFuture<>();

        db.collection("User").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("HomeTwoActivity", "DocumentSnapshot data: " + document.getData());
                                UserPUPZ userPupzz = new UserPUPZ();
                                userPupzz.setFirstName((String) document.get("firstName"));
                                userPupzz.setLastName((String) document.get("lastName"));
                                userPupzz.setEmail((String) document.get("email"));
                                userPupzz.setPassword((String) document.get("password"));
                                userPupzz.setPhone((String) document.get("phone"));
                                userPupzz.setAddress((String) document.get("address"));
                                userPupzz.setValid((Boolean) document.get("valid"));
                                userPupzz.setOwnerId((String) document.get("ownerId"));

                                userPupz = userPupzz;

                                pass = userPupzz.getPassword();

                                future.complete(userPupzz);
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

    private CompletableFuture<UserPUPV> getUserPupv(String uid) {
        CompletableFuture<UserPUPV> future = new CompletableFuture<>();

        db.collection("User").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("HomeTwoActivity", "DocumentSnapshot data: " + document.getData());
                                UserPUPV userPupvv = new UserPUPV();
                                userPupvv.setFirstName((String) document.get("FirstName"));
                                userPupvv.setLastName((String) document.get("LastName"));
                                userPupvv.setEmail((String) document.get("E-mail"));
                                userPupvv.setPassword((String) document.get("Password"));
                                userPupvv.setPhone((String) document.get("Phone"));
                                userPupvv.setAddress((String) document.get("Address"));
                                userPupvv.setValid((Boolean) document.get("IsValid"));
                                userPupvv.setCompanyName((String) document.get("CompanyName"));
                                userPupvv.setCompanyDescription((String) document.get("CompanyDescription"));
                                userPupvv.setCompanyAddress((String) document.get("CompanyAddress"));
                                userPupvv.setCompanyemail((String) document.get("CompanyEmail"));
                                userPupvv.setCompanyPhone((String) document.get("CompanyPhone"));
                                userPupvv.setWorkTime((String) document.get("WorkTime"));

                                userPupv = userPupvv;

                                pass = userPupvv.getPassword();

                                future.complete(userPupvv);
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

        StorageReference imageRef = storageRef.child("images/" + userId);

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
                Log.e("HomeTwoActivity", "Error loading image", exception);
                imageView.setImageResource(R.drawable.defaultprofilepicture);
            }
        });
    }
}