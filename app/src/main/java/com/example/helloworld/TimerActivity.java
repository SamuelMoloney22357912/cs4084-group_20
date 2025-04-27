package com.example.helloworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView;
    private EditText minutesInput;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button backButton;

    private Timer timer;
    private TimerTask timerTask;
    private double time = 0.0;
    private boolean timerStarted = false;
    private final Handler handler = new Handler();
    private long stopButtonHoldTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

       // timerTextView = findViewById(R.id.timerTextView);
        //minutesInput = findViewById(R.id.minutesInput);
        //startButton = findViewById(R.id.startButton);
       // stopButton = findViewById(R.id.stopButton);
        //resetButton = findViewById(R.id.resetButton);
        //backButton = findViewById(R.id.backButton);

        timer = new Timer();

        startButton.setOnClickListener(this::startTapped);
        resetButton.setOnClickListener(v -> resetTimer());
        backButton.setOnClickListener(v -> finish());

        stopButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopButtonHoldTime = System.currentTimeMillis();
                    handler.postDelayed(this::showStopConfirmation, 5000);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacksAndMessages(null);
                    break;
            }
            return true;
        });


        resetButton.setEnabled(false);
        backButton.setEnabled(true);
    }

    private void showStopConfirmation() {
        long heldTime = System.currentTimeMillis() - stopButtonHoldTime;
        if (heldTime >= 5000) {
            AlertDialog.Builder stopAlert = new AlertDialog.Builder(this);
            stopAlert.setTitle("Stop Timer");
            stopAlert.setMessage("Are you sure you want to stop the timer?");
            stopAlert.setPositiveButton("Yes", (dialogInterface, i) -> stopTimer());
            stopAlert.setNegativeButton("No", (dialogInterface, i) -> {});
            stopAlert.show();
        }
    }

    public void startTapped(View view) {
        if (!timerStarted) {
            String minutesStr = minutesInput.getText().toString();
            if (!minutesStr.isEmpty()) {
                int minutes = Integer.parseInt(minutesStr);
                time = minutes * 60;
                startTimer();
            }
        }
    }

    private void startTimer() {


        timerStarted = true;
        resetButton.setEnabled(false);
        backButton.setEnabled(false);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (time > 0) {
                        time--;
                        timerTextView.setText(getTimerText());
                    } else {
                        timerTask.cancel();
                        timerStarted = false;
                        timerTextView.setText("Time Finished. Congratulations!");


                        timerTextView.setText("Time Finished. Congratulations!");

                        timerTextView.setTextColor(ContextCompat.getColor(TimerActivity.this, android.R.color.holo_red_dark));
                        resetButton.setEnabled(true);
                        backButton.setEnabled(true);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = rounded % 60;
        int minutes = (rounded / 60) % 60;
        int hours = (rounded / 3600);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerStarted = false;
            timerTextView.setText("00:00:00");
            timerTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            backButton.setEnabled(true);
        }
    }


    private void resetTimer() {
        timerStarted = false;
        time = 0;
        timerTextView.setText("00:00:00");
        timerTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        resetButton.setEnabled(false);
    }






}
