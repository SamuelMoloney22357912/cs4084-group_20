package com.example.helloworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<Integer> daysList;
    private LocalDate selectedDate;
    private final OnDayClickListener onDayClickListener;

    public interface OnDayClickListener {
        void onDayClick(int position, String dayText);
    }

    public CalendarAdapter(List<Integer> daysList, LocalDate selectedDate, OnDayClickListener listener) {
        this.daysList = daysList;
        this.selectedDate = selectedDate;
        this.onDayClickListener = listener;
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
            boolean hasEvents = !Event.eventsForDate(currentDay).isEmpty();

            // Highlight the selected date with blue
            if (currentDay.equals(selectedDate)) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(holder.itemView.getContext(), android.R.color.holo_blue_dark));
            }
            // Highlight today's date with green, only if it's in the current month
            else if (currentDay.equals(LocalDate.now()) && currentDay.getMonth().equals(LocalDate.now().getMonth())
                    && currentDay.getYear() == LocalDate.now().getYear()) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(holder.itemView.getContext(), android.R.color.holo_green_dark));
            }
            // Highlight dates with events in red
            else if (hasEvents) {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(holder.itemView.getContext(), android.R.color.holo_red_dark));
            }
            // Default background for other days
            else {
                holder.dayButton.setBackgroundTintList(
                        ContextCompat.getColorStateList(holder.itemView.getContext(), android.R.color.holo_purple));
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
