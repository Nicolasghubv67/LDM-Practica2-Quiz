package com.example.practica2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.practica2.R;
import com.example.practica2.media.SoundManager;
import com.example.practica2.QuizApplication;
import com.example.practica2.media.SoundPlayer;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private SoundPlayer soundPlayer;

    private ImageButton btnSettings;
    private ImageButton btnHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        soundPlayer = ((QuizApplication) getApplication()).getSoundPlayer();

        // Toolbar como ActionBar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);
        }

        LottieAnimationView btnStart = findViewById(R.id.btnStart);
        btnStart.enableMergePathsForKitKatAndAbove(true);

        btnStart.setOnClickListener(v -> {
            soundPlayer.playClick();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnSettings = findViewById(R.id.btnSettings);
        btnHelp = findViewById(R.id.btnHelp);

        btnSettings.setOnClickListener(v -> {
            soundPlayer.playClick();
            startActivity(new Intent(this, SettingsActivity.class));
        });

        btnHelp.setOnClickListener(v -> {
            soundPlayer.playClick();
            startActivity(new Intent(this, HelpActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        viewModel.reset();
    }




}