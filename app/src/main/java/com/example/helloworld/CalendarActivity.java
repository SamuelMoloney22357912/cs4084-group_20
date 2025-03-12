package com.example.helloworld;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    private TextView monthYearTextView;
    private RecyclerView recyclerViewCalendar;
    private CalendarAdapter calendarAdapter;
    private LocalDate selectedDate;
    private ArrayList<String> daysOfMonth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);

        monthYearTextView = findViewById(R.id.monthYearTextView);
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        Button previousMonthButton = findViewById(R.id.previousMonthButton);
        Button nextMonthButton = findViewById(R.id.nextMonthButton);

        selectedDate = LocalDate.now();
        updateCalendar();

        previousMonthButton.setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            updateCalendar();
        });

        nextMonthButton.setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            updateCalendar();
        });
    }

    private void updateCalendar() {
        String monthYear = monthYearFromDate(selectedDate);
        monthYearTextView.setText(monthYear);

        daysOfMonth = daysInMonthArray(selectedDate);

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(daysOfMonth, (position, dayText) -> {
                if (!dayText.isEmpty()) {
                    String message = "Selected Date: " + dayText + " " + monthYear;
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            });
            recyclerViewCalendar.setLayoutManager(new GridLayoutManager(this, 7));
            recyclerViewCalendar.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.updateData(daysOfMonth);
        }
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // Monday = 1, Sunday = 7

        dayOfWeek = (dayOfWeek % 7);  // Convert Monday-based index to Sunday-based (0-6)

        for (int i = 0; i < 42; i++) { // 6 rows of 7 days each
            if (i < dayOfWeek || i >= daysInMonth + dayOfWeek) {
                daysInMonthArray.add(""); // Empty slots
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek + 1));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }
}
