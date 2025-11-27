package com.example.practica2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.practica2.QuizApplication;
import com.example.practica2.R;
import com.example.practica2.media.SoundPlayer;

public class MainActivity extends BaseActivity {

    private static final int MIN_QUESTIONS = 3;
    private static final int MAX_QUESTIONS = 10;
    private static final int DEFAULT_QUESTIONS = 5;

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
            showQuestionCountDialog();
        });
    }

    private void showQuestionCountDialog() {
        android.view.View dialogView =
                getLayoutInflater().inflate(R.layout.dialog_question_count, null);

        TextView tvValue = dialogView.findViewById(R.id.tvQuestionCountValue);
        com.google.android.material.slider.Slider slider =
                dialogView.findViewById(R.id.sliderQuestionCount);

        slider.setValueFrom(MIN_QUESTIONS);
        slider.setValueTo(MAX_QUESTIONS);
        slider.setStepSize(1f);
        slider.setValue(DEFAULT_QUESTIONS);
        tvValue.setText(String.valueOf(DEFAULT_QUESTIONS));

        slider.addOnChangeListener((s, value, fromUser) ->
                tvValue.setText(String.valueOf((int) value)));

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton(R.string.empezar, (dialog, which) -> {
                    int count = (int) slider.getValue();
                    startGame(count);
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void startGame(int questionCount) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("QUESTION_COUNT", questionCount);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        viewModel.reset();
    }
}
