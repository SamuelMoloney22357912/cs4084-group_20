package com.example.helloworld;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView calendarRecyclerView;
    private ArrayList<String> daysOfMonth = new ArrayList<>();
    private CalendarAdapter.OnItemListener onItemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);

        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7); // 7 columns for each day of the week
        calendarRecyclerView.setLayoutManager(gridLayoutManager);

        LocalDate currentDate = LocalDate.now();
        int daysInMonth = currentDate.lengthOfMonth();
        int firstDayOfMonth = currentDate.withDayOfMonth(1).getDayOfWeek().getValue();

        for (int i = 1; i < firstDayOfMonth; i++) {
            daysOfMonth.add(""); // Empty space
        }

        for (int i = 1; i <= daysInMonth; i++) {
            daysOfMonth.add(String.valueOf(i));
        }

        onItemListener = new CalendarAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position, String dayText) {
            }
        };

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysOfMonth, onItemListener);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }
}
