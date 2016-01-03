package com.brain.alpha.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.brain.alpha.app1.domain.DataService;
import com.brain.alpha.app1.domain.Track;
import com.brain.alpha.app1.domain.TrackCategory;
import com.brain.alpha.app1.domain.TrackListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrackListActivity extends AppCompatActivity {
    public static final String TRACK_ID = "TRACK_ID";
    public static final String TRACK_URI = "TRACK_URI";
    List<TrackCategory> categoryList = DataService.categoryList;
    List<Track> trackList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String category = intent.getStringExtra(MainActivity.TRACK_CATEGORY);

        TextView title = (TextView)findViewById(R.id.categoryName);
        title.setText(category);

        TrackCategory trackCategory = null;
        for(TrackCategory tc : categoryList){
            if(tc.getName().toLowerCase().equals(category.toLowerCase())){
                trackCategory = tc;
                break;
            }
        }

        //todo: if track null error

        trackList = DataService.getRemoteTrackList(trackCategory);

        TrackListAdapter adapter = new TrackListAdapter(this, R.layout.track_list_template, trackList);
        ListView listView = (ListView) findViewById(R.id.listTrackView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = trackList.get(position);
                Intent intent = new Intent(TrackListActivity.this, MusicPlayerActivity.class);
                intent.putExtra(TRACK_ID, track.getId());
                intent.putExtra(TRACK_URI, track.getUriLinkTo("self"));
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
