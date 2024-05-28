package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Package;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PricelistAdapter<T> extends ArrayAdapter<T> {
    private ArrayList<T> items;
    private FirebaseFirestore db;
    private Context context;
    private int resource;
    public PricelistAdapter(Context context, int resource, ArrayList<T> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        T item = items.get(position);

        TextView serialNumber = convertView.findViewById(R.id.serial_number);
        TextView name = convertView.findViewById(R.id.name);
        TextView price = convertView.findViewById(R.id.price);
        TextView discount = convertView.findViewById(R.id.discount);
        TextView priceWithDiscount = convertView.findViewById(R.id.price_with_discount);

        serialNumber.setText(String.valueOf(position + 1) + ".");

        if (item instanceof Product) {
            Product product = (Product) item;

            name.setText(product.getName());
            price.setText(product.getPrice().toString());
            discount.setText("-" + product.getDiscount().toString() + "%");
            priceWithDiscount.setText("= " + String.format("%.2f", product.getPrice() * (1 - product.getDiscount()*0.01)));
        } else if (item instanceof Service) {
            Service service = (Service) item;

            name.setText(service.getName());
            price.setText(service.getFullPrice().toString());
            discount.setText("-" + service.getDiscount().toString() + "%");
            priceWithDiscount.setText("= " + String.format("%.2f", service.getFullPrice() * (1 - service.getDiscount()*0.01)));
        } else if (item instanceof Package) {
            Package pkg = (Package) item;

            name.setText(pkg.getName());
            price.setText(pkg.getPrice().toString());
            discount.setText("-" + pkg.getDiscount().toString() + "%");
            priceWithDiscount.setText("= " + String.format("%.2f", pkg.getPrice() * (1 - pkg.getDiscount()*0.01)));
        }

        TextInputLayout editPriceLayout = convertView.findViewById(R.id.edit_price);
        TextInputEditText priceEditText = convertView.findViewById(R.id.priceEditText);

        if(!(item instanceof Package)) {
            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editPriceLayout.getEditText().setText(price.getText());
                    editPriceLayout.setVisibility(View.VISIBLE);
                    price.setVisibility(View.GONE);
                }
            });
        }

        priceEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String updatedPriceText = editPriceLayout.getEditText().getText().toString();
                    double updatedPrice = Double.parseDouble(updatedPriceText);

                    price.setText(updatedPriceText);
                    price.setVisibility(View.VISIBLE);
                    editPriceLayout.setVisibility(View.GONE);

                    T item = items.get(position);
                    if (item instanceof Product) {
                        ((Product) item).setPrice(updatedPrice);
                    } else if (item instanceof Service) {
                        ((Service) item).setFullPrice(updatedPrice);
                    } else if (item instanceof Package) {
                        ((Package) item).setPrice(updatedPrice);
                    }

                    notifyDataSetChanged();

                    return true;
                }
                return false;
            }
        });

        TextInputLayout editDiscountLayout = convertView.findViewById(R.id.edit_discount);
        TextInputEditText discountEditText = convertView.findViewById(R.id.discountEditText);

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDiscountLayout.getEditText().setText(discount.getText());
                editDiscountLayout.setVisibility(View.VISIBLE);
                discount.setVisibility(View.GONE);
            }
        });

        discountEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String updatedDiscountText = editDiscountLayout.getEditText().getText().toString();
                    double updatedDiscount = Double.parseDouble(updatedDiscountText.replace("-", "").replace("%", ""));

                    discount.setText("-" + updatedDiscountText + "%");
                    discount.setVisibility(View.VISIBLE);
                    editDiscountLayout.setVisibility(View.GONE);

                    if (item instanceof Product) {
                        ((Product) item).setDiscount(updatedDiscount);
                        priceWithDiscount.setText("= " + String.format("%.2f", ((Product) item).getPrice() * (1 - updatedDiscount * 0.01)));
                    } else if (item instanceof Service) {
                        ((Service) item).setDiscount(updatedDiscount);
                        priceWithDiscount.setText("= " + String.format("%.2f", ((Service) item).getFullPrice() * (1 - updatedDiscount * 0.01)));
                    } else if (item instanceof Package) {
                        ((Package) item).setDiscount(updatedDiscount);
                        priceWithDiscount.setText("= " + String.format("%.2f", ((Package) item).getPrice() * (1 - updatedDiscount * 0.01)));
                    }

                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }
}
