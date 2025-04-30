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

    // Method to delete an event
    public static void deleteEvent(Event event) {
        eventsList.remove(event); // Removes the event from the list
    }

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

    // Method to add an event to the list
    public static void addEvent(Event event) {
        eventsList.add(event); // Adds the event to the static list
    }
}
