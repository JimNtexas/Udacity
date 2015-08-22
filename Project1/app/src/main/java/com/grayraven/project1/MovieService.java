package com.grayraven.project1;


import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.ResponseStatusException;

/**
 * Connect with Movie database using theMovieApp api,
 * download the first page of movies according to the provided parameters
 * Created by jhoward on 7/25/2015.
 */
public class MovieService  extends IntentService{

    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_API_ERROR = 2;
    public static final String SERVICE_ERROR = "service_error";
    public static final String RESULT_STATUS = "result_status";
    public static final String MOVIE_SERVICE_INTENT = "movie_service";
    public static final String MOVIE_LIST_JSON = "movie_json";
    public static final String SORT_PREFERENCE = "sort_pref";
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_USER_RATING = "vote_average.desc";
    public static final String POSTER_SIZE_STANDARD = "w185";
    public static final String POSTER_SIZE_SMALL = "w154";
    public static final String INCLUDE_FAVORITES = "include_all_favorites";
    private static final int MIN_VOTE_CNT = 1000;
    private static final String TAG = "MovieService";

    TmdbApi tmdb = null;
    List<ExtendedMovie> mymovies = null;

    public MovieService() {
        super(MovieService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent)  {
        Log.i(TAG, "Movie Service Start");
        mymovies = null;

        String sortBy = intent.getStringExtra(SORT_PREFERENCE);
        if(sortBy == null) {
            sortBy = SORT_BY_POPULARITY;
        }

        Log.i(TAG, "Sorting by: " + sortBy);

        try {
            tmdb = TmdbSingleton.getTmdbInstance();
        } catch (ResponseStatusException e) {
            Intent response = new Intent(MOVIE_SERVICE_INTENT);
            response.putExtra(RESULT_STATUS, STATUS_API_ERROR);
            response.putExtra(SERVICE_ERROR, e.getMessage());
            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
            Log.e(TAG, "Movie service error");
            this.stopSelf();
        }
        Discover discover = new Discover();
        discover.page(1);
        discover.sortBy(sortBy);
        discover.voteCountGte(MIN_VOTE_CNT);
        List<MovieDb> movies = tmdb.getDiscover().getDiscover(discover).getResults();
        mymovies  = new ArrayList<ExtendedMovie>();

        for(MovieDb db : movies) {
            mymovies.add(new ExtendedMovie(db));
        }

        Log.i(TAG, "BEFORE SORT [title - popularity - rating:");
        for(ExtendedMovie m : mymovies) {
           Log.i(TAG, m.getTitle() + " - " + m.getmDb().getPopularity() + " - " + m.getmDb().getVoteAverage() + (m.isFavorite() ? "*" : ""));
        }

        boolean include_favorites = intent.getBooleanExtra(INCLUDE_FAVORITES, false);
        if(include_favorites) {
            AddFavoriteIfNotPresent();
        }

        Collections.sort(mymovies);


        Log.i(TAG, "AFTER SORT [title - popularity - rating:");
        for(ExtendedMovie m : mymovies) {
            Log.i(TAG, m.getTitle() + " - " + m.getmDb().getPopularity() + " - " + m.getmDb().getVoteAverage() + (m.isFavorite() ? "*" : ""));
        }

        Intent response = new Intent(MOVIE_SERVICE_INTENT);
        String json = new Gson().toJson(mymovies);
        response.putExtra(RESULT_STATUS, STATUS_FINISHED);
        response.putExtra(MOVIE_LIST_JSON, json);  //TODO: Base64 encode to reduce chance of SQL injection?
        LocalBroadcastManager.getInstance(this).sendBroadcast(response);
        Log.i(TAG, "Movie request complete");
        this.stopSelf();
    }

    public static String getPosterUrl(String posterPath, String size ) {
        final String posterFormat = "http://image.tmdb.org/t/p/%s/%s"; //w154 or w185
        String url = String.format(posterFormat, posterPath, size);
        return url;
    }

    void AddFavoriteIfNotPresent() {
        ArrayList<Integer> favIds = (ArrayList<Integer>) OrmHandler.getFavoriteIds();
        ArrayList<Integer> idsToAdd = new ArrayList<Integer>();
        for(ExtendedMovie m : mymovies) {
            if(favIds.contains(m.getMovieId())) {
               favIds.remove(favIds.indexOf(m.getMovieId()));
                Log.d(TAG, m.getTitle() + " is already a favorite");
            }
        }
        if(favIds.size() > 0) {
            for(int id : favIds) {
                addFavorite(id);
            }
        }
    }

    private void addFavorite(int id){
        TmdbMovies movies = tmdb.getMovies();
        MovieDb movie = movies.getMovie(id,"en");
        if(movie != null) {
            Log.d(TAG, "Adding favorite " + movie.getOriginalTitle());
            mymovies.add(new ExtendedMovie(movie));
        }
    }


}



