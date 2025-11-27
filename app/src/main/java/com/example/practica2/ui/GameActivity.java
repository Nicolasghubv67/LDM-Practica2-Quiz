package com.example.practica2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.practica2.R;
import com.example.practica2.repository.GameRepository;

public class GameActivity extends BaseActivity {

    public static final String EXTRA_QUESTION_COUNT = "QUESTION_COUNT";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private GameViewModel viewModel;
    private TextView score;
    private Button btnNext;
    private Runnable hideRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        int questionCount = getIntent().getIntExtra(EXTRA_QUESTION_COUNT, 5);

        // Cargar preguntas aleatorias desde Room
        GameRepository repository = new GameRepository(this);
        repository.getRandomQuestionsAsync(questionCount, loadedQuestions ->
                        runOnUiThread(() -> viewModel.setQuestionsFromDb(loadedQuestions))
        );

        score   = findViewById(R.id.tvScore);
        btnNext = findViewById(R.id.btnNext);
        Button btnBack = findViewById(R.id.btnBack);

        viewModel.getScore().observe(this, s -> score.setText(String.valueOf(s)));
        viewModel.getValidated().observe(this, v -> updateNextButton());
        viewModel.getCurrentIndexLive().observe(this, idx -> updateNextButton());

        btnNext.setOnClickListener(v -> {
            boolean last = viewModel.isLastQuestion();
            if (last) {
                Intent intent = new Intent(GameActivity.this, ResultsActivity.class);

                int finalScore = viewModel.getScore().getValue() == null
                        ? 0
                        : viewModel.getScore().getValue();

                int correct = viewModel.getCorrectAnswersValue();
                int wrong   = viewModel.getWrongAnswersValue();

                intent.putExtra("FINAL_SCORE", finalScore);
                intent.putExtra("TOTAL_QUESTIONS", viewModel.getTotal());
                intent.putExtra("CORRECT_ANSWERS", correct);
                intent.putExtra("WRONG_ANSWERS", wrong);

                startActivity(intent);
                finish();
            } else {
                viewModel.nextQuestion();
            }
        });


        btnBack.setOnClickListener(v -> showExitAlert());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { showExitAlert(); }
        });
    }

    public void showFeedback(String message, boolean isCorrect) {
        TextView tvFeedback = findViewById(R.id.tvFeedback);
        if (tvFeedback == null) return;

        tvFeedback.animate().cancel();
        if (hideRunnable != null) handler.removeCallbacks(hideRunnable);

        tvFeedback.setText(message);
        tvFeedback.setTextColor(ContextCompat.getColor(
                this, isCorrect ? R.color.green_correct : R.color.red_incorrect));

        tvFeedback.setTranslationY(-8f);
        tvFeedback.animate().translationY(0f).setDuration(180).start();

        tvFeedback.setAlpha(0f);
        tvFeedback.setVisibility(View.VISIBLE);
        tvFeedback.animate()
                .alpha(1f)
                .setDuration(180)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        hideRunnable = () -> tvFeedback.animate()
                .alpha(0f)
                .setDuration(180)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> tvFeedback.setVisibility(View.GONE))
                .start();

        handler.postDelayed(hideRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hideRunnable != null) handler.removeCallbacks(hideRunnable);
    }

    private void updateNextButton() {
        boolean enable = Boolean.TRUE.equals(viewModel.getValidated().getValue());
        btnNext.setEnabled(enable);

        boolean last = viewModel.getTotal() > 0 &&
                viewModel.getCurrentIndex() == viewModel.getTotal() - 1;
        btnNext.setText(last ? R.string.finalizar : R.string.siguiente);
    }

    private void showExitAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.salir_del_quiz)
                .setMessage(R.string.aviso_perder_progreso)
                .setPositiveButton(R.string.si, (DialogInterface dialog, int which) -> finish())
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }
}
