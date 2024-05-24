package com.example.eventplanner.adapters;

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
import com.example.eventplanner.model.GuestEvent;
import com.example.eventplanner.model.SubcategoryPlanner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GuestEventListAdapter extends  RecyclerView.Adapter<GuestEventListAdapter.GuestEventViewHolder>{
    private static ArrayList<GuestEvent> guestsEvent;

    public GuestEventListAdapter(ArrayList<GuestEvent> guestsEvent){
        this.guestsEvent = guestsEvent;
    }

    @NonNull
    @Override
    public GuestEventListAdapter.GuestEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_row_table, parent, false);
        return new GuestEventListAdapter.GuestEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestEventListAdapter.GuestEventViewHolder holder, int position) {
        GuestEvent guestEvent = guestsEvent.get(position);
        holder.bind(guestEvent,position);
    }

    @Override
    public int getItemCount() {
        return guestsEvent.size();
    }




    public static class GuestEventViewHolder extends RecyclerView.ViewHolder {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        BottomSheetDialog bottomSheetDialog;
        TextView serialNumber;
        TextView fullname;

        TextView age;

        TextView invite;
        TextView accept;
        TextView specialRequests;

        ShowOneEventActivity activity;

        ImageView deleteGuestButton;

        ImageView editGuestButton;
        public GuestEventViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = (ShowOneEventActivity) itemView.getContext();
            fullname = itemView.findViewById(R.id.fullNameGuest);
            age = itemView.findViewById(R.id.ageGuest);
            invite = itemView.findViewById(R.id.invited);
            accept = itemView.findViewById(R.id.acceptedInvite);
            specialRequests = itemView.findViewById(R.id.specialRequest);
            serialNumber = itemView.findViewById(R.id.serialNumberGuest);

            deleteGuestButton = itemView.findViewById(R.id.deleteGuest);
            editGuestButton = itemView.findViewById(R.id.editGuest);

            deleteGuestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popUpView = inflater.inflate(R.layout.confirmation_popup, null);



                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.MATCH_PARENT;
                    boolean focusable = true;
                    PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    Button cancelButton = popUpView.findViewById(R.id.cancel_button);
                    Button deleteButton = popUpView.findViewById(R.id.delete_button);

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
                    });*/


                }
            });


            editGuestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Context context = itemView.getContext();

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

                    buttonEdit.setText("Edit");

                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (validateCreteSubcategoryPlanner(editPrice))
                                return;

                            updateSubcategoryPlanner(new SubcategoryPlanner(Long.parseLong(String.valueOf(idSubcatPlanner.getText())), editCategory.getText().toString() , editSubcategory.getText().toString(),Float.parseFloat(String.valueOf(editPrice.getText()))));
                            bottomSheetDialog.dismiss();
                        }

                    });



                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();*/
                }
            });
        }

        public void bind(GuestEvent guestEvent, int position) {
            fullname.setText(guestEvent.getFullname());
            age.setText(guestEvent.getAge());
            invite.setText(guestEvent.getInvite());
            accept.setText(guestEvent.getAcceptInvite());
            specialRequests.setText(guestEvent.getSpecialRequests());
            serialNumber.setText(String.valueOf(position + 1));

        }
    }
}
