package com.example.practica2;

import android.app.Application;

import com.example.practica2.media.MusicManager;
import com.example.practica2.media.MusicPlayer;
import com.example.practica2.media.SoundManager;
import com.example.practica2.media.SoundPlayer;

public class QuizApplication extends Application {

    private SoundPlayer soundPlayer;
    private MusicPlayer musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        soundPlayer = new SoundManager(this);
        musicPlayer = new MusicManager(this);

        // Sincronizar estados con SharedPreferences
        boolean soundEnabled = AppPreferences.isSoundEnabled(this);
        boolean musicEnabled = AppPreferences.isMusicEnabled(this);

        soundPlayer.setSoundEnabled(soundEnabled);
        musicPlayer.setEnabled(musicEnabled);
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (musicPlayer instanceof MusicManager) {
            musicPlayer.stop();
        }
    }
}
