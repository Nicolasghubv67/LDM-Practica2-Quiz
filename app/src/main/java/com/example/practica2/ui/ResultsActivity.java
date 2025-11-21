package com.example.practica2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.R;
import com.example.practica2.data.AppDatabase;
import com.example.practica2.data.GameResult;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        db = AppDatabase.getInstance(this);

        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        int correct = getIntent().getIntExtra("correctAnswers", 0);
        int wrong = getIntent().getIntExtra("wrongAnswers", 0);

        GameResult result = new GameResult(
                System.currentTimeMillis(),
                score,
                totalQuestions,
                correct,
                wrong
        );

        // MUY IMPORTANTE: Room SIEMPRE en hilo de fondo
        new Thread(() -> {
            db.gameResultDao().insert(result);

            // Prueba rápida: leer cuántas partidas hay y enseñarlo en un Toast
            List<GameResult> all = db.gameResultDao().getAllOrdered();
            runOnUiThread(() -> Toast.makeText(
                    ResultsActivity.this,
                    "Partidas guardadas: " + all.size(),
                    Toast.LENGTH_SHORT
            ).show());
        }).start();

        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        Button btnBackToMain = findViewById(R.id.btnBackToMain);

        // Recuperar la puntuación desde el Intent
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        tvFinalScore.setText("Puntuación final: " + finalScore);

        // Botón para volver a la pantalla principal
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // cierra ResultsActivity
        });
    }
}
