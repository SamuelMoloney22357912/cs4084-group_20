package com.example.helloworld;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView calendarRecyclerView;
    private CalendarAdapter calendarAdapter;
    private LocalDate selectedDate;
    private LinearLayout eventsContainer;
    private TextView monthYearTextView;
    private String monthYear;
    private LinearLayout globalEventsContainer;
    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);
        getSupportActionBar().hide();


        initWidgets();
        selectedDate = LocalDate.now();
        updateCalendarDays(); // Set default to the current date
        monthYear = CalendarUtils.formattedMonthYear(selectedDate);
        updateCalendarDays();  // Update the calendar days
        updateEventsForSelectedDate();
        calendarAdapter = new CalendarAdapter(daysInMonth, this::onDayClicked);
        calendarRecyclerView.setAdapter(calendarAdapter);// Display events for the current date
    }
    private void onDayClicked(int day) {
        selectedDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), day);
        String message = "Selected Date: " + day + " " + CalendarUtils.formattedMonthYear(selectedDate);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Update the events for the selected date
        updateEventsForSelectedDate();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columns for days of the week
        eventsContainer = findViewById(R.id.eventsContainer);  // Initialize eventsContainer
        globalEventsContainer = findViewById(R.id.globalEventsContainer);  // Initialize global events container
        monthYearTextView = findViewById(R.id.monthYearTextView);
        createEventButton = findViewById(R.id.createEventButton); // Initialize the create event button

        // Set click listener for the Create Event button
        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, EventEditActivity.class);
            intent.putExtra("SELECTED_DATE", selectedDate.toString()); // Pass the selected date to the event edit screen
            startActivity(intent);
        });

        // Add month navigation button functionality
        findViewById(R.id.previousMonthButton).setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            monthYearTextView.setText(CalendarUtils.formattedMonthYear(selectedDate)); // Update month/year
            updateCalendarDays(); // Update the days for the previous month
            updateEventsForSelectedDate(); // Update events for the new selected date
        });

        findViewById(R.id.nextMonthButton).setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            monthYearTextView.setText(CalendarUtils.formattedMonthYear(selectedDate)); // Update month/year
            updateCalendarDays(); // Update the days for the next month
            updateEventsForSelectedDate(); // Update events for the new selected date
        });
    }

    private List<Integer> daysInMonth = new ArrayList<>();  // Declare this at the class level

    private void updateCalendarDays() {
        daysInMonth.clear();  // Clear the list before adding new days to prevent duplication
        int firstDayOfMonth = selectedDate.withDayOfMonth(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int lengthOfMonth = selectedDate.lengthOfMonth(); // Days in the current month

        // Add empty spaces before the first day of the month
        for (int i = 1; i < firstDayOfMonth; i++) {
            daysInMonth.add(0);  // Adding empty space
        }

        // Add the actual days of the month
        for (int day = 1; day <= lengthOfMonth; day++) {
            daysInMonth.add(day);
        }

        // Now, update the RecyclerView with the new daysInMonth list
        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(daysInMonth, this::onDayClicked);
            calendarRecyclerView.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.notifyDataSetChanged(); // Only call this once after updating the list
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the events for the selected date
        updateEventsForSelectedDate();
    }

    public void updateEventsForSelectedDate() {
        // Get events for the selected date
        ArrayList<Event> eventsForSelectedDate = Event.eventsForDate(selectedDate);

        // Clear existing views (events)
        eventsContainer.removeAllViews();

        // Add new events
        if (eventsForSelectedDate.isEmpty()) {
            // Show a default message when no events exist for the selected date
            TextView noEventsText = new TextView(this);
            noEventsText.setText("No events for this date");
            noEventsText.setTextSize(16);
            noEventsText.setPadding(16, 16, 16, 16);
            eventsContainer.addView(noEventsText);
        } else {
            // Loop through the list of events for the selected date
            for (Event event : eventsForSelectedDate) {
                // Create a new TextView for each event
                TextView eventView = new TextView(this);
                String eventDetails = "Event: " + event.getName() +
                        "\nPlace: " + event.getPlace() +
                        "\nTime: " + CalendarUtils.formattedTime(event.getTime()) +
                        "\nDifficulty: " + event.getDifficulty() +
                        "\nPerson: " + event.getPerson();
                eventView.setText(eventDetails);
                eventView.setBackgroundResource(R.drawable.event_background);
                eventView.setPadding(16, 16, 16, 16);
                eventView.setTextColor(Color.BLACK);
                eventView.setTextSize(14);

                // Set layout parameters to provide spacing between event views
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                eventView.setLayoutParams(params);

                // Add the event view to the events container
                eventsContainer.addView(eventView);
            }
        }

        // Optionally refresh the layout after adding new views
        eventsContainer.requestLayout();
    }

}
