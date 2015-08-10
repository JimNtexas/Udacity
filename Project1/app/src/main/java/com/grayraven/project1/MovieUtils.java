package com.grayraven.project1;

import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;

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

    /*
     * Given an ImageView, return the size of the image as currently scaled
     */
    public static Point getSizeOfImageView(ImageView imageView) {
        
        int imageViewHeight= imageView.getMeasuredHeight();
        int imageViewWidth=imageView.getMeasuredWidth();
        int imageHeight=imageView.getDrawable().getIntrinsicHeight();//original height of underlying image
        int imageWidth=imageView.getDrawable().getIntrinsicWidth();//original width of underlying image

        if (imageViewHeight/imageHeight<=imageViewWidth/imageWidth) imageViewWidth=imageWidth*imageViewHeight/imageHeight;//rescaled width of image within ImageView
        else imageViewHeight= imageHeight*imageViewWidth/imageWidth;//rescaled height of image within ImageView

        return new Point(imageViewWidth, imageViewHeight);
    }
}