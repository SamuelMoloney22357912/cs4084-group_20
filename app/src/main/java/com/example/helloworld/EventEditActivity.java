package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.LocalDate;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET, eventPlaceET, eventDifficultyET, eventPersonET;
    private TextView eventDateTV;
    private TimePicker eventTimePicker; // Declare the TimePicker variable

    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        String selectedDateString = getIntent().getStringExtra("SELECTED_DATE");
        selectedDate = LocalDate.parse(selectedDateString); // Parse the date

        // Display selected date in TextView
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(selectedDate));
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventPlaceET = findViewById(R.id.eventPlaceET);
        eventDifficultyET = findViewById(R.id.eventDifficultyET);
        eventPersonET = findViewById(R.id.eventPersonET);
        eventTimePicker = findViewById(R.id.eventTimePicker); // Initialize the TimePicker
    }

    public void saveEventAction(View view) {
        // Get the data entered by the user
        String eventName = eventNameET.getText().toString();
        String eventPlace = eventPlaceET.getText().toString();
        String eventDifficulty = eventDifficultyET.getText().toString();
        String eventPerson = eventPersonET.getText().toString();

        // Get the time from the TimePicker
        int hour = eventTimePicker.getHour();
        int minute = eventTimePicker.getMinute();
        LocalTime eventTime = LocalTime.of(hour, minute); // Create a LocalTime object

        // Create a new Event object with the user input
        Event newEvent = new Event(eventName, eventPlace, selectedDate, eventTime, eventDifficulty, eventPerson);
        Event.eventsList.add(newEvent);

        // Optionally, navigate back or update the UI
        finish(); // Close the activity after saving the event
    }
}
