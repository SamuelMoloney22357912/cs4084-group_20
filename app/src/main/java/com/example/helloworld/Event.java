package com.example.helloworld;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    // Static list to store all events
    public static ArrayList<Event> eventsList = new ArrayList<>();

    // Method to get events for a specific date
    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventsList) {
            if (event.getDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    // Method to get events for a specific month and year
    public static ArrayList<Event> getAllEventsForMonth(int year, int month) {
        ArrayList<Event> eventsForMonth = new ArrayList<>();
        for (Event event : eventsList) {
            // Check if the event's year and month match the provided values
            if (event.getDate().getYear() == year && event.getDate().getMonthValue() == month) {
                eventsForMonth.add(event);
            }
        }
        return eventsForMonth;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;

    public Event(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
