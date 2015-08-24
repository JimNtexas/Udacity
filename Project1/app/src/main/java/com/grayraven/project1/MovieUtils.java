package com.grayraven.project1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class MovieUtils {

    private static final String TAG = "movie_utils";

    /*
     * Given a date string in the format yyyy/mm/dd, convert it
     * to the more common U.S. long format of MMMM dd, yyyy
     */
    static public String formatLongDate(String date) {
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
     * Given a date string in the format yyyy/mm/dd, covert it
     * to the more common U.S. medium form of MMM dd, yyyy
     */
     static public String formatMediumDate(String date) {
         try {
             SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
             Date dateObj = curFormater.parse(date);
             SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
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
    static public String formatShortDate(String date) {
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

    private static String getMetaDataString(Context context, String name) {
        String value = null;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = ai.metaData.getString(name);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't find config value: " + name);
        }

        return value;
    }


}
