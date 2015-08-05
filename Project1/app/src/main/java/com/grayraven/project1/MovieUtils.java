package com.grayraven.project1;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieUtils {

    private static final String TAG = "movie_utils";

    /*
     * Given a date string in the format yyyy/mm/dd, convert it
     * to the more common U.S. long format of MMMM dd, yyyy
     */
    public static String formatLongDate(String date) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            Date dateObj = curFormater.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            return newFormat.format(dateObj);

        } catch (ParseException e) {
            Log.d(TAG, "formatDate unable to parse date from Movie DB, returning string unchanged");
            return date;
        }
    }

    /*
     * Given a date string in the format yyyy/mm/dd, convert it
     * to the more common U.S. short format of MMMM yyyy
     */
    public static String formatShortDate(String date) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            Date dateObj = curFormater.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("mm - yyyy", Locale.US);
            return newFormat.format(dateObj);

        } catch (ParseException e) {
            Log.d(TAG, "formatDate unable to parse date from Movie DB, returning string unchanged");
            return date;
        }
    }
}
