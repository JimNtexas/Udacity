package com.grayraven.project1;

import android.util.Log;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.ResponseStatusException;

/**
 * Create only one Movie Database object for this app
 */
class TmdbSingleton {
    private static TmdbApi tmdbInstance = null;
    private static final String TAG = "movie_tmdb_singleton";

    protected TmdbSingleton() {
    }

    public static TmdbApi getTmdbInstance() throws ResponseStatusException {
        if(tmdbInstance == null) {
            try {
                tmdbInstance = new TmdbApi(ApiKey.API_KEY);
                Log.i(TAG, "Tmdb singleton created");
            } catch(ResponseStatusException e) {
                Log.i(TAG, "Exception: " + e.getMessage());
                throw(e);
            }
        }
        return tmdbInstance;
    }
}
