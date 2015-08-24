package com.grayraven.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class TrailerDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    static final String TAG = "MovieTrailerDlg";
    private static String mJson;
    private List<Video> mVideos = null;

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if(textView != null)
        {
            textView.setText("View Trailer");
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.movie_light_text));
            textView.setBackgroundColor(getResources().getColor(R.color.movie_blue_background));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.trailer_dialog_fragment, null, false);
        ListView mTrailerList = (ListView) v.findViewById(R.id.trailer_list);
        mVideos  = new Gson().fromJson(mJson, new TypeToken<List<Video>>() {
        }.getType());

        TrailerAdapter adapter = new TrailerAdapter(getActivity(),mVideos);
        mTrailerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mTrailerList.setOnItemClickListener(this);
        return v;
    }

    public static TrailerDialogFragment newInstance(String json) {
        TrailerDialogFragment trailerFragment = new TrailerDialogFragment();
        Bundle args = new Bundle();
        args.putString(DetailsActivity.MOVIE_TRAILER_JSON, json);
        trailerFragment.setArguments(args);
        mJson = json;
        return trailerFragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "Trailer number " + i + " selected");
        Video vid = mVideos.get(i);
        Log.i(TAG, "Trailer name: " + vid.getName());
        Log.i(TAG, "Trailer  key: " + vid.getKey());
        Log.i(TAG, "Trailer site: " + vid.getSite());
        Log.i(TAG, "Trailer type: " + vid.getType());

        Log.i(TAG, "show movie");
        if(vid.getSite().equals("YouTube") ) {
            String url = "http://www.youtube.com/watch?v=" + vid.getKey();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            Toast.makeText(getActivity(),"Unfortunately this video type is not yet supported: " + vid.getSite(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override //recommended as a framework bug workaround
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

}