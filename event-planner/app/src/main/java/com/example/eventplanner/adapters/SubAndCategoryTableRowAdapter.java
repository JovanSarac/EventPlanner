package com.example.eventplanner.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.ShowOneEventActivity;
import com.example.eventplanner.databinding.FragmentAddSubcategoryOnBudgetPlannerBinding;
import com.example.eventplanner.model.SubcategoryPlanner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.eventplanner.model.SubcategoryPlanner;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubAndCategoryTableRowAdapter extends RecyclerView.Adapter<SubAndCategoryTableRowAdapter.SubAndCategoryViewHolder>{

    private static ArrayList<SubcategoryPlanner> subcategories;

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
        holder.bind(subcategory,position);
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

        TextView idSubcategoryPlanner;

        ImageView deleteButton;

        ImageView editButton;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        BottomSheetDialog bottomSheetDialog;

        ShowOneEventActivity activity;



        public SubAndCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = (ShowOneEventActivity) itemView.getContext();
            nameCategory = itemView.findViewById(R.id.category);
            nameSubcategory = itemView.findViewById(R.id.subcategory);
            price = itemView.findViewById(R.id.amount);
            serialNum = itemView.findViewById(R.id.serialNumber);

            deleteButton = itemView.findViewById(R.id.deleteSub);
            idSubcategoryPlanner = itemView.findViewById(R.id.idSubcategoryPlanner);



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

                    Button cancelButton = popUpView.findViewById(R.id.cancel_button);
                    Button deleteButton = popUpView.findViewById(R.id.create_button);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();


                            if (position != RecyclerView.NO_POSITION) {
                                // Pristupanje elementu u listi podataka na odgovarajućoj poziciji
                                SubcategoryPlanner subcategory = subcategories.get(position);

                                // Sada možete izvršiti željene akcije sa tim elementom
                                // Na primjer, možete prikazati dijalog za potvrdu brisanja ili izvršiti brisanje direktno
                                //showDeleteConfirmationDialog(subcategory);
                                deleteSubcategoryPlanner(subcategory);
                                popupWindow.dismiss();
                            }

                        }
                    });


                }
            });

            editButton = itemView.findViewById(R.id.editSub);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = itemView.getContext();

                    LayoutInflater inflater = LayoutInflater.from(context);

                    bottomSheetDialog = new BottomSheetDialog( itemView.getContext(), R.style.FullScreenBottomSheetDialog);
                    View dialogView =inflater.inflate(R.layout.fragment_add_subcategory_on_budget_planner, null);


                    // Pronalaženje komponenti unutar layouta
                    AutoCompleteTextView editCategory = dialogView.findViewById(R.id.categoryInput);
                    AutoCompleteTextView editSubcategory = dialogView.findViewById(R.id.subcategoryInput);
                    TextInputEditText editPrice = dialogView.findViewById(R.id.priceInput);
                    TextView idSubcatPlanner = dialogView.findViewById(R.id.idsubPlanner);
                    MaterialButton buttonEdit = dialogView.findViewById(R.id.saveAddBtn);

                    // Postavljanje podataka u komponente
                    editCategory.setText(nameCategory.getText());
                    editSubcategory.setText(nameSubcategory.getText());
                    editPrice.setText(price.getText());
                    idSubcatPlanner.setText(idSubcategoryPlanner.getText());

                    String[] Category = {"Ugostiteljski objekti, hrana, ketering, torte i kolači", "Muzika i zabava", "Smjestaj", "Logistika i obezbeđenje"};
                    String[] Subcategories = {"Subcategory", "Hrana za događaje", "Ketering i priprema hrane", "Iznajmljivanje ugostiteljskih objekata za događaje", "Fotografisanje"};

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_dropdown_item_1line, Category);
                    editCategory.setAdapter(adapter);

                    // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
                    editCategory.setOnItemClickListener((parent, view, position, id) -> {
                        //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                        String selectedCategory = (String) parent.getItemAtPosition(position);

                        System.out.println("Selected category: " + selectedCategory);
                    });

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_dropdown_item_1line, Subcategories);
                    editSubcategory.setAdapter(adapter2);

                    // Dodavanje slušatelja za AutoCompleteTextView ako želite reagirati na odabir
                    editSubcategory.setOnItemClickListener((parent, view, position, id) -> {
                        //Ovdje možete dodati kôd koji se izvršava kada korisnik odabere neku stavku
                        String selectedSubcategory = (String) parent.getItemAtPosition(position);

                        System.out.println("Selected category: " + selectedSubcategory);
                    });




                    buttonEdit.setText("Edit");

                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateSubcategoryPlanner(new SubcategoryPlanner(Long.parseLong(String.valueOf(idSubcatPlanner.getText())), editCategory.getText().toString() , editSubcategory.getText().toString(),Float.parseFloat(String.valueOf(editPrice.getText()))));
                            bottomSheetDialog.dismiss();
                        }

                    });



                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                }
            });

        }

        public void bind(SubcategoryPlanner subcategory, int position) {
            nameCategory.setText(subcategory.getNameCategory());
            nameSubcategory.setText(subcategory.getNameSubcategory());
            price.setText(subcategory.getPrice().toString());
            serialNum.setText(String.valueOf(position + 1));
            idSubcategoryPlanner.setText((subcategory.getSerialNum().toString()));

        }

        private void updateSubcategoryPlanner(SubcategoryPlanner subcategoryPlanner) {


            Map<String, Object> updates = new HashMap<>();
            updates.put("nameCategory", subcategoryPlanner.getNameCategory());
            updates.put("nameSubcategory", subcategoryPlanner.getNameSubcategory());
            updates.put("price", subcategoryPlanner.getPrice());

            db.collection("SubcategoryPlanner").document(subcategoryPlanner.getSerialNum().toString()).update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Document successfully updated!");
                            activity.getSubcategoryPlanner();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });


        }

        private void deleteSubcategoryPlanner(SubcategoryPlanner subcategoryPlanner) {

            db.collection("SubcategoryPlanner").document(subcategoryPlanner.getSerialNum().toString()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Document successfully deleted!");
                            activity.getSubcategoryPlanner();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });


        }
    }
}
