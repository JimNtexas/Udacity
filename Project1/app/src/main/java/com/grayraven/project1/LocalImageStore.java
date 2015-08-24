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

@SuppressWarnings({"UnusedParameters", "ConstantConditions", "unused"})
class LocalImageStore {

    static private final String TAG = "MovieLocalImageStore";
    static private final String localDir = "PopularMovies";

    static public void savePosterToFile(String movieId, String posterPath, Context context) {

        Log.d(TAG, "saving to local file not yet implemented"); //TODO: activate when off-line mode is added
      /*  processPosterPath(movieId, posterPath);
        saveImage(context, smallUrl, smallFilename);
        saveImage(context, largeUrl, largeFilename);*/
    }

    static private void saveImage(final Context context, String url,  final String filename) {
        @SuppressWarnings("EmptyMethod") Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File appDir = context.getExternalFilesDir(localDir);  //TODO: save as private file
                        File file = new File(appDir, filename);
                        try {
                            Log.i(TAG, "saving: " + filename);
                            if(file.createNewFile()) {
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.close();
                                Log.i(TAG, "saved: " + filename);
                            } else {
                                Log.e(TAG, "Could not save " + filename);
                            }
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
            }
        };

           Picasso.with(context).load(url).into(target);

    }

    // see http://developer.android.com/reference/android/content/Context.html#getExternalFilesDir(java.lang.String)
        static public void deleteImageFiles(String movieId, Context context) {
            boolean result = false;
            File appDir = context.getExternalFilesDir(localDir);  //todo - save private
            File small = new File(appDir, movieId + ".small.jpg");
            File large = new File(appDir, movieId + ".large.jpg");
            try {
                if (small != null) {
                  result =  small.delete();
                    if(!result) {
                        Log.e(TAG, "Could not delete file " + movieId + ".small.jpg");
                    }
                }
                if(large != null) {
                    boolean largeResult = large.delete();
                    if(!largeResult) {
                        Log.e(TAG, "Could not delete file " + movieId + ".large.jpg");
                    }
                }

            } catch(Exception e) {
                Log.e(TAG, "file delete error for movie " + movieId );
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

    //TODO: implement offline mode
    static private void processPosterPath(String movieId, String posterPath) {
        StringBuilder builder = new StringBuilder(posterPath);
        builder.delete(0, posterPath.lastIndexOf("//") + 2);
        String imageId = builder.toString();
//        String largeFilename = movieId + ".large.jpg";
//        Log.i(TAG, "small url: " + posterPath);
    }


}
