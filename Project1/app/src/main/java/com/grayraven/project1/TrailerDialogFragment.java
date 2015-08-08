package com.grayraven.project1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class TrailerDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    static final String TAG = "MovieTrailerDlg";
    static String mJson;
    ListView mTrailerList = null;
    List<Video> mVideos = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.trailer_dialog_fragment, null, false);
        mTrailerList = (ListView)v.findViewById(R.id.trailer_list);
        mVideos  = new Gson().fromJson(mJson, new TypeToken<List<Video>>() {
        }.getType());

        getDialog().setTitle("Select a Trailer to View");
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
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override //recommended as a framework bug workaround
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

}