package com.example.practica2.media;

public interface SoundPlayer {

    void playCorrect();

    void playWrong();

    void playClick();

    void setSoundEnabled(boolean enabled);

    boolean isSoundEnabled();
}
