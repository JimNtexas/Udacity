package com.grayraven.project1;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsFragment extends android.support.v4.app.Fragment {
    public static String TAG = "MovieDetailsFragment";
  /*  private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mRatingView;
    private TextView mReleaseView;
    private TextView mPlotView;
*/

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


        } else {
            rootView = inflater.inflate(R.layout.empty_detail_view, container, false);
        }


        return rootView;
    }






}
