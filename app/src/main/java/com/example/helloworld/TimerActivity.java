package com.example.helloworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button startStopButton;
    private Button resetButton;

    private Timer timer;
    private TimerTask timerTask;
    private double time = 0.0;
    private boolean timerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerTextView = findViewById(R.id.timerTextView);
        startStopButton = findViewById(R.id.startStopButton);
        resetButton = findViewById(R.id.resetButton);

        timer = new Timer();

        // Set up button listeners
        startStopButton.setOnClickListener(this::startStopTapped);
        resetButton.setOnClickListener(this::resetTapped);
    }

    public void resetTapped(View view) {
        if (time > 0) {  // Only reset if time is greater than zero
            AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
            resetAlert.setTitle("Reset Timer");
            resetAlert.setMessage("Are you sure you want to reset the timer?");
            resetAlert.setPositiveButton("Reset", (dialogInterface, i) -> {
                if (timerTask != null) {
                    timerTask.cancel();
                    setButtonUI("Start", R.color.black);
                    time = 0.0;
                    timerStarted = false;
                    timerTextView.setText(formatTime(0, 0, 0));
                }
            });

            resetAlert.setNeutralButton("Cancel", (dialogInterface, i) -> {
                // Do nothing
            });

            resetAlert.show();
        }
    }

    public void startStopTapped(View view) {
        if (!timerStarted) {
            timerStarted = true;
            setButtonUI("Pause", R.color.blue);
            startTimer();
        } else {
            timerStarted = false;
            setButtonUI("Start", R.color.black);

            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    private void setButtonUI(String text, int color) {
        startStopButton.setText(text);
        startStopButton.setTextColor(ContextCompat.getColor(this, color));
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    time++;
                    timerTextView.setText(getTimerText());
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = (rounded % 86400) / 3600;

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}
