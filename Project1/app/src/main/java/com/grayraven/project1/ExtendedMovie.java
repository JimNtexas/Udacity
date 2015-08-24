package com.grayraven.project1;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;

/**
 * Extends MovieDb to include the list of movie trailers
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
class ExtendedMovie extends MovieDb implements Comparable<ExtendedMovie> {
    private final String TAG = "MovieExtendedDb";
    private List<Video> mTrailers =new ArrayList<>();
    private MovieDb mDb;
    private boolean mIsFavorite = false;
    private String mSmallPosterId;
    private String mLargePosterId;

    public MovieDb getmDb() {
        return mDb;
    }

    public List<Video> getmTrailers() {
        return mTrailers;
    }

    ExtendedMovie(MovieDb db) {
        mDb = db;
        mIsFavorite = OrmHandler.isFavorite(Integer.toString( db.getId() ));
        int id = db.getId();
        TmdbApi tmdb = TmdbSingleton.getTmdbInstance();
        if(id > 0) {
            mTrailers = tmdb.getMovies().getVideos(id, "en");
            for(Video vid: mTrailers)
            {
                if(!vid.getSite().equalsIgnoreCase("YouTube")) {

                    Log.i(TAG, "unknown movie site: " + vid.getSite());
                }
            }
        }
    }

    public String getTitle() {
        return mDb.getOriginalTitle();
    }

    public MovieDb getMovie() {
        return mDb;
    }

    public int getMovieId() {
        return mDb.getId();
    }

    public boolean isFavorite() { return mIsFavorite; }
    public void setFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }

    public String getmSmallPosterId() {
        return mSmallPosterId;
    }

    public void setmSmallPosterId(String mSmallPosterId) {
        this.mSmallPosterId = mSmallPosterId;
    }

    public String getmLargePosterId() {
        return mLargePosterId;
    }

    public void setmLargePosterId(String mLargePosterId) {
        this.mLargePosterId = mLargePosterId;
    }

    public List<Video> getTrailers() {
        return mTrailers;
    }

    public int compareTo(@NonNull ExtendedMovie m) {
        return Boolean.compare( m.isFavorite(), mIsFavorite);
    }
}
