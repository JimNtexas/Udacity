package com.grayraven.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class TrailerDialogFragment extends DialogFragment {

    static final String TAG = "MovieTrailerDlg";
    static String mJson;


    public TrailerDialogFragment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.trailer_dialog_fragment, null);
        List<Video> trailers  = new Gson().fromJson(mJson, new TypeToken<List<Video>>() {
        }.getType());
     //   TrailerAdapter adapter = new TrailerAdapter(getActivity(), trailers);
      //  mListview.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Trailer Dlg");
        return builder.create();
    }



    public static TrailerDialogFragment newInstance(String json) {
        TrailerDialogFragment trailerFragment = new TrailerDialogFragment();
        Bundle args = new Bundle();
        args.putString(DetailsActivity.MOVIE_TRAILER_JSON, json);
        trailerFragment.setArguments(args);
        mJson = json;
        return trailerFragment;
    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "Trailer Dlg View Created ======================================");
        View rootView = inflater.inflate(R.layout.trailer_dialog_fragment, container,
                false);
        List<Video> trailers  = new Gson().fromJson(mJson, new TypeToken<List<Video>>() {}.getType());
        TrailerAdapter adapter = new TrailerAdapter(getActivity(), trailers);
        ListView trailerList = (ListView) this.getActivity().findViewById(R.id.trailer_list);
        trailerList.setAdapter(adapter);
     getDialog().setTitle("Show Trailers");

        String json = getArguments().getString(DetailsActivity.MOVIE_TRAILER_JSON);
        List<Video> trailers  = new Gson().fromJson(json, new TypeToken<List<Video>>() {
        }.getType());
        TrailerAdapter adapter = new TrailerAdapter(getActivity(), trailers);
        ListView trailerList = (ListView) this.getActivity().findViewById(R.id.trailer_list);
        trailerList.setAdapter(adapter);
        return rootView;
    } */

}