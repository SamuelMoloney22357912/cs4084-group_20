package com.example.helloworld;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
    private LinearLayout globalEventsContainer;  // Container for global events
    private LinearLayout allSavedEventsContainer; // Container for all saved events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);

        initWidgets();
        selectedDate = LocalDate.now(); // Set default to the current date
        monthYear = CalendarUtils.formattedMonthYear(selectedDate);
        updateCalendarDays();  // Update the calendar days
        updateEventsForSelectedDate();  // Display events for the current date
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columns for days of the week
        eventsContainer = findViewById(R.id.eventsContainer);  // Initialize eventsContainer
        globalEventsContainer = findViewById(R.id.globalEventsContainer);  // Initialize global events container
        allSavedEventsContainer = findViewById(R.id.allSavedEventsContainer); // Initialize all saved events container
        monthYearTextView = findViewById(R.id.monthYearTextView);

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

    private void updateCalendarDays() {
        int firstDayOfMonth = selectedDate.withDayOfMonth(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int lengthOfMonth = selectedDate.lengthOfMonth(); // Days in the current month

        // Create a list of the days to pass to the RecyclerView adapter
        List<Integer> daysInMonth = new ArrayList<>();

        // Add empty spaces before the first day of the month
        for (int i = 1; i < firstDayOfMonth; i++) {
            daysInMonth.add(0); // Adding empty space
        }

        // Add the actual days of the month
        for (int day = 1; day <= lengthOfMonth; day++) {
            daysInMonth.add(day);
        }

        // Pass this list to your RecyclerView Adapter
        calendarAdapter = new CalendarAdapter(daysInMonth, this::onDayClicked);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
    }

    private void onDayClicked(int position, String dayText) {
        if (!dayText.isEmpty()) {
            selectedDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), Integer.parseInt(dayText));
            String message = "Selected Date: " + dayText + " " + CalendarUtils.formattedMonthYear(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // Open the EventEditActivity to add an event
            Intent intent = new Intent(CalendarActivity.this, EventEditActivity.class);
            intent.putExtra("SELECTED_DATE", selectedDate.toString()); // Pass the selected date
            startActivity(intent);

            updateEventsForSelectedDate(); // Update events for the selected date
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reference to the container for events of the selected date
        eventsContainer = findViewById(R.id.eventsContainer);
        globalEventsContainer = findViewById(R.id.globalEventsContainer);
        allSavedEventsContainer = findViewById(R.id.allSavedEventsContainer);

        updateEventsForSelectedDate();
    }

    // Update events for the selected date
    public void updateEventsForSelectedDate() {
        // Get events for the selected date (Replace this with your event fetching mechanism)
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
                TextView eventView = new TextView(this);
                eventView.setText(event.getName() + " - " + CalendarUtils.formattedTime(event.getTime()));
                eventView.setBackgroundResource(R.drawable.event_background);
                eventView.setPadding(16, 16, 16, 16);
                eventView.setTextColor(Color.BLACK);
                eventView.setTextSize(16);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                eventView.setLayoutParams(params);

                eventsContainer.addView(eventView);
            }
        }

        // Handle visibility for global events (optional)
        if (globalEventsContainer.getChildCount() == 0) {
            globalEventsContainer.setVisibility(View.GONE);
        } else {
            globalEventsContainer.setVisibility(View.VISIBLE);
        }

        // Call to display events for all saved events in this month (optional)
        updateSavedEventsForSelectedMonth();
    }

    private void updateSavedEventsForSelectedMonth() {
        // Assuming you have a method that retrieves all events for a given month
        List<Event> allEventsForMonth = Event.getAllEventsForMonth(selectedDate.getYear(), selectedDate.getMonthValue());

        // Clear existing views (events)
        allSavedEventsContainer.removeAllViews();

        if (allEventsForMonth.isEmpty()) {
            // If there are no saved events, show a message
            TextView noSavedEventsText = new TextView(this);
            noSavedEventsText.setText("No saved events for this month");
            noSavedEventsText.setTextSize(16);
            noSavedEventsText.setPadding(16, 16, 16, 16);
            allSavedEventsContainer.addView(noSavedEventsText);
        } else {
            // Loop through the list of saved events for the selected month
            for (Event event : allEventsForMonth) {
                TextView eventView = new TextView(this);
                eventView.setText(event.getName() + " - " + CalendarUtils.formattedTime(event.getTime()));
                eventView.setBackgroundResource(R.drawable.event_background);
                eventView.setPadding(16, 16, 16, 16);
                eventView.setTextColor(Color.BLACK);
                eventView.setTextSize(16);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                eventView.setLayoutParams(params);

                // Add the event view to the allSavedEventsContainer
                allSavedEventsContainer.addView(eventView);
            }
        }

        // Optionally refresh the layout after adding new views
        allSavedEventsContainer.requestLayout();
    }

}
