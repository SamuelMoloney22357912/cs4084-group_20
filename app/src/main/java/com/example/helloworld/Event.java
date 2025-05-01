package com.example.helloworld;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    private String name;
    private String place;
    private LocalDate date;
    private LocalTime time;
    private String difficulty;
    private String person;

    // Constructor to initialize all fields
    public Event(String name, String place, LocalDate date, LocalTime time, String difficulty, String person) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.time = time;
        this.difficulty = difficulty;
        this.person = person;
    }

    // Getters for each field
    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getPerson() {
        return person;
    }

    // Static method to get events for a specific date (using the database)
    public static ArrayList<Event> getEventsForDate(LocalDate date, CalendarDatabase db) {
        return db.getEventsForDate(date);  // Retrieve events from the database
    }

    // Static method to add an event (using the database)
    public static void addEvent(Event event, CalendarDatabase db) {
        db.addEvent(event); // Save event to the database
    }

    // Static method to delete an event (using the database)
    public static void deleteEvent(Event event, CalendarDatabase db) {
        db.deleteEvent(event); // Delete event from the database
    }
}
