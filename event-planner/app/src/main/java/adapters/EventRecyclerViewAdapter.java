package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;

import java.util.ArrayList;

import model.Event;

public class EventRecyclerViewAdapter  extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventsViewHolder> {

    private ArrayList<Event> events;

    public EventRecyclerViewAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_purple_light, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;

        TextView eventType;
        TextView eventDescription;
        TextView eventLocation;
        TextView eventDistanceLocation;
        TextView eventAvailable;
        TextView eventDate;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.nameEvent);
            eventType = itemView.findViewById(R.id.typeEvent);
            eventDescription = itemView.findViewById(R.id.descriptionEvent);
            eventLocation = itemView.findViewById(R.id.locationEventShow);
            eventDistanceLocation = itemView.findViewById(R.id.doo);
            eventAvailable = itemView.findViewById(R.id.availableEvent);
            eventDate = itemView.findViewById(R.id.dateEvent);
        }

        public void bind(Event event) {
            eventName.setText(event.getName());
            eventType.setText("(" + event.getTypeEvent() + ")");
            eventDescription.setText(event.getDescription());
            eventLocation.setText(event.getLocationPlace());

            eventDistanceLocation.setText(Integer.toString(event.getMaxDistance()));
            eventAvailable.setText(event.isAvailble() ? "Da" : "Ne");
            eventDate.setText(event.getDateEvent().toString());
        }
    }
}