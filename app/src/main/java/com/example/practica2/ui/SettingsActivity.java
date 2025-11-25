package com.example.practica2.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.AppPreferences;
import com.example.practica2.QuizApplication;
import com.example.practica2.R;
import com.example.practica2.media.MusicPlayer;
import com.example.practica2.media.SoundPlayer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends BaseActivity {

    private SoundPlayer soundPlayer;
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        QuizApplication app = (QuizApplication) getApplication();
        soundPlayer = app.getSoundPlayer();
        musicPlayer = app.getMusicPlayer();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        MaterialSwitch switchSound = findViewById(R.id.switchSound);
        MaterialSwitch switchMusic = findViewById(R.id.switchMusic);

        // Estado inicial
        boolean soundEnabled = AppPreferences.isSoundEnabled(this);
        boolean musicEnabled = AppPreferences.isMusicEnabled(this);
        switchSound.setChecked(soundEnabled);
        switchMusic.setChecked(musicEnabled);

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.setSoundEnabled(this, isChecked);
            soundPlayer.setSoundEnabled(isChecked);
        });

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.setMusicEnabled(this, isChecked);
            musicPlayer.setEnabled(isChecked);
        });
    }
}
