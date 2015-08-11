package com.grayraven.project1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;


public class MainActivity extends ActionBarActivity {

    private final String TAG = "MovieMain";
    private List<ExtendedMovie> mMovies = new ArrayList<>();
    private GridViewAdapter gridAdapter;
    private Context mContext;
    private Menu mOptions;
    private String mSortPreference;
    private ProgressDialog mLoadProgress;
    private final String ACTIVITY_SORT_PREFERENCE = "activity_sort_pref";
    SharedPreferences mPrefs;
    private boolean mTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortPreference = mPrefs.getString(ACTIVITY_SORT_PREFERENCE, MovieService.SORT_BY_POPULARITY);

        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        @SuppressWarnings("UnusedAssignment") Logger logger = LoggerFactory.getLogger(MainActivity.class); //required by themoviedbapi

        if(findViewById(R.id.tablet_detail) != null) {
            mTablet = true;
            DetailsFragment fragment = new DetailsFragment();
            getSupportFragmentManager().beginTransaction()
                  .replace(R.id.tablet_detail, fragment, DetailsFragment.TAG).commit();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(MovieService.MOVIE_SERVICE_INTENT));

        gridAdapter = new GridViewAdapter(this, mMovies);
        GridView mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(gridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                MovieDb item = gridAdapter.getMovie(position);
                String json = new Gson().toJson(gridAdapter.getTrailers(position));
                String title = item.getOriginalTitle();
                String url;
                String plot = item.getOverview();
                String releaseDate = item.getReleaseDate();
                int movieId = item.getId();
                String rating = Float.toString(item.getVoteAverage());

                if(mTablet) {
                    Bundle args = new Bundle();
                    args.putString(DetailsActivity.MOVIE_TITLE, title);
                    args.putString(DetailsActivity.MOVIE_TRAILER_JSON, json);
                    url = MovieService.getPosterUrl(MovieService.POSTER_SIZE_SMALL,item.getPosterPath());
                    args.putString(DetailsActivity.MOVIE_URL,url);
                    args.putString(DetailsActivity.MOVIE_PLOT,plot);
                    args.putString(DetailsActivity.MOVIE_RELEASE_DATE, releaseDate);
                    args.putInt(DetailsActivity.MOVIE_ID, movieId);
                    args.putString(DetailsActivity.MOVIE_RATING, rating);
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.tablet_detail, fragment, DetailsFragment.TAG).commit();

                } else {

                    //Pass the image title and url to DetailsActivity
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE_TITLE, title);
                    url = MovieService.getPosterUrl(MovieService.POSTER_SIZE_STANDARD, item.getPosterPath());
                    intent.putExtra(DetailsActivity.MOVIE_URL, url);
                    intent.putExtra(DetailsActivity.MOVIE_PLOT,plot);
                    intent.putExtra(DetailsActivity.MOVIE_RELEASE_DATE, releaseDate);
                    intent.putExtra(DetailsActivity.MOVIE_ID, movieId);
                    intent.putExtra(DetailsActivity.MOVIE_TRAILER_JSON, json);
                    intent.putExtra(DetailsActivity.MOVIE_RATING, rating);
                    //Start details activity
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        StartMovieService();
    }

    private void StartMovieService() {
        if(isNetworkConnected()) {
            Intent service = new Intent(getApplicationContext(), MovieService.class);
            service.putExtra(MovieService.SORT_PREFERENCE, mSortPreference);
            service.setPackage("com.grayraven.project1");
            Log.i(TAG, "starting service");
            ShowLoadingProgress();
            startService(service);
        } else
        {
            //TODO: Show a clever error dialog or view
            Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
      //  mOptions.findItem(R.id.menuPopularity).setChecked(false);
       // mOptions.findItem(R.id.menuRated).setChecked(false);
        mSortPreference = mPrefs.getString(ACTIVITY_SORT_PREFERENCE, MovieService.SORT_BY_POPULARITY);
        if(mSortPreference.equals(MovieService.SORT_BY_POPULARITY)) {
            menu.findItem(R.id.menuPopularity).setChecked(true);
            menu.findItem(R.id.menuRated).setChecked(false);
        } else {
            menu.findItem(R.id.menuRated).setChecked(true);
            menu.findItem(R.id.menuPopularity).setChecked(false);
        }
        mOptions = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mOptions = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Log.i(TAG,"action settings");
                break;
            case R.id.menuPopularity:
                Log.i(TAG, "popularity");
                mSortPreference = MovieService.SORT_BY_POPULARITY;
                item.setChecked(true);
                mOptions.findItem(R.id.menuRated).setChecked(false);
                StartMovieService();
                break;
            case R.id.menuRated:
                Log.i(TAG, "rated");
                mSortPreference = MovieService.SORT_BY_USER_RATING;
                mOptions.findItem(R.id.menuPopularity).setChecked(false);
                item.setChecked(true);
                StartMovieService();
                break;
        }
        mPrefs.edit().putString(ACTIVITY_SORT_PREFERENCE, mSortPreference).commit();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(!action.equals(MovieService.MOVIE_SERVICE_INTENT)) {
                Log.d(TAG, "Unknown intent: " + action);
                return;
            }
            mLoadProgress.cancel();
            int result =  intent.getExtras().getInt(MovieService.RESULT_STATUS);
            String json = intent.getStringExtra(MovieService.MOVIE_LIST_JSON);
            if(result == MovieService.STATUS_FINISHED) {
                //noinspection unchecked
                mMovies = new Gson().fromJson(json, new TypeToken<List<ExtendedMovie>>(){}.getType());
               /* for(ExtendedMovie db : mMovies) {
                     Log.i(TAG, "Title: " + db.getOriginalTitle());
                    Log.i(TAG, "Number of trailers: "  + db.getTrailers().size());
                    Log.i(TAG, "popularity: " + db.getPopularity());
                    Log.i(TAG, "thumb: " + db.getPosterPath());
                    Log.i(TAG, "plot : " + db.getOverview() );
                    Log.i(TAG, "rating : " + db.getVoteAverage());
                    Log.i(TAG, "popularity: " + db.getPopularity());
                    Log.i(TAG, "release date: " + db.getReleaseDate());
                } */



                gridAdapter.setGridData(mMovies);
            } else if(result == MovieService.STATUS_API_ERROR) {
                String msg = intent.getStringExtra(MovieService.SERVICE_ERROR);
                Toast.makeText(mContext, "Unable to contact movie database server: " + msg,Toast.LENGTH_LONG).show();
            }
        }

    };

    /** Check the active network to see if it is connected or connecting */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void ShowLoadingProgress() {
        mLoadProgress = new ProgressDialog(this);
        mLoadProgress.setCancelable(false);
        mLoadProgress.setTitle(getString(R.string.loading_movies));
        mLoadProgress.setMessage(getString((R.string.loading_msg)));
        mLoadProgress.show();
    }
}


