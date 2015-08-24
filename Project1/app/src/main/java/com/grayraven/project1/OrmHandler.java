package com.grayraven.project1;

import android.util.Log;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to manage Sugar ORM database operations
 */
class OrmHandler {

    static private final String TAG = "MovieOrmHandler";

    static boolean isFavorite(LocalMovie m) {
       return isMovieInDb(m.getMovieId());
    }

    static boolean isFavorite(ExtendedMovie m) {
        String movieId = Integer.toString( m.getId() );
        return isMovieInDb(movieId);
    }

    static boolean isFavorite(String movieId) {
        return isMovieInDb(movieId);
    }

    static private boolean isMovieInDb(String movieId) {

        Select dupQuery = Select.from(LocalMovie.class)
                .where(Condition.prop("movieid").eq(movieId))
                .limit("1");
        return dupQuery.count() > 0;
    }

    static public void insertMovie(LocalMovie m) {
        // delete any duplicate MovieID row
        Select dupQuery = Select.from(LocalMovie.class)
                    .where(Condition.prop("movieid").eq(m.getMovieId()))
                    .limit("1");

        Log.i(TAG, "query size: " + dupQuery.count());
        if(dupQuery.count() > 0) {
            Log.i(TAG, m.getTitle() + " already exists in db, deleting old record");
            @SuppressWarnings("unchecked") List<LocalMovie> dups = dupQuery.list();
            dups.get(0).delete();
        }

        m.save();
        Log.i(TAG, "Saved record for " + m.getTitle() + " - " + m.getMovieId());
    }

    static public void deleteMovie(LocalMovie m) {
        Select dupQuery = Select.from(LocalMovie.class)
                .where(Condition.prop("movieid").eq(m.getMovieId()))
                .limit("1");

        @SuppressWarnings("unchecked") List<LocalMovie> dups = dupQuery.list();
        if( dupQuery.count() > 0) {
            Log.i(TAG, "deleting favorite " + m.getTitle());
            dups.get(0).delete();
        } else
        {
            Log.e(TAG, "Could not delete " + m.getTitle() + " - it's not in the DB!");
        }
    }

    static void dump() {
        Log.i(TAG, "Local Movie database:");
        List<LocalMovie> movies = LocalMovie.listAll(LocalMovie.class);
        for(LocalMovie movie : movies) {
            Log.i(TAG, movie.getTitle());
        }
    }

    static List<Integer> getFavoriteIds() {
        @SuppressWarnings("Convert2Diamond") ArrayList<Integer> favorites = new ArrayList<Integer>();
        List<LocalMovie> movies = LocalMovie.listAll(LocalMovie.class);
        Collections.sort(movies); //sort by rating
        for(LocalMovie movie : movies){
            favorites.add(Integer.parseInt(movie.getMovieId()));
        }
        return favorites;
    }


}
