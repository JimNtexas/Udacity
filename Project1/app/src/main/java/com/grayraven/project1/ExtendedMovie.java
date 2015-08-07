package com.grayraven.project1;

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
        TmdbApi tmdb = TmdbSingleton.getTmdbInstance();
        if(id > 0) {
            mTrailers = tmdb.getMovies().getVideos(id, "en");
        }
    }

    public MovieDb getMovie() {
        return mDb;
    }

    public List<Video> getTrailers() {
        return mTrailers;
    }
}
