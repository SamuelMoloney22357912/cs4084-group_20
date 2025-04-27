package com.example.helloworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// In your CalendarAdapter.java
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<Integer> daysInMonth;
    private OnDayClickListener onDayClickListener;

    // Modify the constructor to accept List<Integer>
    public CalendarAdapter(List<Integer> daysInMonth, OnDayClickListener onDayClickListener) {
        this.daysInMonth = daysInMonth;
        this.onDayClickListener = onDayClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer day = daysInMonth.get(position);
        if (day == 0) {
            holder.dayButton.setVisibility(View.INVISIBLE); // Hide empty spaces
        } else {
            holder.dayButton.setVisibility(View.VISIBLE);
            holder.dayButton.setText(String.valueOf(day));
            holder.dayButton.setOnClickListener(v -> onDayClickListener.onDayClick(position, String.valueOf(day)));
        }
    }


    @Override
    public int getItemCount() {
        return daysInMonth.size();
    }

    public interface OnDayClickListener {
        void onDayClick(int position, String dayText);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button dayButton;

        public ViewHolder(View itemView) {
            super(itemView);
            dayButton = itemView.findViewById(R.id.dayButton);
        }
    }
}
