package com.example.practica2.media;

public interface MusicPlayer {
    void start();
    void pause();
    void stop();
    void setEnabled(boolean enabled);
    boolean isEnabled();
}
