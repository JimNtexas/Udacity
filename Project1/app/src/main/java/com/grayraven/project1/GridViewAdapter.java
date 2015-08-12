package com.grayraven.project1;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;

class GridViewAdapter extends ArrayAdapter<ExtendedMovie> {

    private static final String TAG = "MovieGridView";
    private final Context mContext;
    private final int layoutResourceId;
    private List<ExtendedMovie> mMovieData;

    public GridViewAdapter(Context mContext, List<ExtendedMovie> mMovieData) {
        super(mContext, R.layout.grid_item_layout, mMovieData);
        this.layoutResourceId = R.layout.grid_item_layout;
        this.mContext = mContext;
        this.mMovieData = mMovieData;
    }

    @Override
    public int getCount() {
        return mMovieData.size();
    }

    /**
     * Updates grid data and refresh grid items.
     */
    public void setGridData(List<ExtendedMovie> movieData) {
        mMovieData = movieData;
        Log.i(TAG, "Grid data count: " + movieData.size());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        MovieDb movie =  mMovieData.get(position).getMovie();
        holder.titleTextView.setText(Html.fromHtml(movie.getOriginalTitle()));

        String path = MovieService.getPosterUrl(MovieService.POSTER_SIZE_STANDARD,movie.getPosterPath());
        Picasso.with(mContext).load(path).into(holder.imageView);

        path = MovieService.getPosterUrl(MovieService.POSTER_SIZE_SMALL,movie.getPosterPath());
        Picasso.with(mContext).load(path).fetch();  // cache the small posters here to avoid flicker when viewing movie details


        return row;
    }

    MovieDb getMovie(int position) {
        return mMovieData.get(position).getMovie();
    }

    public List<Video> getTrailers(int position) {
        return mMovieData.get(position).getTrailers();
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
