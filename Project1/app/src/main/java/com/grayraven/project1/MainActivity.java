package com.grayraven.project1;

import android.annotation.SuppressLint;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;


@SuppressWarnings({"WeakerAccess", "deprecation"})
public class MainActivity extends ActionBarActivity {

    private final String TAG = "MovieMain";
    private List<ExtendedMovie> mMovies = new ArrayList<>();
    private GridViewAdapter gridAdapter;
    private Context mContext;
    private Menu mOptions;
    private String mSortPreference;
    private ProgressDialog mLoadProgress;
    private final String ACTIVITY_SORT_PREFERENCE = "activity_sort_pref";
    public final String INCLUDE_FAVS_PREFERENCE = "include_favorites";
    SharedPreferences mPrefs;
    private boolean mTablet = false;
    private boolean mIncludeFavorites = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortPreference = mPrefs.getString(ACTIVITY_SORT_PREFERENCE, MovieService.SORT_BY_POPULARITY);
        mIncludeFavorites = mPrefs.getBoolean(INCLUDE_FAVS_PREFERENCE, false);

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
                String path = item.getPosterPath(); //todo: debug only
                Log.i(TAG, "raw poster path: " + item.getPosterPath());

                if (mTablet) {
                    Bundle args = new Bundle();
                    args.putString(DetailsActivity.MOVIE_TITLE, title);
                    args.putString(DetailsActivity.MOVIE_TRAILER_JSON, json);
                    url = MovieService.getPosterUrl(MovieService.POSTER_SIZE_SMALL, item.getPosterPath());
                    args.putString(DetailsActivity.MOVIE_URL, url);
                    args.putString(DetailsActivity.MOVIE_PLOT, plot);
                    args.putString(DetailsActivity.MOVIE_RELEASE_DATE, releaseDate);
                    args.putInt(DetailsActivity.MOVIE_ID, movieId);
                    args.putString(DetailsActivity.MOVIE_RATING, rating);
                    DetailsFragment fragment = new DetailsFragment();
                    fragment.setArguments(args);
                    FragmentManager fm = getSupportFragmentManager();
                    //FragmentTransaction ft = fm.beginTransaction();
                    fm.beginTransaction()
                            .replace(R.id.tablet_detail, fragment, DetailsFragment.TAG).commit();

                } else {

                    //Pass the image title and url to DetailsActivity
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE_TITLE, title);
                    url = MovieService.getPosterUrl(MovieService.POSTER_SIZE_STANDARD, item.getPosterPath());
                    intent.putExtra(DetailsActivity.MOVIE_URL, url);
                    intent.putExtra(DetailsActivity.MOVIE_PLOT, plot);
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

        Log.d(TAG, "Starting service from ONCREATE");
        StartMovieService(false); // the movie service will only download if it's list of movies is empty
    }

    private void StartMovieService(boolean refresh) {
        Log.i(TAG, "Starting movie service.  Refresh: " + refresh);
        if(isNetworkConnected()) {
            Intent service = new Intent(getApplicationContext(), MovieService.class);
            service.putExtra(MovieService.SORT_PREFERENCE, mSortPreference);
            service.putExtra(MovieService.INCLUDE_FAVORITES, mIncludeFavorites);
            service.putExtra(MovieService.REFRESH_DATA, refresh);
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
        mSortPreference = mPrefs.getString(ACTIVITY_SORT_PREFERENCE, MovieService.SORT_BY_POPULARITY);
        if(mSortPreference.equals(MovieService.SORT_BY_POPULARITY)) {
            menu.findItem(R.id.menuPopularity).setChecked(true);
        } else {
            menu.findItem(R.id.menuRated).setChecked(true);
        }
        mIncludeFavorites = mPrefs.getBoolean(INCLUDE_FAVS_PREFERENCE, false);
        menu.findItem(R.id.includeFavorites).setChecked(mIncludeFavorites);
        mOptions = menu;
        SetMenuTitle();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mOptions = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Note: according to http://developer.android.com/guide/topics/ui/menus.html you can't have a checkable group in the options menu,
        //so we manage the menu state ourselves
        int id = item.getItemId();
        switch(id) {
            case R.id.menuPopularity:
                Log.i(TAG, "sorting by popularity");
                mSortPreference = MovieService.SORT_BY_POPULARITY;
                item.setChecked(true);
                mOptions.findItem(R.id.menuRated).setChecked(false);
                Log.d(TAG, "starting movie service from POPULARITY");
                StartMovieService(true);
                break;
            case R.id.menuRated:
                Log.i(TAG, "sorting by rated");
                mSortPreference = MovieService.SORT_BY_USER_RATING;
                item.setChecked(true);
                mOptions.findItem(R.id.menuPopularity).setChecked(false);
                Log.d(TAG, "starting movie service from RATINGS");
                StartMovieService(true);
                break;
        }
        mPrefs.edit().putString(ACTIVITY_SORT_PREFERENCE, mSortPreference).commit();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @SuppressLint("CommitPrefEdits")
    public void onClickFavorite(MenuItem item) {
        mIncludeFavorites = !item.isChecked();
        item.setChecked(mIncludeFavorites);
        mPrefs.edit().putBoolean(INCLUDE_FAVS_PREFERENCE,mIncludeFavorites).commit();
        Log.d(TAG, "starting movie service from FAVORITES");
        StartMovieService(true);
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

                if(mTablet) {
                    if( findViewById(R.id.empty_text_view) != null) {
                        TextView text =  (TextView)findViewById(R.id.empty_text_view);
                        text.setVisibility(View.VISIBLE);
                        ImageView picture = (ImageView)findViewById(R.id.empty_imageView);
                        picture.setVisibility(View.VISIBLE);
                    }
                }
                //noinspection unchecked
                mMovies = new Gson().fromJson(json, new TypeToken<List<ExtendedMovie>>(){}.getType());
                SetMenuTitle();
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

    private void SetMenuTitle() {
        String title = (mSortPreference.equals(MovieService.SORT_BY_POPULARITY) ? getString(R.string.most_popular) : getString(R.string.highest_rated));
        if(mOptions != null) {
            mOptions.findItem(R.id.action_settings).setTitle(title);
        }
        Log.d(TAG, "TITLE: " + title);
    }

}


