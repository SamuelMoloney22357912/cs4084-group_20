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
    private List<Integer> daysInMonth = new ArrayList<>();  // 🔥 moved here

    private CalendarDatabase calendarDatabase; // Instance of the CalendarDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.calender_activity);

        // Initialize the database
        calendarDatabase = new CalendarDatabase(this);

        initWidgets();

        selectedDate = LocalDate.now();
        monthYear = CalendarUtils.formattedMonthYear(selectedDate);
        monthYearTextView.setText(monthYear); // 🔥 You were missing this!

        updateCalendarDays();  // 🔥 this will initialize adapter
        updateEventsForSelectedDate();
    }

    private void onDayClicked(int position, String dayText) {
        int day = Integer.parseInt(dayText);
        selectedDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), day);

        calendarAdapter.setSelectedDate(selectedDate); // 🔥 update highlight!

        Toast.makeText(this, "Selected Date: " + day + " " + CalendarUtils.formattedMonthYear(selectedDate), Toast.LENGTH_LONG).show();

        updateEventsForSelectedDate();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columns for days of the week
        eventsContainer = findViewById(R.id.eventsContainer);
        globalEventsContainer = findViewById(R.id.globalEventsContainer);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        createEventButton = findViewById(R.id.createEventButton);

        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, EventEditActivity.class);
            intent.putExtra("SELECTED_DATE", selectedDate.toString());
            startActivity(intent);
        });

        findViewById(R.id.previousMonthButton).setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            monthYearTextView.setText(CalendarUtils.formattedMonthYear(selectedDate));
            updateCalendarDays();
            updateEventsForSelectedDate();
        });

        findViewById(R.id.nextMonthButton).setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            monthYearTextView.setText(CalendarUtils.formattedMonthYear(selectedDate));
            updateCalendarDays();
            updateEventsForSelectedDate();
        });
    }

    private void updateCalendarDays() {
        daysInMonth.clear();
        int firstDayOfMonth = selectedDate.withDayOfMonth(1).getDayOfWeek().getValue();
        int lengthOfMonth = selectedDate.lengthOfMonth();

        for (int i = 1; i < firstDayOfMonth; i++) {
            daysInMonth.add(0); // Add empty spaces for days before the first of the month
        }

        for (int day = 1; day <= lengthOfMonth; day++) {
            daysInMonth.add(day); // Add days of the month
        }

        calendarAdapter = new CalendarAdapter(daysInMonth, selectedDate, this::onDayClicked, calendarDatabase);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventsForSelectedDate();
    }

    public void updateEventsForSelectedDate() {
        // Use CalendarDatabase to get events for the selected date
        ArrayList<Event> eventsForSelectedDate = calendarDatabase.getEventsForDate(selectedDate);
        eventsContainer.removeAllViews();

        if (eventsForSelectedDate.isEmpty()) {
            TextView noEventsText = new TextView(this);
            noEventsText.setText("No events for this date");
            noEventsText.setTextSize(16);
            noEventsText.setPadding(16, 16, 16, 16);
            eventsContainer.addView(noEventsText);
        } else {
            for (Event event : eventsForSelectedDate) {
                // Create a container to hold the event details and delete button
                LinearLayout eventContainer = new LinearLayout(this);
                eventContainer.setOrientation(LinearLayout.HORIZONTAL);
                eventContainer.setPadding(16, 16, 16, 16);

                // Create a TextView to display the event details
                TextView eventView = new TextView(this);
                String eventDetails = "Event: " + event.getName() +
                        "\nPlace: " + event.getPlace() +
                        "\nTime: " + CalendarUtils.formattedTime(event.getTime()) +
                        "\nDifficulty: " + event.getDifficulty() +
                        "\nPerson: " + event.getPerson();
                eventView.setText(eventDetails);
                eventView.setBackgroundResource(R.drawable.event_background);
                eventView.setTextColor(Color.BLACK);
                eventView.setTextSize(14);

                // Create a delete button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setBackgroundColor(Color.RED);
                deleteButton.setTextColor(Color.WHITE);
                deleteButton.setPadding(16, 8, 16, 8);

                // Set up the delete button click listener to remove the event
                deleteButton.setOnClickListener(v -> {
                    // Delete the event from the database
                    calendarDatabase.deleteEvent(event);

                    // Refresh the events for the selected date
                    updateEventsForSelectedDate();
                });

                // Add the event view and delete button to the event container
                eventContainer.addView(eventView);
                eventContainer.addView(deleteButton);

                // Add the event container to the events container
                eventsContainer.addView(eventContainer);
            }
        }
        eventsContainer.requestLayout();
    }
}
