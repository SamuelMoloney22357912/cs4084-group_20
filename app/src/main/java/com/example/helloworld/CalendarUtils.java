package com.example.helloworld;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {

    public static LocalDate selectedDate = LocalDate.now();

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(java.time.LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(formatter);
    }

    public static String formattedMonthYear(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<String> getDaysInMonthArray(LocalDate date) {
        ArrayList<String> daysOfMonth = new ArrayList<>();
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int lengthOfMonth = firstDayOfMonth.lengthOfMonth();

        // Get the day of the week for the first day of the month
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Add empty spaces for padding before the first day
        for (int i = 0; i < dayOfWeek - 1; i++) {
            daysOfMonth.add(""); // Empty space for padding
        }

        // Add the actual days of the month
        for (int i = 1; i <= lengthOfMonth; i++) {
            daysOfMonth.add(String.valueOf(i));
        }

        return daysOfMonth;
    }
}
