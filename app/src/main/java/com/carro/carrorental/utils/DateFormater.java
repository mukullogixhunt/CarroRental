package com.carro.carrorental.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormater {

    public static String getDate(long milliSeconds, String outputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getFutureDateFrom(String startDateString, int daysToAdd) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();

        try {
            if (startDateString != null && !startDateString.trim().isEmpty()) {
                Date startDate = parser.parse(startDateString);
                calendar.setTime(startDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return formatter.format(calendar.getTime());
    }

    public static String changeDateFormat(String fromFormat, String toFormat, String dateStr) {

        SimpleDateFormat sdfIn = new SimpleDateFormat(fromFormat, Locale.US);
        java.util.Date date = null;
        try {
            date = sdfIn.parse(dateStr);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat(toFormat, Locale.US);
        String formattedTime = sdfOut.format(date);

        return formattedTime;

    }

    public static String formatTime(long timeInMillis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return sdf.format(calendar.getTime());
    }

    public static String formatTo24Hour(String timeIn12Hour) {
        try {
            // Define the input format (12-hour format)
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            // Define the output format (24-hour format)
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            // Parse the input time and format it into 24-hour time
            Date date = inputFormat.parse(timeIn12Hour);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
