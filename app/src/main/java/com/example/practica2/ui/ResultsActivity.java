package com.example.practica2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.R;
import com.example.practica2.data.GameResult;

public class ResultsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ResultsViewModel viewModel = new ViewModelProvider(this).get(ResultsViewModel.class);

        // -------- Resumen de la partida actual --------
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        int correct = getIntent().getIntExtra("CORRECT_ANSWERS", 0);
        int wrong = getIntent().getIntExtra("WRONG_ANSWERS", 0);

        GameResult result = new GameResult(
                System.currentTimeMillis(),
                finalScore,
                totalQuestions,
                correct,
                wrong
        );
        viewModel.insert(result);

        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvTotalQuestions = findViewById(R.id.tvTotalQuestionsValue);
        TextView tvCorrectAnswers = findViewById(R.id.tvCorrectAnswersValue);
        TextView tvWrongAnswers = findViewById(R.id.tvWrongAnswersValue);
        Button btnBackToMain = findViewById(R.id.btnBackToMain);

        tvFinalScore.setText(getString(R.string.final_score_label, finalScore));
        tvTotalQuestions.setText(String.valueOf(totalQuestions));
        tvCorrectAnswers.setText(String.valueOf(correct));
        tvWrongAnswers.setText(String.valueOf(wrong));

        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // -------- Tabla de resultados históricos --------
        RecyclerView rvResults = findViewById(R.id.rvResults);
        GameResultAdapter adapter = new GameResultAdapter();
        rvResults.setLayoutManager(new LinearLayoutManager(this));
        rvResults.setAdapter(adapter);

        viewModel.getResults().observe(this, adapter::submitList);
    }
}