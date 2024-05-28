package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.activities.EditPackageActivity;
import com.example.eventplanner.R;

import java.util.ArrayList;

import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;

public class PackageListPupvAdapter extends ArrayAdapter<Package> {

    private ArrayList<Package> packages;

    public PackageListPupvAdapter(Context context, ArrayList<Package> packages){
        super(context, R.layout.package_card_pupv, packages);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.package_card_pupv, parent, false);
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

            String productNames = "";
            /*ArrayList<Product> productList = pckage.getProducts();
            for(int i = 0; i < productList.size(); i++){
                productNames += productList.get(i).getName();
                if(i < productList.size() - 1){
                    productNames += ", ";
                }
            }*/

            String serviceNames = "";
            /*ArrayList<Service> serviceList = pckage.getServices();
            for(int i = 0; i < serviceList.size(); i++){
                serviceNames += serviceList.get(i).getName();
                if(i < serviceList.size() - 1){
                    serviceNames += ", ";
                }
            }*/
        }

        Button editButton = convertView.findViewById(R.id.edit);
        Button deleteButton = convertView.findViewById(R.id.delete);

        final View finalConvertView = convertView;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), EditPackageActivity.class);
                intent.putExtra("Id", pckage.getId());
                intent.putExtra("Name", pckage.getName());
                intent.putExtra("Category", pckage.getCategoryId());
                intent.putExtra("Subcategory", pckage.getServiceIds());
                intent.putExtra("Description", pckage.getDescription());
                intent.putExtra("Price", pckage.getPrice());
                intent.putExtra("Discount", pckage.getDiscount());
                intent.putExtra("Products", pckage.getProductIds());
                intent.putExtra("Services", pckage.getServiceIds());
                intent.putExtra("Events", pckage.getEventTypeIds());
                intent.putExtra("ImageTypes", pckage.getImages());
                intent.putExtra("ReservationDue", pckage.getReservationDue());
                intent.putExtra("CancelationDue", pckage.getCancelationDue());
                intent.putExtra("AutomaticAffirmation", pckage.getAutomaticAffirmation());
                intent.putExtra("Availability", pckage.getAvailable());
                intent.putExtra("Visibility", pckage.getVisible());

                finalConvertView.getContext().startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.confirmation_popup, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });


        return convertView;
    }
}
