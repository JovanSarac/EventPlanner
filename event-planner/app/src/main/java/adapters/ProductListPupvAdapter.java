package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.CreateProductActivity;
import com.example.eventplanner.EditProductActivity;
import com.example.eventplanner.R;

import java.util.ArrayList;

import model.Product;

public class ProductListPupvAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;

    public ProductListPupvAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card_pupv, products);
        this.products = products;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card_pupv, parent, false);
        }

        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView productPrice = convertView.findViewById(R.id.product_price);

        if(product != null){
            productImage.setImageResource(product.getImageId());
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getPrice().toString() + "$");
        }

        Button editButton = convertView.findViewById(R.id.product_edit);

        final View finalConvertView = convertView;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), EditProductActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productCategory", product.getCategory());
                intent.putExtra("productSubcategory", product.getSubcategory());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productDescription", product.getDescription());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("productDiscount", product.getDiscount());
                intent.putExtra("productImage", product.getImageId());
                intent.putExtra("productEvents", product.getEvents());
                intent.putExtra("productAvailability", product.getAvailable());
                intent.putExtra("productVisibility", product.getVisible());

                finalConvertView.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
