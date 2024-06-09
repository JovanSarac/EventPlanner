package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.eventplanner.databinding.FragmentShowFavouritesPspBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ShowFavouritesPspFragment extends Fragment {

    FragmentShowFavouritesPspBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowFavouritesPspBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Prikazati listu proizvoda, sakriti ostale
                        binding.productListt.setVisibility(View.VISIBLE);
                        binding.serviceListt.setVisibility(View.GONE);
                        binding.packageListt.setVisibility(View.GONE);
                        break;
                    case 1:
                        // Prikazati listu usluga, sakriti ostale
                        binding.productListt.setVisibility(View.GONE);
                        binding.serviceListt.setVisibility(View.VISIBLE);
                        binding.packageListt.setVisibility(View.GONE);
                        break;
                    case 2:
                        // Prikazati listu paketa, sakriti ostale
                        binding.productListt.setVisibility(View.GONE);
                        binding.serviceListt.setVisibility(View.GONE);
                        binding.packageListt.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Ovdje ne trebate ništa raditi, jer se ne treba reagirati na odabir drugih tabova
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Ovdje ne trebate ništa raditi, jer se ne treba reagirati na ponovni odabir taba
            }
        });


        return root;
    }
}