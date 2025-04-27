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
        for (int i = 1; i <= lengthOfMonth; i++) {
            daysOfMonth.add(String.valueOf(i));
        }
        return daysOfMonth;
    }
}
