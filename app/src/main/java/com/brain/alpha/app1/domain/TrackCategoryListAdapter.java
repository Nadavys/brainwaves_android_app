package com.brain.alpha.app1.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brain.alpha.app1.R;

import java.util.List;

public class TrackCategoryListAdapter extends ArrayAdapter<TrackCategory>{
    private List<TrackCategory> list;
    public TrackCategoryListAdapter(Context context, int resource, List<TrackCategory> objects) {
        super(context, resource, objects);
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.track_list_template, parent, false);
        }

        TrackCategory category = list.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(category.getName());
        ((TextView)convertView.findViewById(R.id.description)).setText(category.getDescription());


        return convertView;
    }
}
