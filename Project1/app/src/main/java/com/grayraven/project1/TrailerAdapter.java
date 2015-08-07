package com.grayraven.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

/**
 * Created by jhoward on 8/5/2015.
 */
public class TrailerAdapter extends BaseAdapter {

    private List<Video> mVideos;

    private final Context mContext;
    private LayoutInflater mLayoutInflater = null;

    public TrailerAdapter(Context mContext, List<Video> videos) {
        this.mContext = mContext;
        this.mVideos = videos;
    }

    public void updateTrailers(List<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        return mVideos.size();
    }

    @Override
    public Video getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
         final TextView nameView;
         final TextView typeView;

        if(view == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.trailer_item_layout,null);

            nameView = (TextView) view.findViewById(R.id.trailer_name);
            typeView = (TextView) view.findViewById(R.id.trailer_type);
            view.setTag(new ViewHolder(nameView,typeView));
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            nameView = holder.nameView;
            typeView = holder.typeView;
        }

        Video video = getItem(position);
        nameView.setText(video.getName());
        typeView.setText(video.getType());
        return view;
    }

    private static class ViewHolder {
        public final TextView nameView;
        public final TextView typeView;

        public ViewHolder(TextView name, TextView type) {
            this.nameView = name;
            this.typeView = type;
        }
    }
}
