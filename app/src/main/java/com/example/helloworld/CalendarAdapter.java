package com.example.helloworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private final String currentDate;  // Store today's date for comparison

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = new ArrayList<>(daysOfMonth);
        this.onItemListener = onItemListener;
        // Get today's date in the same format as the day text (e.g., "15")
        this.currentDate = String.valueOf(LocalDate.now().getDayOfMonth());
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        if (!dayText.isEmpty()) {
            holder.dayOfMonth.setText(dayText);

            if (dayText.equals(currentDate)) {
                int highlightColor = ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_purple);
                holder.dayOfMonth.setBackgroundColor(highlightColor);
                int highlightNumColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.white);
                holder.dayOfMonth.setTextColor(highlightNumColor);
            } else {
                holder.dayOfMonth.setBackgroundResource(0);
            }

            holder.dayOfMonth.setVisibility(View.VISIBLE);
        } else {
            holder.dayOfMonth.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void updateData(ArrayList<String> newDays) {
        this.daysOfMonth = new ArrayList<>(newDays);
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView dayOfMonth;
        private final OnItemListener onItemListener;

        public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String dayText = dayOfMonth.getText().toString();
            if (!dayText.isEmpty()) {
                onItemListener.onItemClick(getAdapterPosition(), dayText);
            }
        }
    }
}
