package com.example.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CalendarDatabase extends SQLiteOpenHelper {

    public CalendarDatabase(Context context) {
        super(context, "CalendarDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "place TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "difficulty TEXT, " +
                "person TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    // Add an event to the database
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", event.getName());
        values.put("place", event.getPlace());
        values.put("date", event.getDate().toString());
        values.put("time", event.getTime().toString());
        values.put("difficulty", event.getDifficulty());
        values.put("person", event.getPerson());

        db.insert("events", null, values);
        db.close();
    }

    // Delete an event from the database
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("events", "name = ? AND place = ? AND date = ? AND time = ?", new String[]{
                event.getName(),
                event.getPlace(),
                event.getDate().toString(),
                event.getTime().toString()
        });
        db.close();
    }

    // Retrieve events for a specific date
    public ArrayList<Event> getEventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("events", null, "date = ?", new String[]{date.toString()}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Ensure the column index is valid before accessing the data
                int nameIndex = cursor.getColumnIndex("name");
                int placeIndex = cursor.getColumnIndex("place");
                int dateIndex = cursor.getColumnIndex("date");
                int timeIndex = cursor.getColumnIndex("time");
                int difficultyIndex = cursor.getColumnIndex("difficulty");
                int personIndex = cursor.getColumnIndex("person");

                // Check that all indexes are >= 0 before accessing values
                if (nameIndex >= 0 && placeIndex >= 0 && dateIndex >= 0 && timeIndex >= 0 &&
                        difficultyIndex >= 0 && personIndex >= 0) {
                    String name = cursor.getString(nameIndex);
                    String place = cursor.getString(placeIndex);
                    LocalDate eventDate = LocalDate.parse(cursor.getString(dateIndex));
                    LocalTime time = LocalTime.parse(cursor.getString(timeIndex));
                    String difficulty = cursor.getString(difficultyIndex);
                    String person = cursor.getString(personIndex);

                    Event event = new Event(name, place, eventDate, time, difficulty, person);
                    events.add(event);
                }
            } while (cursor.moveToNext());
        }


        db.close();
        return events;
    }
}
