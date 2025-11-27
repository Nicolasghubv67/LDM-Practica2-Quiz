package com.example.practica2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.practica2.media.MusicPlayer;
import com.example.practica2.media.SoundPlayer;

public class QuizApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private MusicPlayer musicPlayer;
    private SoundPlayer soundPlayer;
    private int startedActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

        musicPlayer = new MusicPlayer(this, R.raw.bg_music);
        soundPlayer = new SoundPlayer(this);
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    // -------- ActivityLifecycleCallbacks --------

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        startedActivities++;

        // Si la app pasa de 0 -> 1 activities, se considera foreground
        if (startedActivities == 1 && AppPreferences.isMusicEnabled(activity)) {
            musicPlayer.start();
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        startedActivities--;

        if (startedActivities == 0) {
            // La app ya no tiene ninguna Activity visible → pausa música
            musicPlayer.pause();
        }
    }

    @Override public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityResumed(@NonNull Activity activity) {}
    @Override public void onActivityPaused(@NonNull Activity activity) {}
    @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
    @Override public void onActivityDestroyed(@NonNull Activity activity) {}
}
