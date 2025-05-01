package com.example.helloworld;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<Integer> daysList;
    private LocalDate selectedDate;
    private final OnDayClickListener onDayClickListener;
    private final CalendarDatabase calendarDatabase; // Add a reference to CalendarDatabase

    public interface OnDayClickListener {
        void onDayClick(int position, String dayText);



    }

    public CalendarAdapter(List<Integer> daysList, LocalDate selectedDate, OnDayClickListener listener, CalendarDatabase database) {
        this.daysList = daysList;
        this.selectedDate = selectedDate;
        this.onDayClickListener = listener;
        this.calendarDatabase = database; // Initialize the database instance
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        notifyDataSetChanged(); // 🔥 Refresh to highlight selected day
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        Integer day = daysList.get(position);

        if (day != null && day != 0) {
            holder.dayButton.setText(String.valueOf(day));
            holder.dayButton.setVisibility(View.VISIBLE);

            LocalDate currentDay = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), day);
            boolean hasEvents = !calendarDatabase.getEventsForDate(currentDay).isEmpty(); // Query the database

            // Get the context for accessing color resources
            Context context = holder.itemView.getContext();

            // Highlight the selected date with blue
            if (currentDay.equals(selectedDate)) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.selected_day_background));
            }
            // Highlight today's date with light blue
            else if (currentDay.equals(LocalDate.now()) && currentDay.getMonth().equals(LocalDate.now().getMonth())
                    && currentDay.getYear() == LocalDate.now().getYear()) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.today_background));
            }
            // Highlight dates with events with very light blue
            else if (hasEvents) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.event_background));
            }
            // Default background for other days with white and blue border
            else {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.default_day_background));
                holder.dayButton.setTextColor(ContextCompat.getColor(context, R.color.default_day_border)); // Blue border for text color
            }

            holder.dayButton.setOnClickListener(v -> {
                onDayClickListener.onDayClick(position, String.valueOf(day));
            });

        } else {
            holder.dayButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        Button dayButton;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dayButton = itemView.findViewById(R.id.dayButton);
        }
    }
}
