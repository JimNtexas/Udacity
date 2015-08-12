package com.grayraven.project1;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class DetailsFragment extends android.support.v4.app.Fragment {
    public static String TAG = "MovieDetailsFragment";
    private String mJson;

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "Inflating detail fragment");
        View rootView;
        Bundle args = getArguments();
        if(args != null) {
            rootView = inflater.inflate(R.layout.activity_details_view, container, false);
            TextView titleView  = (TextView) rootView.findViewById(R.id.title);
            titleView.setText((String) args.get(DetailsActivity.MOVIE_TITLE));

            String imageUrl = (String) args.get(DetailsActivity.MOVIE_URL);
            ImageView posterView = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getActivity()).load(imageUrl)
                    .placeholder(R.drawable.whiteposter)
                    .into(posterView);

            String rating = (String) args.get(DetailsActivity.MOVIE_RATING) + " / 10.0";
            TextView ratingTextView = (TextView) rootView.findViewById(R.id.rating);
            ratingTextView.setText(Html.fromHtml(rating));

            String release = (String) args.get(DetailsActivity.MOVIE_RELEASE_DATE);
            TextView releaseDateView = (TextView) rootView.findViewById(R.id.release_date);
            releaseDateView.setText(Html.fromHtml(MovieUtils.formatMediumDate(release)));

            String plot = (String) args.get(DetailsActivity.MOVIE_PLOT);
            TextView   plotTextView = (TextView) rootView.findViewById(R.id.plot);
            plotTextView.setText(Html.fromHtml(plot));

            mJson = (String) args.get(DetailsActivity.MOVIE_TRAILER_JSON);
            List<Video> trailers  = new Gson().fromJson(mJson, new TypeToken<List<Video>>() {
            }.getType());

            Button btnTrailers = (Button) rootView.findViewById(R.id.button_trailers);
            btnTrailers.setVisibility((trailers != null && trailers.size() > 0) ? View.VISIBLE : View.INVISIBLE);
            btnTrailers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTrailerDialog(mJson);
                }
            });



        } else {
            rootView = inflater.inflate(R.layout.empty_detail_view, container, false);
        }


        return rootView;
    }

    private void showTrailerDialog(String json) {
        TrailerDialogFragment df = new TrailerDialogFragment().newInstance(json);
        android.support.v4.app.Fragment fr = getActivity().getSupportFragmentManager().findFragmentByTag(TrailerDialogFragment.TAG);
        if (fr == null) {
            df.show(getActivity().getSupportFragmentManager(), TrailerDialogFragment.TAG);
        }

    }






}
