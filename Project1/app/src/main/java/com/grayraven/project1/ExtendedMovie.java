package com.grayraven.project1;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;

/**
 * Extends MoviveDB to include the list of movie trailers
 */
public class ExtendedMovie extends MovieDb {
    private final String TAG = "MovieExtendedDb";
    private List<Video> mTrailers =new ArrayList<>();
    private MovieDb mDb;

    ExtendedMovie(MovieDb db) {
        mDb = db;
        int id = db.getId();
        Log.i(TAG, "Constructed " + mDb.getOriginalTitle());
        TmdbApi tmdb = TmdbSingleton.getTmdbInstance();
        if(id > 0) {
            mTrailers = tmdb.getMovies().getVideos(id, "en");
          /*  if(mTrailers != null && mTrailers.size() > 0) {
                for(Video v : mTrailers) {
                    Log.i(TAG, "site: " + v.getSite());
                    Log.i(TAG, " key: " + v.getKey());
                    Log.i(TAG, "type: " + v.getType());
                } */
        }
    }

    public MovieDb getMovie() {
        return mDb;
    }

    public List<Video> getTrailers() {
        return mTrailers;
    }

    public int getNumberOfTrailers() {
        return mTrailers == null ? 0 : mTrailers.size();
    }
}
