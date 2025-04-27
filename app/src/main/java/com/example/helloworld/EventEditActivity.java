package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.LocalDate;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        String selectedDateString = getIntent().getStringExtra("SELECTED_DATE");
        selectedDate = LocalDate.parse(selectedDateString); // Parse the date
        time = LocalTime.now();

        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();

        Event newEvent = new Event(eventName, selectedDate, time);
        Event.eventsList.add(newEvent);

        Toast.makeText(this, "Event Saved!", Toast.LENGTH_SHORT).show();
        finish(); // close this activity
    }

}
