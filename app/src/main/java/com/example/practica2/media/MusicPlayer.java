package com.example.practica2.media;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.RawRes;

public class MusicPlayer {

    private final Context appContext;
    private final int musicResId;

    private MediaPlayer mediaPlayer;

    public MusicPlayer(Context context, @RawRes int musicResId) {
        this.appContext = context.getApplicationContext();
        this.musicResId = musicResId;
    }

    private void ensurePlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(appContext, musicResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
            }
        }
    }

    public void start() {
        ensurePlayer();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}
