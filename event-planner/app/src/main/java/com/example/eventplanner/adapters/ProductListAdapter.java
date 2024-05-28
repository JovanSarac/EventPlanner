package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;

import java.util.ArrayList;

import com.example.eventplanner.model.Product;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    Button remove;
    int resourece;
    private OnItemRemovedListener onItemRemovedListener;
    public interface OnItemRemovedListener {
        void onProductRemoved(Product removedItem);
    }
    public void setOnItemRemovedListener(OnItemRemovedListener listener) {
        this.onItemRemovedListener = listener;
    }

    public ProductListAdapter(Context context, int resourece, ArrayList<Product> products){
        super(context, resourece, products);
        this.products = products;
        this.resourece = resourece;
    }

    @Override
    public int getCount() {
        return this.products.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return this.products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourece, parent, false);
        }

        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView productPrice = convertView.findViewById(R.id.product_price);

        if(product != null){
            Glide.with(getContext())
                    .load(product.getImages().get(0))
                    .into(productImage);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getPrice().toString() + "$");
        }

        if(resourece == R.layout.product_card_package) {

            remove = convertView.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product removedItem = products.get(position);
                    products.remove(position);
                    notifyDataSetChanged();

                    notifyDataSetChanged();

                    if (onItemRemovedListener != null) {
                        onItemRemovedListener.onProductRemoved(removedItem);
                    }
                }

            });
        }


        return convertView;
    }
}
