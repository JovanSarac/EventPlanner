package com.example.eventplanner.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.google.android.material.textfield.TextInputEditText;

public class TimeInputComponent extends LinearLayout {
    private TextView dayTextView;
    private TextInputEditText startTimeEditText;
    private TextInputEditText endTimeEditText;
    public TimeInputComponent(Context context) {
        super(context);
        init(context);
    }

    public TimeInputComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeInputComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.time_input_component, this);

        dayTextView = findViewById(R.id.dayTextView);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
    }

    public void setDayText(String text) {
        dayTextView.setText(text);
    }

    public String getDayText() {
        return dayTextView.getText().toString();
    }
    public String getStartTime() {
        return startTimeEditText.getText().toString();
    }

    public String getEndTime() {
        return endTimeEditText.getText().toString();
    }
}
