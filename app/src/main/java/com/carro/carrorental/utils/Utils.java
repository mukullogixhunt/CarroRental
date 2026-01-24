package com.carro.carrorental.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static boolean isAdvShow=true;
    public static String getDate(long milliSeconds, String outputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static int calculateDays(
            String pickDate,
            String pickTime,
            String dropDate,
            String dropTime
    ) {
        try {
            // Input format: 31-12-2025 05:05 PM
            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);

            Date pickDateTime = sdf.parse(pickDate + " " + pickTime);
            Date dropDateTime = sdf.parse(dropDate + " " + dropTime);

            if (pickDateTime == null || dropDateTime == null) {
                return 0;
            }

            long diffMillis = dropDateTime.getTime() - pickDateTime.getTime();
            long diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis);

            // Convert hours → days (round up)
            return (int) Math.ceil(diffHours / 24.0);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String formatMapDuration(String mapDuration) {
        // Return an empty string if the input is null or empty to prevent crashes
        if (mapDuration == null || mapDuration.trim().isEmpty()) {
            return "";
        }

        int hours = 0;
        int minutes = 0;

        try {
            // Split the string into parts: ["8", "hours", "0", "mins"]
            String[] parts = mapDuration.toLowerCase().split("\\s+"); // Split by any whitespace

            // Find the numbers for hours and minutes
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].contains("hour")) {
                    // The number is the part just before "hour"
                    if (i > 0) {
                        hours = Integer.parseInt(parts[i-1]);
                    }
                } else if (parts[i].contains("min")) {
                    // The number is the part just before "min"
                    if (i > 0) {
                        minutes = Integer.parseInt(parts[i-1]);
                    }
                }
            }
        } catch (Exception e) {
            // If parsing fails for any reason (e.g., unexpected format), just return the original string
            e.printStackTrace();
            return mapDuration;
        }

        StringBuilder formattedString = new StringBuilder();

        // Append hours part only if it's greater than 0
        if (hours > 0) {
            formattedString.append(hours).append(" hrs");
        }

        // Append minutes part only if it's greater than 0
        if (minutes > 0) {
            // Add a space if hours were also added
            if (formattedString.length() > 0) {
                formattedString.append(" ");
            }
            formattedString.append(minutes).append(" min");
        }

        // If both were 0, the string will be empty.
        // If the original string was something like "Less than 1 min", it will be returned by the catch block.
        return formattedString.toString();
    }



    public static String changeDateFormat(String fromFormat, String toFormat, String dateStr) {

        SimpleDateFormat sdfIn = new SimpleDateFormat(fromFormat, Locale.US);
        Date date = null;
        try {
            date = sdfIn.parse(dateStr);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat(toFormat, Locale.US);
        String formattedTime = sdfOut.format(date);

        return formattedTime;

    }

    public static String formatDate(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

    public static String formatTime(long millis) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date(millis));
    }
    public static String formatTimeString(String fromFormat, String toFormat, String timeStr) {
        SimpleDateFormat sdfIn = new SimpleDateFormat(fromFormat, Locale.getDefault());
        Date date = null;
        try {
            date = sdfIn.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr; // Return the original string if parsing fails
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat(toFormat, Locale.getDefault());
        return sdfOut.format(date);
    }


    public static String getMonthName(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String getMonthShortName(int monthNumber) {
        String monthName = "";

        if (monthNumber >= 1 && monthNumber <= 12) {
            try {
                Calendar calendar = Calendar.getInstance();
                // Adjust for zero-based indexing
                calendar.set(Calendar.MONTH, monthNumber - 1);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monthName;
    }



    public static class DurationResult {
        public long days;
        public long hours;
        public long minutes;
        public boolean isError;
        public String errorMessage;

        public DurationResult(long days, long hours, long minutes) {
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.isError = false;
        }

        public DurationResult(String errorMessage) {
            this.isError = true;
            this.errorMessage = errorMessage;
        }

        public String getFormattedString() {
            if (isError) {
                return "Error: " + errorMessage;
            }
            return days + " days, " + hours + " hours, " + minutes + " minutes";
        }
    }

    public static DurationResult calculateDuration(String pickupDateString, String pickupTimeString,
                                                   String returnDateString, String returnTimeString) {
        try {
            String pickupDateTimeString = pickupDateString + " " + pickupTimeString;
            String returnDateTimeString = returnDateString + " " + returnTimeString;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd-MM-yyyy hh:mm a")
                        .toFormatter(Locale.ENGLISH);

                LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeString, formatter);
                LocalDateTime returnDateTime = LocalDateTime.parse(returnDateTimeString, formatter);
                Duration duration = Duration.between(pickupDateTime, returnDateTime);
                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                long minutes = duration.toMinutes() % 60;
                return new DurationResult(days, hours, minutes);

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
                sdf.setLenient(true);
                Date pickupDate = sdf.parse(pickupDateTimeString);
                Date returnDate = sdf.parse(returnDateTimeString);
                long diff = returnDate.getTime() - pickupDate.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) % 24;
                long minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) % 60;
                return new DurationResult(days, hours, minutes);
            }

        } catch (java.time.format.DateTimeParseException e) {
            e.printStackTrace();
            return new DurationResult("Invalid date/time format - " + e.getMessage());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return new DurationResult("Invalid date/time format - " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new DurationResult(e.getMessage());
        }
    }

//    public static String calculateDurationString(String pickupDateString, String pickupTimeString, String returnDateString, String returnTimeString) {
//        try {
//            String pickupDateTimeString = pickupDateString + " " + pickupTimeString;
//            String returnDateTimeString = returnDateString + " " + returnTimeString;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                        .parseCaseInsensitive() // Ignore case for AM/PM
//                        .appendPattern("dd-MM-yyyy hh:mm a") // Pattern for date and time
//                        .toFormatter(Locale.ENGLISH);
//
//                LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeString, formatter);
//                LocalDateTime returnDateTime = LocalDateTime.parse(returnDateTimeString, formatter);
//                Duration duration = Duration.between(pickupDateTime, returnDateTime);
//                long days = duration.toDays();
//                long hours = duration.toHours() % 24;
//                long minutes = duration.toMinutes() % 60;
//                return days + " days, " + hours + " hours, " + minutes + " minutes";
//
//            } else {
//
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
//                sdf.setLenient(true); // Optional: Makes parsing more forgiving
//                Date pickupDate = sdf.parse(pickupDateTimeString);
//                Date returnDate = sdf.parse(returnDateTimeString);
//                long diff = returnDate.getTime() - pickupDate.getTime();
//                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//                long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) % 24;
//                long minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS) % 60;
//                return days + " days, " + hours + " hours, " + minutes + " minutes";
//            }
//
//        } catch (java.time.format.DateTimeParseException e) {
//            e.printStackTrace();
//            return "Error: Invalid date/time format - " + e.getMessage();
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return "Error: Invalid date/time format - " + e.getMessage();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }

    public static void attachDatePicker(
            Context context,
            TextView targetTextView
    ) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {

                    // Month is 0-based, so +1
                    int realMonth = month + 1;

                    // Format yyyy-MM-dd
                    String formattedDate = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            realMonth,
                            dayOfMonth
                    );

                    targetTextView.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


}
