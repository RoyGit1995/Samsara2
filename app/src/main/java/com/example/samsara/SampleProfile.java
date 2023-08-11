package com.example.samsara;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SampleProfile extends AppCompatActivity {

    private VideoView videoView;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerSecond;
    private Button playPauseButton;
    private Button playPauseButtonSecond;
    private Button createProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        videoView = findViewById(R.id.videoView);
        // Load video from the drawable folder
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoplayback_italy);
        videoView.setVideoURI(videoUri);
        // Add media controller for play, pause, etc.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // The video is prepared, but don't start playing automatically
                // You can add a play button or handle playback as needed
            }
        });


        playPauseButton = findViewById(R.id.playpause);
        mediaPlayer = MediaPlayer.create(this, R.raw.jenniferaudiomessage);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseButton.setText("Play");
            }
        });

        playPauseButtonSecond = findViewById(R.id.playpauseSecond);
        mediaPlayerSecond = MediaPlayer.create(this, R.raw.jenniferaudioseond);

        mediaPlayerSecond.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseButtonSecond.setText("Play");
            }
        });

        createProfileButton = findViewById(R.id.createYourProfile);
        createProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SampleProfile.this, CreateYourProfile.class);
            startActivity(intent);
        });
    }







    public void onPlayPauseButtonClick(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setText("Play");
        } else {
            mediaPlayer.start();
            playPauseButton.setText("Pause");
        }
    }

    public void onPlayPauseButtonClickSecond(View view) {
        if (mediaPlayerSecond.isPlaying()) {
            mediaPlayerSecond.pause();
            playPauseButtonSecond.setText("Play");
        } else {
            mediaPlayerSecond.start();
            playPauseButtonSecond.setText("Pause");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaPlayerSecond != null) {
            mediaPlayerSecond.release();
            mediaPlayerSecond = null;
        }
    }
}
