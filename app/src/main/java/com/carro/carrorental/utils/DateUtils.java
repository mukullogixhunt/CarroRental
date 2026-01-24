package com.carro.carrorental.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    // Get the current date
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }
    public static String calculateEndDate(int daysToAdd) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Add the specified number of days
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Format the date (e.g., "June 12, 2025")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }
}
