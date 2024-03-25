package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;

import java.util.ArrayList;

import model.Product;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;

    public ProductListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card, products);
        this.products = new ArrayList<Product>();

        this.products.add(new Product(1l, "some category", "some subcategory", "name", "description", 100.0, 10.0, null, true, false));
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card, parent, false);
        }

        LinearLayout productCard = convertView.findViewById(R.id.product_card_item);
        TextView productName = convertView.findViewById(R.id.product_id);

        if(product != null){
            productName.setText(product.getName());
        }


        return convertView;
    }
}
