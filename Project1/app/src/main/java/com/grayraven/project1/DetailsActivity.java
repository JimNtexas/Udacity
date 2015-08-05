package com.grayraven.project1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends ActionBarActivity implements SwipeInterface{

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_RATING = "rating";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_URL = "image_url";
    public static final String MOVIE_ID = "movie_id";
    private static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.details_rl);
        swipe_layout.setOnTouchListener(swipe);

        String title = getIntent().getStringExtra(MOVIE_TITLE);
        TextView  titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(title));

        String rating = getIntent().getStringExtra(MOVIE_RATING) + " / 10.0";
        TextView ratingTextView = (TextView) findViewById(R.id.rating);
        ratingTextView.setText(Html.fromHtml(rating));

        String release = getIntent().getStringExtra(MOVIE_RELEASE_DATE);
        TextView releaseDateView = (TextView) findViewById(R.id.release_date);
        releaseDateView.setText(Html.fromHtml(getFormatedDate(release)));

        String plot = getIntent().getStringExtra(MOVIE_PLOT);
        TextView   plotTextView = (TextView) findViewById(R.id.plot);
        plotTextView.setText(Html.fromHtml(plot));

        String imageUrl = getIntent().getStringExtra(MOVIE_URL);

        //com.grayraven.project1.ImageViewTopCrop imageView = (ImageViewTopCrop) findViewById(R.id.grid_item_image);
        ImageView posterView = (ImageView) findViewById(R.id.poster);
        Picasso.with(this).load(imageUrl).into(posterView);
      //  imageView.setImageAlpha(128);

        Log.i(TAG, "Trailers for: todo " + title);

    }

    /*
     * return date string based on screen orientation
     */
    private String getFormatedDate(String date) {
        String result;
        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            result = MovieUtils.formatShortDate(date);
        } else {
            result = MovieUtils.formatLongDate(date);
        }
        return result;
    }

    /*private List<Video> getTrailers(int id) {
        List<Video> vids = null;
        if(id > 0) {
            TmdbApi tmdb = TmdbSingleton.getTmdbInstance();
            vids = tmdb.getMovies().getVideos(id, "");
            if(vids != null && vids.size() > 0) {
                for(Video v : vids) {
                    Log.i(TAG, "site: " + v.getSite());
                    Log.i(TAG, " key: " + v.getKey());
                    Log.i(TAG, "type: " + v.getType());
                }
            }
        }
       return vids;
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void left2right(View v) {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
