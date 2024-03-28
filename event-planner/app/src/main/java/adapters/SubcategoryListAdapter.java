package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;

import java.util.ArrayList;

import model.Product;
import model.Subcategory;

/*public class SubcategoryListAdapter extends  ArrayAdapter<Subcategory> {

    private ArrayList<Subcategory> subcategories;

    public SubcategoryListAdapter(Context context, ArrayList<Subcategory> subcategories){
        super(context, R.layout.subcategories_of_servecis_products, subcategories);
        this.subcategories = subcategories;
    }

    @Override
    public int getCount() {
        return this.subcategories.size();
    }

    @Nullable
    @Override
    public Subcategory getItem(int position) {
        return this.subcategories.get(position);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Subcategory subcategory = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategories_of_servecis_products, parent, false);
        }


        TextView categoryName = convertView.findViewById(R.id.nameCategory);
        TextView nameSubcategory = convertView.findViewById(R.id.nameSubcategory);
        TextView descriptionSubcategory = convertView.findViewById(R.id.descriptionSubcategory);
        TextView typeSbcategory = convertView.findViewById(R.id.typeSubcategory);

        if(subcategory != null){
            categoryName.setText(subcategory.getNameCategory());
            nameSubcategory.setText(subcategory.getName());
            descriptionSubcategory.setText(subcategory.getDescription());
            typeSbcategory.setText(subcategory.getTypeSubcategory());
        }


        return convertView;
    }
}*/

public class SubcategoryListAdapter extends RecyclerView.Adapter<SubcategoryListAdapter.SubcategoryViewHolder> {

    private ArrayList<Subcategory> subcategories;

    public SubcategoryListAdapter(ArrayList<Subcategory> subcategories){
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public SubcategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategories_of_servecis_products, parent, false);
        return new SubcategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryViewHolder holder, int position) {
        Subcategory subcategory = subcategories.get(position);
        holder.bind(subcategory);
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public static class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView nameSubcategory;
        TextView descriptionSubcategory;
        TextView typeSubcategory;

        public SubcategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.nameCategory);
            nameSubcategory = itemView.findViewById(R.id.nameSubcategory);
            descriptionSubcategory = itemView.findViewById(R.id.descriptionSubcategory);
            typeSubcategory = itemView.findViewById(R.id.typeSubcategory);
        }

        public void bind(Subcategory subcategory) {
            categoryName.setText(subcategory.getNameCategory());
            nameSubcategory.setText(subcategory.getName());
            descriptionSubcategory.setText(subcategory.getDescription());
            typeSubcategory.setText(subcategory.getTypeSubcategory());
        }
    }
}
