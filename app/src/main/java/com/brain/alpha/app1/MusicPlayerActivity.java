package com.brain.alpha.app1;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.brain.alpha.app1.domain.DataService;
import com.brain.alpha.app1.domain.Track;
import com.brain.alpha.app1.service.MusicService.MusicBinder;


import android.content.Intent;
import android.widget.MediaController;
import android.widget.TextView;

import com.brain.alpha.app1.service.MusicService;

public class MusicPlayerActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    String audioFile;


    public void play(View v){
        musicSrv.playSong(audioFile);
        System.out.println("playIntent = " + playIntent);


    }
    //service
    private MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;

    //controller
    private MusicController controller;

    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String trackUri = intent.getStringExtra(TrackListActivity.TRACK_URI);
        Track track = DataService.getRemoteTrack(trackUri);
        //todo: chagneto media
        audioFile = track.getUriLinkTo("track");
        Log.i("audioFile ", audioFile);
        ((TextView) findViewById(R.id.playerTrackName)).setText(track.getName());
        ((TextView)findViewById(R.id.playerDescription)).setText(track.getDescription());
        ((TextView)findViewById(R.id.duration)).setText(getTimeString(track.getLength()));


    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
            setController();
            play(null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private String getTimeString(long secs) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (secs / (1* 60 * 60));
        int minutes = (int) ((secs % (1 * 60 * 60)) / (1 * 60));
        int seconds = (int) (((secs % (1 * 60 * 60)) % (1 * 60)));

        buf.append(String.format("%02d", hours)).append(":").append(String.format("%02d", minutes))
                .append(":").append(String.format("%02d", seconds));

        return buf.toString();
    }


    //--- media controller methods

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    //set the controller up
    private void setController(){
        controller = new MusicController(this);
        //set previous and next button listeners

        //set and show
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.mediaController));
        controller.setEnabled(true);
    }



    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
}
