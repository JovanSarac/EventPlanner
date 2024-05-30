package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private UserOD userOd;

    private boolean checkUpdate = false;

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = mAuth.getCurrentUser();

        if(user!= null && user.getDisplayName().equals("OD")){
            getUserOd(user.getUid()).thenAccept(userOD -> {
                this.userOd = userOD;

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

        db = FirebaseFirestore.getInstance();

        return root;
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
                                UserOD userOd = new UserOD();
                                userOd.setFirstName((String) document.get("FirstName"));
                                userOd.setLastName((String) document.get("LastName"));
                                userOd.setEmail((String) document.get("E-mail"));
                                userOd.setPassword((String) document.get("Password"));
                                userOd.setPhone((String) document.get("Phone"));
                                userOd.setAddress((String) document.get("Address"));
                                userOd.setValid((Boolean) document.get("IsValid"));

                                future.complete(userOd);
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
}