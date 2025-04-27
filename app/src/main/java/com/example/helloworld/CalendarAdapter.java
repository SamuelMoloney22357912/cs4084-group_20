package com.example.helloworld;

import static com.example.helloworld.CalendarUtils.selectedDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<Integer> daysList;
    private OnDayClickListener onDayClickListener;

    // Constructor to accept days list and the listener
    public CalendarAdapter(List<Integer> daysList, OnDayClickListener onDayClickListener) {
        this.daysList = daysList;
        this.onDayClickListener = onDayClickListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // Get the day for this position in the list
        int day = daysList.get(position);

        if (day == 0) {
            // If it's an empty space (day == 0), hide the button
            holder.dayButton.setVisibility(View.INVISIBLE);
        } else {
            // Otherwise, show the day and set the button text
            holder.dayButton.setVisibility(View.VISIBLE);
            holder.dayButton.setText(String.valueOf(day));

            // Set an OnClickListener to handle day clicks
            holder.dayButton.setOnClickListener(v -> {
                // Pass the selected day back to the activity
                onDayClickListener.onDayClicked(day);
            });
        }
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    // ViewHolder for the calendar days
    public static class CalendarViewHolder extends RecyclerView.ViewHolder {

        Button dayButton;  // Button for each calendar day

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dayButton = itemView.findViewById(R.id.dayButton);  // Reference to the Button
        }
    }

    // Interface to handle day click events
    public interface OnDayClickListener {
        void onDayClicked(int day);
    }
}
