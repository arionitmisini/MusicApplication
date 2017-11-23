package com.example.arioniti.musicapplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class Player extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    static MediaPlayer mp;
    Thread updateSeekBar;
    Uri u;
    //TextView musicName;
    int position;
    ArrayList<File> mySongs;
    SeekBar sb,volume;
    AudioManager audio;
    //Button btnPlay,btnFF,btnNext,btnFB,btnPrevious;
    ImageButton playButoni, nextButoni, ffButoni, bfButoni, backButoni,PauseButon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        /******************Seek Min**********************/
        sb=(SeekBar)findViewById(R.id.seekMinutes);
        updateSeekBar=new Thread(){
            public void run(){
                int totalDuration=mp.getDuration();
                int currentPosition=0;
                sb.setMax(totalDuration);
                while(mp!=null&&currentPosition<totalDuration){
                    try{
                        Thread.sleep(500);
                        currentPosition=mp.getCurrentPosition();


                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    sb.setProgress(currentPosition);
                }

            }


        };

        /************************************************/
        /* btnPlay=(Button)findViewById(R.id.btnPlay);
        btnFF=(Button)findViewById(R.id.btnFF);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnFB=(Button)findViewById(R.id.btnBF);
        btnPrevious=(Button)findViewById(R.id.btnPrevious);

        btnPlay.setOnClickListener(this);
        btnFF.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);*/


        playButoni = (ImageButton) findViewById(R.id.playButoni);
        ffButoni = (ImageButton) findViewById(R.id.ffButoni);
        nextButoni = (ImageButton) findViewById(R.id.nextButoni);
        bfButoni = (ImageButton) findViewById(R.id.bfButoni);
        backButoni = (ImageButton) findViewById(R.id.backButoni);
        PauseButon = (ImageButton) findViewById(R.id.PauseButon);

        playButoni.setOnClickListener(this);
        ffButoni.setOnClickListener(this);
        nextButoni.setOnClickListener(this);
        bfButoni.setOnClickListener(this);
        backButoni.setOnClickListener(this);
        PauseButon.setOnClickListener(this);

        /*************MPSTOP**********/
        /*if(mp!=null){
            mp.stop();
            mp.release();
        }*/
        /****************************/


        Intent i=getIntent();
        Bundle b = i.getExtras();
        mySongs=(ArrayList) b.getParcelableArrayList("listaMuzikav");
        position=b.getInt("pos",0);
        u= Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();

        /*********************Seek Volume***************************/
        volume=(SeekBar)findViewById(R.id.seekBar);
        audio=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume=audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume.setMax(maxVolume);
        volume.setProgress(currentVolume);
        volume.setOnSeekBarChangeListener(this);
        /***********************************************************/
        musicName();
        /***********************SEEKMINUTES**********************/
         sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mp.seekTo(seekBar.getProgress());
            }
        });
        /*******************************************************/
        updateSeekBar.start();
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.playButoni:
                mp.start();
                playButoni.setVisibility(View.INVISIBLE);
                PauseButon.setVisibility(View.VISIBLE);
                break;
            case R.id.PauseButon:
                mp.pause();
                PauseButon.setVisibility(View.INVISIBLE);
                playButoni.setVisibility(View.VISIBLE);
                break;
            case R.id.ffButoni:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.bfButoni:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.nextButoni:
                if(!mp.isPlaying()){
                    playButoni.setVisibility(View.INVISIBLE);
                    PauseButon.setVisibility(View.VISIBLE);
                }
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                musicName();
                break;
            case R.id.backButoni:
                if(!mp.isPlaying()){
                    playButoni.setVisibility(View.INVISIBLE);
                    PauseButon.setVisibility(View.VISIBLE);
                }
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                musicName();
                break;

        }

    }

   /* public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.btnPlay:
                if(mp.isPlaying()){
                    btnPlay.setText(">");
                    mp.pause();
                }
                else{
                    btnPlay.setText("II");
                    mp.start();
                }
                break;
            case R.id.btnFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btnBF:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btnNext:
                mp.stop();
                mp.release();
                u= Uri.parse(mySongs.get((position+1)%mySongs.size()).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;
            case R.id.btnPrevious:
                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u= Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;

        }

    }*/
    @Override
    //SeekBar Volume Control
    public void onProgressChanged(SeekBar arg0, int music_volume, boolean arg2) {
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,music_volume,0);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void musicName(){
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(mySongs.get(position).getName().replace(".mp3","").replace(".wav",""));
    }





}
