package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.PupvUserCardAdapter;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.DateSchedule;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventPUPZ;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.UserPUPV;
import com.example.eventplanner.model.UserPUPZ;
import com.example.eventplanner.utils.WorkingHours;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ReserveServiceActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> pupz_list_names=new ArrayList<>();
    List<UserPUPZ> pupz_list=new ArrayList<>();
    Service service;
    List<Event> events=new ArrayList<>();
    List<String> event_names=new ArrayList<>();
    UserPUPZ selectedPupz;
    DateSchedule dateScheule;
    Event selectedEvent;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    HashMap<String, List<EventPUPZ>> busyDates=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserve_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getService();
        getPupz();

        TextInputEditText from=findViewById(R.id.fromTime);
        TextInputEditText to=findViewById(R.id.toTime);
        from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && from.getText()!=null && !from.getText().equals("")) {
                    String timeString=from.getText().toString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime time = LocalTime.parse(timeString, formatter);
                    //time.plusMinutes((int)(service.getDuration()*60));
                    time=time.plusMinutes((int)(1.6*60));
                    to.setText(time.toString());
                }
            }
        });

        findViewById(R.id.reserveService).setOnClickListener(v->{

        });

    }

    void getPupz(){
        db.collection("User")
                .whereEqualTo("userType", "PUPZ")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                pupz_list_names.add(doc.getString("firstName") + " " + doc.getString("lastName"));

                                UserPUPZ user = doc.toObject(UserPUPZ.class);
                                pupz_list.add(user);
                            }
                            TextInputLayout textInputLayout = findViewById(R.id.pupz_pick);
                            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
                            ArrayAdapter<String> pupzAdapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_dropdown_item_1line, pupz_list_names);
                            autoCompleteTextView.setAdapter(pupzAdapter);

                            autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                                selectedPupz = pupz_list.get(position) ;
                                getDateSchedules();
                            });
                        } else {
                            Toast.makeText(ReserveServiceActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    void getService(){
        db.collection("Services")
                .document("-1760065405153320460")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Service yourObject = documentSnapshot.toObject(Service.class);
                        if (yourObject != null) {
                            service=yourObject;
                            getEvents();
                        }
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error fetching document", e);
                });
    }
    void getEvents(){
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (Long id : service.getEventIds()) {
            tasks.add(db.collection("Events").document(id.toString()).get());
        }
        Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks.addOnCompleteListener(new OnCompleteListener<List<DocumentSnapshot>>() {
            @Override
            public void onComplete(Task<List<DocumentSnapshot>> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> snapshots = task.getResult();

                    for (DocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            Event yourObject = new Event(
                                    Long.parseLong(doc.getId()),
                                    doc.getString("userOdId"),
                                    doc.getString("typeEvent"),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getLong("maxPeople").intValue(),
                                    doc.getString("locationPlace"),
                                    doc.getLong("maxDistance").intValue(),
                                    doc.getDate("dateEvent"),
                                    doc.getBoolean("available")
                            );
                            events.add(yourObject);
                            event_names.add(yourObject.getName());
                        }
                    }
                    TextInputLayout textInputLayout = findViewById(R.id.event_pick);
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
                    ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(ReserveServiceActivity.this, android.R.layout.simple_dropdown_item_1line, event_names);
                    autoCompleteTextView.setAdapter(eventAdapter);

                    autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                        selectedEvent = events.get(position);
                    });
                }
            }
        });
    }

    void getDateSchedules(){
        db.collection("DateSchedule")
                .whereEqualTo("workerId",selectedPupz.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dateScheule=null;
                            for (DocumentSnapshot doc : task.getResult()) {
                                DateSchedule schedule= doc.toObject(DateSchedule.class);
                                try {
                                    Date startDate=dateFormat.parse(schedule.getDateRange().getStartDate());
                                    Date endDate=dateFormat.parse(schedule.getDateRange().getEndDate());
                                    Date date=new Date(selectedEvent.getDateEvent().getTime());
                                    if (date.after(startDate) && date.before(endDate)) {
                                        dateScheule=schedule;
                                        getBusyDates();
                                        break;
                                    } else {
                                        System.out.println("The date is outside the range.");
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        } else {
                            Toast.makeText(ReserveServiceActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void getBusyDates(){
        db.collection("Event")
                .whereEqualTo("workerId",selectedPupz.getId())
                .whereEqualTo("dateScheduleId",dateScheule.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                EventPUPZ yourObject = doc.toObject(EventPUPZ.class);
                                yourObject.setStartHours(yourObject.getStartHours().split(" ")[0]);
                                yourObject.setEndHours(yourObject.getEndHours().split(" ")[0]);
                                if(busyDates.get(yourObject.getDay())==null){
                                    List<EventPUPZ> list=new ArrayList<>();
                                    list.add(yourObject);
                                    busyDates.put(yourObject.getDay(),list);
                                }else{
                                    busyDates.get(yourObject.getDay()).add(yourObject);
                                }
                            }
                            displayTable();

                        } else {
                            Toast.makeText(ReserveServiceActivity.this, "Getting data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void displayTable(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = sdf.format(selectedEvent.getDateEvent());
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        dayOfWeek="Friday";

        Collections.sort(busyDates.get(dayOfWeek.toUpperCase()), new Comparator<EventPUPZ>() {
            @Override
            public int compare(EventPUPZ o1, EventPUPZ o2) {
                return o1.getStartHours().compareTo(o2.getStartHours());
            }
        });

        for (EventPUPZ day:busyDates.get(dayOfWeek.toUpperCase())) {
            TableRow newRow = new TableRow(this);
            newRow.setGravity(Gravity.CENTER);

            TextView textView1 = new TextView(this);
            textView1.setText(day.getStartHours());
            textView1.setGravity(Gravity.CENTER);

            TextView textView2 = new TextView(this);
            textView2.setText(day.getEndHours());
            textView2.setGravity(Gravity.CENTER);

            TextView textView3 = new TextView(this);
            textView3.setText(day.getType());
            textView3.setGravity(Gravity.CENTER);

            newRow.addView(textView1);
            newRow.addView(textView2);
            newRow.addView(textView3);

            tableLayout.addView(newRow);
        };
    }
    void createReservation(){

    }
}