package com.example.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class timetableActivity extends AppCompatActivity {
    //private LocalDate showedDate;
    private ArrayList<timetableTask> tasks;
    private final DateTimeFormatter mainDate = DateTimeFormatter.ofPattern("EEEE dd/MM");
    private Database database;
    private TextView date;
    private ListAdapter listAdapter;

    private DayOfWeek currentDay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        date = findViewById(R.id.date);
        ImageView left = findViewById(R.id.left);
        ImageView right = findViewById(R.id.right);
        ListView listview = findViewById(R.id.listview);
        LinearLayout add = findViewById(R.id.add);

        tasks = new ArrayList<>();
        listAdapter = new ListAdapter();
        listview.setAdapter(listAdapter);
        database = new Database(this);

        //showedDate = LocalDate.now();
        currentDay = LocalDate.now().getDayOfWeek();
        if (currentDay == DayOfWeek.SUNDAY) currentDay = DayOfWeek.MONDAY;
        if (currentDay == DayOfWeek.SATURDAY) currentDay = DayOfWeek.FRIDAY;


        RefreshData();

        /*
        date.setOnClickListener(v-> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                showedDate = LocalDate.of(year, month+1, day);
                RefreshData();
            }, showedDate.getYear(), showedDate.getMonthValue()-1, showedDate.getDayOfMonth());
            datePickerDialog.show();
        });

         */

        left.setOnClickListener(v-> {
            //showedDate = showedDate.minusDays(1);
            //RefreshData();
            currentDay = currentDay.minus(1);
            if (currentDay == DayOfWeek.SUNDAY) currentDay = DayOfWeek.FRIDAY;
            if (currentDay == DayOfWeek.SATURDAY) currentDay = DayOfWeek.FRIDAY;
            RefreshData();
        });

        right.setOnClickListener(v-> {
            //showedDate = showedDate.plusDays(1);
            //RefreshData();
            currentDay = currentDay.plus(1);
            if (currentDay == DayOfWeek.SATURDAY) currentDay = DayOfWeek.MONDAY;
            if (currentDay == DayOfWeek.SUNDAY) currentDay = DayOfWeek.MONDAY;
            RefreshData();
        });

        add.setOnClickListener(v-> {
            Intent intent = new Intent(timetableActivity.this, timetableTaskEdit.class);
            //intent.putExtra("Date", showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            intent.putExtra("Date", currentDay.toString());
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshData();
    }

    private void RefreshData() {
        //date.setText(showedDate.format(mainDate));
        date.setText(currentDay.toString().substring(0, 1) + currentDay.toString().substring(1).toLowerCase());

        //ArrayList<timetableTask> ts = database.getAllTasks(showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ArrayList<timetableTask> ts = database.getAllTasks(currentDay.toString());
        Collections.sort(ts);
        tasks = ts;
        listAdapter.notifyDataSetChanged();
    }

    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {

        }

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public timetableTask getItem(int i) {
            return tasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.timetable_task, null);

            TextView from = v.findViewById(R.id.from);
            TextView to = v.findViewById(R.id.to);
            TextView task = v.findViewById(R.id.task);

            timetableTask t = tasks.get(i);

            from.setText(t.getFromToString());
            to.setText(t.getToToString());
            task.setText(t.getTask());

            GradientDrawable backDrawable = (GradientDrawable) task.getBackground();
            backDrawable.setColor(t.getColorID(timetableActivity.this));

            task.setOnClickListener(v2-> {
                Intent intent = new Intent(timetableActivity.this, timetableTaskEdit.class);
                intent.putExtra("ID", t.getID());
                intent.putExtra("Task", t.getTask());
                intent.putExtra("From", t.getFromToString());
                intent.putExtra("To", t.getToToString());
                intent.putExtra("Color", t.getColor());
                intent.putExtra("Date", currentDay.toString());
                startActivity(intent);
                //return true;
            });

            return v;
        }
    }
}


