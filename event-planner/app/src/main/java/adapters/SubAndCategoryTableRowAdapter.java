package adapters;

import static androidx.core.content.ContextCompat.getSystemService;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.ShowOneEventActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import model.SubcategoryPlanner;

public class SubAndCategoryTableRowAdapter extends RecyclerView.Adapter<SubAndCategoryTableRowAdapter.SubAndCategoryViewHolder>{

    private ArrayList<SubcategoryPlanner> subcategories;

    public SubAndCategoryTableRowAdapter(ArrayList<SubcategoryPlanner> subcategories){
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public SubAndCategoryTableRowAdapter.SubAndCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_row_table, parent, false);
        return new SubAndCategoryTableRowAdapter.SubAndCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAndCategoryTableRowAdapter.SubAndCategoryViewHolder holder, int position) {
        SubcategoryPlanner subcategory = subcategories.get(position);
        holder.bind(subcategory);
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public static class SubAndCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameCategory;
        TextView nameSubcategory;
        TextView price;
        TextView serialNum;

        ImageView deleteButton;

        ImageView editButton;

        public SubAndCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.category);
            nameSubcategory = itemView.findViewById(R.id.subcategory);
            price = itemView.findViewById(R.id.amount);
            serialNum = itemView.findViewById(R.id.serialNumber);

            deleteButton = itemView.findViewById(R.id.deleteSub);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popUpView = inflater.inflate(R.layout.confirmation_popup, null);

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.MATCH_PARENT;
                    boolean focusable = true;
                    PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                }
            });

            editButton = itemView.findViewById(R.id.editSub);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = itemView.getContext();

                    LayoutInflater inflater = LayoutInflater.from(context);

                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( itemView.getContext(), R.style.FullScreenBottomSheetDialog);
                    View dialogView =inflater.inflate(R.layout.fragment_add_subcategory_on_budget_planner, null);

                    // Pronalaženje komponenti unutar layouta
                    AutoCompleteTextView editCategory = dialogView.findViewById(R.id.categoryedit);
                    AutoCompleteTextView editSubcategory = dialogView.findViewById(R.id.subcategoryInput);
                    TextInputEditText editPrice = dialogView.findViewById(R.id.priceInput);
                    MaterialButton buttonEdit = dialogView.findViewById(R.id.saveAddBtn);

                    // Postavljanje podataka u komponente
                    editCategory.setText(nameCategory.getText());
                    editSubcategory.setText(nameSubcategory.getText());
                    editPrice.setText(price.getText());
                    buttonEdit.setText("Edit");



                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                }
            });

        }

        public void bind(SubcategoryPlanner subcategory) {
            nameCategory.setText(subcategory.getNameCategory());
            nameSubcategory.setText(subcategory.getNameSubcategory());
            price.setText(subcategory.getPrice());
            serialNum.setText(subcategory.getSerialNum());

        }
    }
}
