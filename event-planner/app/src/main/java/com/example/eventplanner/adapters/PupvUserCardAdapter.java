package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.AddSubcategoryActivity;
import com.example.eventplanner.activities.ApproveRegistrationActivity;
import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.activities.SuggestedSubcategoriesActivity;
import com.example.eventplanner.activities.UserDetailsActivity;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.UserPUPV;
import com.google.firebase.firestore.FirebaseFirestore;

public class PupvUserCardAdapter extends RecyclerView.Adapter<PupvUserCardAdapter.ViewHolder> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<UserPUPV> dataList;
    private Context context;

    public PupvUserCardAdapter(List<UserPUPV> dataList,Context context) {
        this.dataList = dataList;
        this.context=context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView emailText;
        public TextView comapanyNameText;
        public TextView comapanyEmailText;

        public ImageView editIcon;
        public ImageView addIcon;
        public ImageView deleteIcon;

        public ImageView detailsIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            emailText = itemView.findViewById(R.id.emailText);
            comapanyNameText = itemView.findViewById(R.id.comapanyNameText);
            comapanyEmailText = itemView.findViewById(R.id.comapanyEmailText);

            editIcon = itemView.findViewById(R.id.iconEditSub);
            addIcon = itemView.findViewById(R.id.iconAddSub);
            deleteIcon = itemView.findViewById(R.id.iconDeleteSub);
            detailsIcon= itemView.findViewById(R.id.detailsIconSub);
        }
    }
    @Override
    public PupvUserCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_register, parent, false);
        return new PupvUserCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PupvUserCardAdapter.ViewHolder holder, int position) {
        UserPUPV data = dataList.get(position);
        holder.nameText.setText(data.getFirstName()+ " " + data.getLastName());
        holder.emailText.setText(data.getEmail());
        holder.comapanyNameText.setText(data.getCompanyName());
        holder.comapanyEmailText.setText(data.getCompanyemail());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        holder.detailsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("user", data);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
