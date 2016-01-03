package com.brain.alpha.app1.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brain.alpha.app1.R;

import java.util.List;
 
public class TrackListAdapter extends ArrayAdapter<Track>{
    private List<Track> trackList;
    public TrackListAdapter(Context context, int resource, List<Track> objects) {
        super(context, resource, objects);
        this.trackList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.track_list_template, parent, false);
        }

        Track track = trackList.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(track.getName());


        return convertView;
    }
}
