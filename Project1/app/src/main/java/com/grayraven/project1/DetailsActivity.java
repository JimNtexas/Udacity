package com.grayraven.project1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends ActionBarActivity implements SwipeInterface{

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_RATING = "rating";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_URL = "image_url";
    private static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        FrameLayout swipe_layout = (FrameLayout) findViewById(R.id.details_frame);
        swipe_layout.setOnTouchListener(swipe);

        String title = getIntent().getStringExtra(MOVIE_TITLE);
        TextView  titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(title));

        String rating = getIntent().getStringExtra(MOVIE_RATING) + " / 10.0";
        TextView ratingTextView = (TextView) findViewById(R.id.rating);
        ratingTextView.setText(Html.fromHtml(rating));

        String release = getIntent().getStringExtra(MOVIE_RELEASE_DATE);
        TextView releaseDateView = (TextView) findViewById(R.id.release_date);
        releaseDateView.setText(Html.fromHtml(formatDate(release)));

        String plot = getIntent().getStringExtra(MOVIE_PLOT);
        TextView   plotTextView = (TextView) findViewById(R.id.plot);
        plotTextView.setText(Html.fromHtml(plot));

        String imageUrl = getIntent().getStringExtra(MOVIE_URL);

        com.grayraven.project1.ImageViewTopCrop imageView = (ImageViewTopCrop) findViewById(R.id.grid_item_image);


        Picasso.with(this).load(imageUrl).into(imageView);
        imageView.setImageAlpha(128);
    }
    /*
     * Given a date string in the format yyyy/mm/dd, convert it
     * to the more common U.S. format of MMMM dd, yyyy
     */
    private String formatDate(String date) {
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            Date dateObj = curFormater.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            return newFormat.format(dateObj);

        } catch (ParseException e) {
            Log.d(TAG, "formatDate unable to parse date from Movie DB, returning string unchanged");
            return date;
        }
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
