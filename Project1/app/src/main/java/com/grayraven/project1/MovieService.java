package com.grayraven.project1;


import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;

/**
 * Connect with Movie database using theMovieApp api,
 * download the first page of movies according to the provided parameters
 * Created by jhoward on 7/25/2015.
 */
public class MovieService  extends IntentService{

    // --Commented out by Inspection (7/31/2015 12:17 PM):public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    // --Commented out by Inspection (7/31/2015 12:18 PM):public static final int STATUS_ERROR = 2;
    public static final String RESULT_STATUS = "result_status";
    public static final String MOVIE_SERVICE_INTENT = "movie_service";
    public static final String MOVIE_LIST_JSON = "movie_json";
    public static final String SORT_PREFERENCE = "sort_pref";
    public static final String SORT_BY_POPULARITY = "popularity.desc";
    public static final String SORT_BY_USER_RATING = "vote_average.desc";
    private static final int MIN_VOTE_CNT = 1000;

    private static final String TAG = "MovieService";

    public MovieService() {
        super(MovieService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Movie Service Start");

        String sortBy = intent.getStringExtra(SORT_PREFERENCE);
        if(sortBy == null) {
            sortBy = SORT_BY_POPULARITY;
        }

        TmdbApi tmdb = new TmdbApi(ApiKey.API_KEY);
        getMovieDbConfiguration(tmdb);
        Discover discover = new Discover();
        discover.page(1);
        discover.sortBy(sortBy); // vote_average.desc
        discover.voteCountGte(MIN_VOTE_CNT);
        List<MovieDb> result = tmdb.getDiscover().getDiscover(discover).getResults();

       /* for(MovieDb db : Result) {
            Log.i(TAG, "Title: " + db.getOriginalTitle());
            Log.i(TAG, "thumb: " + db.getPosterPath());
            Log.i(TAG, "plot : " + db.getOverview() );
            Log.i(TAG, "rating : " + db.getVoteAverage());
            Log.i(TAG, "popularity: " + db.getPopularity());
            Log.i(TAG, "release date: " + db.getReleaseDate());
        }*/

        Intent response = new Intent(MOVIE_SERVICE_INTENT);
        String json = new Gson().toJson(result);
        response.putExtra(RESULT_STATUS, STATUS_FINISHED);
        response.putExtra(MOVIE_LIST_JSON, json);
        LocalBroadcastManager.getInstance(this).sendBroadcast(response);
        Log.i(TAG, "Movie request complete");
        this.stopSelf();
    }

    private void getMovieDbConfiguration(TmdbApi tmdb) {
        TmdbConfiguration results = tmdb.getConfiguration();
        Log.i(TAG, results.toString()); //TODO: save locally, update only every few days, per MovieDB request
    }

    public static String getPosterUrl(String posterPath) {
        final String posterFormat = "http://image.tmdb.org/t/p/w185/%s";
        return String.format(posterFormat, posterPath);
    }
}

