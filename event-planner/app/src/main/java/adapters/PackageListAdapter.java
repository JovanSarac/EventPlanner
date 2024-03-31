package adapters;

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

import com.example.eventplanner.EditPackageActivity;
import com.example.eventplanner.R;

import java.util.ArrayList;

import model.Package;
import model.Product;
import model.Service;

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

            String productNames = "";
            ArrayList<Product> productList = pckage.getProducts();
            for(int i = 0; i < productList.size(); i++){
                productNames += productList.get(i).getName();
                if(i < productList.size() - 1){
                    productNames += ", ";
                }
            }

            String serviceNames = "";
            ArrayList<Service> serviceList = pckage.getServices();
            for(int i = 0; i < serviceList.size(); i++){
                serviceNames += serviceList.get(i).getName();
                if(i < serviceList.size() - 1){
                    serviceNames += ", ";
                }
            }
        }

        return convertView;
    }
}
