package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Package;

import java.util.ArrayList;

public class PackageListAdapter extends ArrayAdapter<Package> {

    private ArrayList<Package> packages;

    public PackageListAdapter(Context context, ArrayList<Package> packages){
        super(context, R.layout.package_card, packages);
        this.packages = packages;
    }

    @Override
    public int getCount() {
        return this.packages.size();
    }

    @Nullable
    @Override
    public Package getItem(int position) {
        return this.packages.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Package pckage = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.package_card, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView description = convertView.findViewById(R.id.description);
        TextView price = convertView.findViewById(R.id.price);
        TextView products = convertView.findViewById(R.id.products);
        TextView services = convertView.findViewById(R.id.services);


        if(pckage != null){
            name.setText(pckage.getName());
            description.setText(pckage.getDescription());
            price.setText(pckage.getPrice().toString() + "$");
            products.setText(String.valueOf(pckage.getProductIds().size()));
            services.setText(String.valueOf(pckage.getServiceIds().size()));
        }

        return convertView;
    }
}
