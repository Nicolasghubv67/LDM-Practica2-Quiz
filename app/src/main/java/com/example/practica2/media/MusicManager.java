package com.example.practica2.media;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.practica2.R;

public class MusicManager implements MusicPlayer {

    private final Context context;
    private MediaPlayer mediaPlayer;
    private boolean enabled = true;

    public MusicManager(Context context) {
        this.context = context.getApplicationContext();
        initPlayer();
    }

    private void initPlayer() {
        //mediaPlayer = MediaPlayer.create(context, R.raw.bg_music); // crea bg_music.mp3 en res/raw
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public void start() {
        if (!enabled || mediaPlayer == null) return;
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
