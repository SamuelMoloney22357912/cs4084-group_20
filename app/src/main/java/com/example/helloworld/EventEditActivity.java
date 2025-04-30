package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET, eventPlaceET, eventDifficultyET, eventPersonET;
    private TextView eventDateTV;
    private TimePicker eventTimePicker;
    private LocalDate selectedDate;
    private CalendarDatabase calendarDatabase; // Added instance of CalendarDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        // Initialize the database
        calendarDatabase = new CalendarDatabase(this);

        String selectedDateString = getIntent().getStringExtra("SELECTED_DATE");
        selectedDate = LocalDate.parse(selectedDateString);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(selectedDate));
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventPlaceET = findViewById(R.id.eventPlaceET);
        eventDifficultyET = findViewById(R.id.eventDifficultyET);
        eventPersonET = findViewById(R.id.eventPersonET);
        eventTimePicker = findViewById(R.id.eventTimePicker);
    }

    public void saveEventAction(View view) {
        // Get event details from input fields
        String eventName = eventNameET.getText().toString();
        String eventPlace = eventPlaceET.getText().toString();
        String eventDifficulty = eventDifficultyET.getText().toString();
        String eventPerson = eventPersonET.getText().toString();

        // Get event time from TimePicker
        int hour = eventTimePicker.getHour();
        int minute = eventTimePicker.getMinute();
        LocalTime eventTime = LocalTime.of(hour, minute);

        // Create the event
        Event newEvent = new Event(eventName, eventPlace, selectedDate, eventTime, eventDifficulty, eventPerson);

        // Save the event to the database
        calendarDatabase.addEvent(newEvent);

        // Finish the activity and return to the previous screen
        finish();
    }
}
