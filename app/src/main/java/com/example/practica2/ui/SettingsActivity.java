package com.example.practica2.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.practica2.AppPreferences;
import com.example.practica2.QuizApplication;
import com.example.practica2.R;
import com.example.practica2.media.MusicPlayer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends BaseActivity {

    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Reproductor global de la aplicación, compartido entre pantallas
        QuizApplication app = (QuizApplication) getApplication();
        musicPlayer = app.getMusicPlayer();

        // ---- Sonido y música ----
        MaterialSwitch switchSound = findViewById(R.id.switchSound);
        MaterialSwitch switchMusic = findViewById(R.id.switchMusic);

        // Estado inicial de los switches según las preferencias guardadas
        boolean soundEnabled = AppPreferences.isSoundEnabled(this);
        boolean musicEnabled = AppPreferences.isMusicEnabled(this);
        switchSound.setChecked(soundEnabled);
        switchMusic.setChecked(musicEnabled);

        // Al cambiar el estado del sonido, se actualiza la preferencia
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppPreferences.setSoundEnabled(this, isChecked)
        );

        // Al cambiar el estado de la música, se guarda la preferencia
        // y se arranca o pausa el reproductor para aplicar el cambio al momento
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppPreferences.setMusicEnabled(this, isChecked);
            if (isChecked) {
                musicPlayer.start();
            } else {
                musicPlayer.pause();
            }
        });

        // ---- Idioma ----
        View rowLanguage = findViewById(R.id.rowLanguage);
        TextView tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage);

        // Idioma actual
        tvCurrentLanguage.setText(getCurrentLanguageLabel(this));

        rowLanguage.setOnClickListener(v -> showLanguageDialog(tvCurrentLanguage));
    }

    // Muestra un diálogo simple con los dos idiomas disponibles
    private void showLanguageDialog(TextView tvCurrentLanguage) {

        // Opciones visibles en el diálogo
        String[] items = {
                getString(R.string.settings_language_spanish),
                getString(R.string.settings_language_english)
        };

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.settings_language_dialog_title)
                .setItems(items, (dialog, which) -> {

                    // Selección del idioma y aplicación del Locale
                    if (which == 0) {
                        AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("es")
                        );
                    } else {
                        AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("en")
                        );
                    }

                    // Refresca la etiqueta del idioma seleccionado
                    tvCurrentLanguage.setText(getCurrentLanguageLabel(this));
                })
                .show();
    }


    // Devuelve el nombre del idioma actualmente activo en la app. (español/inglés)
    private static String getCurrentLanguageLabel(Context context) {

        LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
        String lang = locales.toLanguageTags();

        if (lang.startsWith("en")) {
            return context.getString(R.string.settings_language_english);
        } else {
            return context.getString(R.string.settings_language_spanish);
        }
    }

}
