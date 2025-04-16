package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView dateTextView, timeTextView;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        dateTextView = findViewById(R.id.xml_text_date);
        timeTextView = findViewById(R.id.xml_text_time);

        // Set initial date and time
        updateDateTime();

        // Start updating time every second
        handler.post(timeUpdater);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button calendarButton = findViewById(R.id.calendarButton);
        Button toDoButton = findViewById(R.id.toDoPageButton);
        Button timerButton = findViewById(R.id.timerPageButton);
        Button timetableButton = findViewById(R.id.timetablePageButton);

        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        toDoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ToDoActivity.class);
            startActivity(intent);
        });

        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
        });

        timetableButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, timetableActivity.class);
            startActivity(intent);
        });
    }
    private void updateDateTime() {
        Calendar calendar = Calendar.getInstance();

        // Format Date and Time
        String dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(calendar.getTime());
        String timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(calendar.getTime());

        // Update TextViews
        dateTextView.setText(dateFormat);
        timeTextView.setText(timeFormat);
    }

    // Runnable to update time every second
    private final Runnable timeUpdater = new Runnable() {
        @Override
        public void run() {
            updateDateTime();
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeUpdater); // Stop updates when activity is destroyed
    }
}