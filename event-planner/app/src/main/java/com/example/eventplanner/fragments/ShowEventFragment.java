package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.databinding.FragmentShowEventBinding;

import java.util.ArrayList;
import java.util.Date;

import com.example.eventplanner.adapters.EventRecyclerViewAdapter;

import com.example.eventplanner.model.Event;


public class ShowEventFragment extends Fragment {
    private FragmentShowEventBinding binding;


    public static ShowEventFragment newInstance() {
        return new ShowEventFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.eventsRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Event> events = createEvents();
        EventRecyclerViewAdapter adapterEvents = new EventRecyclerViewAdapter(events);
        recyclerView.setAdapter(adapterEvents);


        return root;
    }

    private ArrayList<Event> createEvents() {
        ArrayList<Event> events = new ArrayList<>();

        events.add((new Event("Vencanje", "Venčanje T i M" , " Ovo je dan kada se dvoje ljudi " +
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
                "Beograd", 50, new Date(), true)));

        return events;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}