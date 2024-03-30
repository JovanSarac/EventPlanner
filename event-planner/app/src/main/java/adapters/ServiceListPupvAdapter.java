package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.EditProductActivity;
import com.example.eventplanner.EditServiceActivity;
import com.example.eventplanner.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.Product;
import model.Service;

public class ServiceListPupvAdapter extends ArrayAdapter<Service> {
    private ArrayList<Service> services;

    public ServiceListPupvAdapter(Context context, ArrayList<Service> services){
        super(context, R.layout.service_card_pupv, services);
        this.services = services;
    }

    @Override
    public int getCount() {
        return this.services.size();
    }

    @Nullable
    @Override
    public Service getItem(int position) {
        return this.services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card_pupv, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView name = convertView.findViewById(R.id.name);
        TextView description = convertView.findViewById(R.id.description);
        TextView fullPrice = convertView.findViewById(R.id.price);
        TextView pricePerHour = convertView.findViewById(R.id.pricePerHour);

        if(service != null){
            image.setImageResource(service.getImageId());
            name.setText(service.getName());
            description.setText(service.getDescription());
            fullPrice.setText(service.getFullPrice().toString() + "$");
            pricePerHour.setText(service.getPricePerHour().toString() + "$/h");
        }

        Button editButton = convertView.findViewById(R.id.edit);
        Button deleteButton = convertView.findViewById(R.id.delete);

        final View finalConvertView = convertView;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), EditServiceActivity.class);
                intent.putExtra("Id", service.getId());
                intent.putExtra("Name", service.getName());
                intent.putExtra("Category", service.getCategory());
                intent.putExtra("Subcategory", service.getSubcategory());
                intent.putExtra("Description", service.getDescription());
                intent.putExtra("Specific", service.getSpecific());
                intent.putExtra("PricePerHour", service.getPricePerHour());
                intent.putExtra("FullPrice", service.getFullPrice());
                intent.putExtra("Discount", service.getDiscount());
                intent.putExtra("Duration", service.getDuration());
                intent.putExtra("Location", service.getLocation());
                intent.putExtra("Image", service.getImageId());
                intent.putExtra("Events", service.getEvents());
                intent.putExtra("Providers", service.getProviders());
                intent.putExtra("ReservationDue", service.getReservationDue());
                intent.putExtra("CancelationDue", service.getCancelationDue());
                intent.putExtra("AutomaticAffirmation", service.getAutomaticAffirmation());
                intent.putExtra("Availability", service.getAvailable());
                intent.putExtra("Visibility", service.getVisible());

                finalConvertView.getContext().startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.confirmation_popup, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });


        return convertView;
    }
}
