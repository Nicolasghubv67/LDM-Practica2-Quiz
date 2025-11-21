package com.example.practica2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.practica2.AppPreferences;
import com.example.practica2.QuizApplication;
import com.example.practica2.R;
import com.example.practica2.media.MusicPlayer;
import com.example.practica2.media.SoundPlayer;
import com.google.android.material.appbar.MaterialToolbar;

public class GameActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private GameViewModel viewModel;
    private TextView score;
    private Button btnNext, btnBack;
    private Runnable hideRunnable;

    private SoundPlayer soundPlayer;
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        QuizApplication app = (QuizApplication) getApplication();
        soundPlayer = app.getSoundPlayer();
        musicPlayer = app.getMusicPlayer();

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        toolbar.setNavigationOnClickListener(v -> showExitAlert());

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        score   = findViewById(R.id.tvScore);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        // Puntuación actual
        viewModel.getScore().observe(this, s -> score.setText(String.valueOf(s)));

        // Cualquier cambio de validación o índice actual actualiza el estado/etiqueta del botón
        viewModel.getValidated().observe(this, v -> updateNextButton());
        viewModel.getCurrentIndexLive().observe(this, idx -> updateNextButton());

        // Navegación "Siguiente / Finalizar" -- no depende del XML
        btnNext.setOnClickListener(v -> {
            boolean last = viewModel.isLastQuestion();
            if (last) {
                Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                int finalScore = viewModel.getScore().getValue() == null ? 0 : viewModel.getScore().getValue();
                intent.putExtra("FINAL_SCORE", finalScore);
                //intent.putExtra("score", score);
                //intent.putExtra("totalQuestions", totalQuestions);
                //intent.putExtra("correctAnswers", correct);
                //intent.putExtra("wrongAnswers", wrong);

                startActivity(intent);
                finish();
            } else {
                // Al pasar de pregunta el fragment se actualiza
                viewModel.nextQuestion();
            }
        });

        // Botón atrás con confirmación
        btnBack.setOnClickListener(v -> showExitAlert());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { showExitAlert(); }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_audio_toggles, menu);
        updateAudioMenuIcons(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle_sound) {
            boolean current = AppPreferences.isSoundEnabled(this);
            boolean newValue = !current;
            AppPreferences.setSoundEnabled(this, newValue);
            soundPlayer.setSoundEnabled(newValue);
            invalidateOptionsMenu(); // refresca iconos
            return true;
        } else if (id == R.id.action_toggle_music) {
            boolean current = AppPreferences.isMusicEnabled(this);
            boolean newValue = !current;
            AppPreferences.setMusicEnabled(this, newValue);
            musicPlayer.setEnabled(newValue);
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppPreferences.isMusicEnabled(this)) {
            musicPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayer.pause();
    }


    private void updateAudioMenuIcons(Menu menu) {
        if (menu == null) return;

        boolean soundEnabled = AppPreferences.isSoundEnabled(this);
        boolean musicEnabled = AppPreferences.isMusicEnabled(this);

        MenuItem soundItem = menu.findItem(R.id.action_toggle_sound);
        MenuItem musicItem = menu.findItem(R.id.action_toggle_music);

        if (soundItem != null) {
            soundItem.setIcon(soundEnabled
                    ? R.drawable.sound_effects_on
                    : R.drawable.sound_effects_off);
        }

        if (musicItem != null) {
            musicItem.setIcon(musicEnabled
                    ? R.drawable.music_on
                    : R.drawable.music_off);
        }
    }



    public void showFeedback(String message, boolean isCorrect) {
        TextView tvFeedback = findViewById(R.id.tvFeedback);
        if (tvFeedback == null) return;

        // Cancela animaciones y callbacks previos
        tvFeedback.animate().cancel();
        if (hideRunnable != null) handler.removeCallbacks(hideRunnable);

        // Configura texto y color
        tvFeedback.setText(message);
        tvFeedback.setTextColor(ContextCompat.getColor(
                this, isCorrect ? R.color.green_correct : R.color.red_incorrect));

        // Pequeño movimiento
        tvFeedback.setTranslationY(-8f);
        tvFeedback.animate().translationY(0f).setDuration(180).start();

        // Fade in
        tvFeedback.setAlpha(0f);
        tvFeedback.setVisibility(View.VISIBLE);
        tvFeedback.animate()
                .alpha(1f)
                .setDuration(180)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        // Fade out a los 2s
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

    // Cambia el texto a finalizar cuando es la última pregunta
    private void updateNextButton() {
        boolean enable = Boolean.TRUE.equals(viewModel.getValidated().getValue());
        btnNext.setEnabled(enable);

        boolean last = viewModel.getCurrentIndex() == viewModel.getTotal() - 1;
        btnNext.setText(last ? "Finalizar" : "Siguiente");
    }


    private void showExitAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Salir del quiz")
                .setMessage("Perderás el progreso. ¿Quieres volver a la pantalla de inicio?")
                .setPositiveButton("Sí", (DialogInterface dialog, int which) -> finish())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}