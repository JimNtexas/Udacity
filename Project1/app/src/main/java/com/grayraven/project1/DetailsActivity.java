package com.grayraven.project1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class DetailsActivity extends ActionBarActivity implements SwipeInterface{

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_RATING = "rating";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_URL = "image_url";
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_TRAILER_JSON = "trailer_json";

    private static final String TAG = "MovieDetailsActivity";
    private String mVideoListJson;
    private Button btnTrailers = null;
    TrailerDialogFragment mTrailerFragment = null;

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

        ImageView posterView = (ImageView) findViewById(R.id.poster);
        Picasso.with(this).load(imageUrl).into(posterView);

        mVideoListJson = getIntent().getStringExtra(MOVIE_TRAILER_JSON);
        List<Video> trailers  = new Gson().fromJson(mVideoListJson, new TypeToken<List<Video>>() {
        }.getType());

        btnTrailers = (Button) findViewById(R.id.button_trailers);
        btnTrailers.setVisibility((trailers != null && trailers.size() > 0) ? View.VISIBLE : View.INVISIBLE);
        btnTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTrailerDialog(mVideoListJson);
            }
        });
    }


    private void showTrailerDialog(String json) {
        TrailerDialogFragment df = new TrailerDialogFragment().newInstance(json);
        android.support.v4.app.Fragment fr = getSupportFragmentManager().findFragmentByTag(TrailerDialogFragment.TAG);
        if (fr == null) {
            df.show(getSupportFragmentManager(), TrailerDialogFragment.TAG);
        }

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
