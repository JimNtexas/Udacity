package com.grayraven.project1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

@SuppressWarnings("WeakerAccess")
class LocalMovie extends SugarRecord<LocalMovie> implements Comparable<LocalMovie> {

    @Ignore
    private static final String TAG = "MovieLocal";

    private String title;
    private String rating;
    private String releaseDate;
    private String plot;
    private String movieid;
    private String trailerJson;
    private String posterPath;

    public LocalMovie() {
    }

    public LocalMovie(String title, String rating, String releaseDate, String plot, String movieid, String trailerJson, String posterPath) {
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.plot = plot;
        this.movieid = movieid;
        this.trailerJson = trailerJson;
        this.posterPath = posterPath;
    }

    public void setBundle(Bundle args) {
        title = (String) args.get(DetailsActivity.MOVIE_TITLE);
        rating = (String) args.get(DetailsActivity.MOVIE_RATING);
        releaseDate = (String) args.get(DetailsActivity.MOVIE_RELEASE_DATE);
        plot = (String) args.get(DetailsActivity.MOVIE_PLOT);
        movieid = Integer.toString( args.getInt(DetailsActivity.MOVIE_ID) );
        trailerJson = (String) args.get(DetailsActivity.MOVIE_TRAILER_JSON);
        Log.i(TAG, "LocalMovie created");
    }



   public String getTrailerJson() {
        return trailerJson;
    }

    public String getMovieId() {
        return movieid;
    }

    public String getPlot() {
        return plot;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public int compareTo(@NonNull LocalMovie another) {
        Float thisRating = Float.parseFloat(getRating());
        return Float.compare(Float.parseFloat(another.getRating()), thisRating);
    }
}
