package com.example.eventplanner.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventplanner.databinding.FragmentShowEventBinding;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;

import com.example.eventplanner.adapters.EventRecyclerViewAdapter;

import com.example.eventplanner.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ShowEventFragment extends Fragment {
    private FragmentShowEventBinding binding;
    FirebaseFirestore db;
    ArrayList<Event> events;

    RecyclerView recyclerView;

    public static ShowEventFragment newInstance() {
        return new ShowEventFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.eventsRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //ArrayList<Event> events = createEvents();
        db = FirebaseFirestore.getInstance();
        createEvents();



        return root;
    }

    private void createEvents() {
        //ArrayList<Event> events = new ArrayList<>();

        events = new ArrayList<>();

        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Event event = new Event(doc.getString("typeEvent"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    Integer.parseInt(String.valueOf(doc.getLong("maxPeople"))),
                                    doc.getString("locationPlace"),
                                    Integer.parseInt(String.valueOf(doc.getLong("maxDistance"))),
                                    doc.getDate("dateEvent"),
                                    doc.getBoolean("available"));

                            //System.out.println(event.getName());


                            events.add(event);

                            EventRecyclerViewAdapter adapterEvents = new EventRecyclerViewAdapter(events);
                            recyclerView.setAdapter(adapterEvents);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        /*events.add((new Event("Vencanje", "Venčanje T i M" , " Ovo je dan kada se dvoje ljudi " +
                "obećavaju jedno drugome vječnu ljubav i zajednički život. Pridružite nam se u ovom posebnom trenutku " +
                "dok zajedno sa porodicom i prijateljima slavimo ljubav i stvaramo nezaboravne uspomene. " +
                "Očekuje nas čarobno iskustvo, ispunjeno osmjesima, toplinom i veseljem, dok se započinje " +
                "novo poglavlje u životima mladenaca.", 200, "Novi Sad", 40, new Date(), true)));

        events.add((new Event("Koncerti i muzicki nastupi", "Koncert Aleksandre Prijovic" ,
                "Dobrodošli na nezaboravan koncert Aleksandre Prijović, gdje će se spojiti njena strastvena" +
                        " izvedba i nevjerojatan talent, pružajući vam veče puno uzbuđenja i muzičkih trenutaka za " +
                        "pamćenje. Pripremite se za veče puno energije, emocija i nezaboravnih hitova dok zajedno sa " +
                        "publikom stvaramo atmosferu punu ljubavi i zabave. Pridružite nam se na ovoj spektakularnoj" +
                        " večeri uz Aleksandrinu prepoznatljivu interpretaciju i uživajte u nezaboravnom muzičkom" +
                        " iskustvu koje će vas ostaviti bez daha.", 200, "Beograd",
                50, new Date(), true)));

        events.add((new Event("Turniri i prvenstva", "Turnir u malom fudbalu" , "Ovogodišnji " +
                "turnir okuplja najbolje igrače iz naše zajednice kako bi se nadmetali u uzbudljivim utakmicama punim " +
                "akcije i golova. Pridružite nam se na ovom nezaboravnom sportskom iskustvu uz sjajnu atmosferu i veliku" +
                " podršku navijača. Uzbudljiv vikend malog fudbala je pred nama, obećavajući neizvjesnost, " +
                "emocije i trenutke za pamćenje na terenu našeg turnira", 200,
                "Beograd", 50, new Date(), true)));*/
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}