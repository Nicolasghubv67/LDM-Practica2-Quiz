package com.example.practica2.media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.practica2.R;

/**
 * Implementación concreta de SoundPlayer basada en SoundPool.
 * SRP: sólo gestiona sonidos (cargar, reproducir, liberar).
 */
public class SoundManager implements SoundPlayer {

    private final SoundPool soundPool;
    private final int soundCorrectId;
    private final int soundWrongId;
    private final int soundClickId;

    private boolean soundEnabled = true;

    public SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        soundCorrectId = soundPool.load(context, R.raw.correct, 1);
        soundWrongId = soundPool.load(context, R.raw.wrong, 1);
        soundClickId = soundPool.load(context, R.raw.click, 1);
    }

    private void play(int soundId) {
        if (!soundEnabled) return;
        if (soundId != 0) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    @Override
    public void playCorrect() {
        play(soundCorrectId);
    }

    @Override
    public void playWrong() {
        play(soundWrongId);
    }

    @Override
    public void playClick() {
        play(soundClickId);
    }

    @Override
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    @Override
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void release() {
        soundPool.release();
    }
}
