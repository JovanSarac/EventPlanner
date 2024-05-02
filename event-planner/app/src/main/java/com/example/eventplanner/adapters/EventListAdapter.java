package com.example.eventplanner.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;

import java.util.ArrayList;

public class EventListAdapter  extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private int resource;

    public EventListAdapter(Context context, int resource, ArrayList<Event> events){
        super(context, resource, events);
        this.events = events;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return this.events.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return this.events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());
        return view;
    }
}
