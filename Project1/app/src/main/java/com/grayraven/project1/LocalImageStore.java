package com.grayraven.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * http://image.tmdb.org/t/p/w154//8x7ej0LnHdKUqilNNJXYOeyB6L9.jpg
 */

public class LocalImageStore {

    static private final String TAG = "MovieLocalImageStore";
    static private String smallUrl;
    static private String largeUrl;
    static private String smallFilename;
    static private String largeFilename;
    static private String localDir = "PopularMovies";

    static public void savePosterToFile(String movieId, String posterPath, Context context) {

        processPosterPath(movieId, posterPath);
        saveImage(context, smallUrl, smallFilename);
        saveImage(context, largeUrl, largeFilename);
    }

    static private void saveImage(final Context context, String url,  final String filename) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File appDir = context.getExternalFilesDir(localDir);
                        File file = new File(appDir, filename);
                        Log.i(TAG, "app dir:" + appDir.getAbsolutePath());
                        try {
                            Log.i(TAG, "saving: " + filename);
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                            ostream.close();
                            Log.i(TAG, "saved: " + filename);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                return;
            }
        };

           Picasso.with(context).load(url).into(target);

    }

    // see http://developer.android.com/reference/android/content/Context.html#getExternalFilesDir(java.lang.String)
        static public boolean deleteImageFiles(String movieId, Context context) {
            boolean result = false;
            File appDir = context.getExternalFilesDir(localDir);
            File small = new File(appDir, movieId + ".small.jpg");
            File large = new File(appDir, movieId + ".large.jpg");
            Log.i(TAG, "deleteing from " + appDir.getAbsolutePath());
            try {
                if (small != null) {
                  result =  small.delete();
                    if(!result) {
                        Log.e(TAG, "Could not delete file " + movieId + ".small.jpg");
                    }
                }
                if(large != null) {
                    boolean largeResult = large.delete();
                    if(!result) {
                        Log.e(TAG, "Could not delete file " + movieId + ".large.jpg");
                    }
                    result = result && largeResult;
                }

            } catch(Exception e) {
                Log.e(TAG, "file delete error for movie " + movieId );
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
                return false;
            }
            return result;
    }
    static private void processPosterPath(String movieId, String posterPath) {

        StringBuilder builder = new StringBuilder(posterPath);
        builder.delete(0, posterPath.lastIndexOf("//") + 2);
        String imageId = builder.toString();
        smallUrl = posterPath;
        largeUrl = posterPath.replace("w154","w185");
        smallFilename = movieId + ".small.jpg";
        largeFilename = movieId + ".large.jpg";
        Log.i(TAG, "image id: " + imageId);
        Log.i(TAG, "small url: " + smallUrl);
        Log.i(TAG, "large url: " + largeUrl);

    }


}
