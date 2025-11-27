package com.example.practica2.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.SystemClock;

import com.example.practica2.AppPreferences;
import com.example.practica2.R;

public class SoundPlayer {

    private final Context appContext;
    private final SoundPool soundPool;
    private final int soundCorrectId;
    private final int soundWrongId;

    private static final long COOLDOWN_MS = 150;
    private long lastCorrectTime = 0L;
    private long lastWrongTime = 0L;

    public SoundPlayer(Context context) {
        this.appContext = context.getApplicationContext();

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(4)
                .build();

        soundCorrectId = soundPool.load(appContext, R.raw.correct, 1);
        soundWrongId   = soundPool.load(appContext, R.raw.wrong, 1);
    }

    private boolean isSoundEnabled() {
        return !AppPreferences.isSoundEnabled(appContext);
    }

    public void playCorrect() {
        if (isSoundEnabled()) return;

        long now = SystemClock.uptimeMillis();
        if (now - lastCorrectTime < COOLDOWN_MS) return;
        lastCorrectTime = now;

        soundPool.play(soundCorrectId, 1f, 1f, 1, 0, 1f);
    }

    public void playWrong() {
        if (isSoundEnabled()) return;

        long now = SystemClock.uptimeMillis();
        if (now - lastWrongTime < COOLDOWN_MS) return;
        lastWrongTime = now;

        soundPool.play(soundWrongId, 1f, 1f, 1, 0, 1f);
    }

    public void playClick() {
        if (isSoundEnabled()) return;
        soundPool.play(soundCorrectId, 1f, 1f, 1, 0, 1f);
    }
}
