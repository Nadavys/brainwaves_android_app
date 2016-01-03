package com.brain.alpha.app1.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brain.alpha.app1.R;
import com.brain.alpha.app1.service.UrlImageView;

import java.net.URL;
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
                    .inflate(R.layout.category_list_template, parent, false);
        }

        TrackCategory category = list.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(category.getName());
        ((TextView)convertView.findViewById(R.id.description)).setText(category.getDescription());

//        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
//        myImageView.setImageBitmap(bitmap);

        ((UrlImageView)convertView.findViewById(R.id.catImageView)).setImageURL(category.getThumbnail());


        return convertView;
    }
}
