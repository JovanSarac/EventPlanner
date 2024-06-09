package com.example.eventplanner.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.UserPUPZ;

import java.util.ArrayList;

public class EmployeeRecyclerViewAdapter extends RecyclerView.Adapter<EmployeeRecyclerViewAdapter.EmployeeViewHolder>{

    private ArrayList<UserPUPZ> pupzs;

    public EmployeeRecyclerViewAdapter(ArrayList<UserPUPZ> pupzs) {
        this.pupzs = pupzs;
    }

    @NonNull
    @Override
    public EmployeeRecyclerViewAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_card, parent, false);
        return new EmployeeRecyclerViewAdapter.EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeRecyclerViewAdapter.EmployeeViewHolder holder, int position) {
        UserPUPZ pupz = pupzs.get(position);
        holder.bind(pupz);
    }

    @Override
    public int getItemCount() {
        return pupzs.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView pupzFullname;
        Button sendMessage;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            pupzFullname = itemView.findViewById(R.id.employee_fullname);
            sendMessage = itemView.findViewById(R.id.send_message_button);
        }

        @SuppressLint("SetTextI18n")
        public void bind(UserPUPZ pupz) {
            pupzFullname.setText(pupz.getFirstName() + " " + pupz.getLastName());
        }
    }
}
