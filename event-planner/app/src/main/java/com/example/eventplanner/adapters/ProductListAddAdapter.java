package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;

import java.util.ArrayList;

import com.example.eventplanner.model.Product;

public class ProductListAddAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    private OnItemClickListener listener; // Change to your custom OnItemClickListener interface

    public ProductListAddAdapter(Context context, OnItemClickListener listener, ArrayList<Product> products){
        super(context, R.layout.product_card_add, products);
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card_add, parent, false);
        }

        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView productPrice = convertView.findViewById(R.id.product_price);

        Button addProduct = convertView.findViewById(R.id.add_product);

        if(product != null){
            productImage.setImageURI(product.getImages().get(0));
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getPrice().toString() + "$");
        }

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(product);
            }
        });

        return convertView;
    }
    public interface OnItemClickListener {
        void onItemClick(Product item);
    }
}
