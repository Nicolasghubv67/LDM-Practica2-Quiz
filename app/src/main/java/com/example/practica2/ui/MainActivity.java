package com.example.practica2.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.practica2.R;
import com.example.practica2.QuizApplication;
import com.example.practica2.media.SoundPlayer;

public class MainActivity extends BaseActivity {

    private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        soundPlayer = ((QuizApplication) getApplication()).getSoundPlayer();

        LottieAnimationView btnStart = findViewById(R.id.btnStart);
        btnStart.enableMergePathsForKitKatAndAbove(true);

        btnStart.setOnClickListener(v -> {
            soundPlayer.playClick();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        viewModel.reset();
    }

}