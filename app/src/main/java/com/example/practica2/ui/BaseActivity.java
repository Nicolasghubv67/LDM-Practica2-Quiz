package com.example.practica2.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.R;
import com.google.android.material.appbar.MaterialToolbar;

public abstract class BaseActivity extends AppCompatActivity {

    protected MaterialToolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        toolbar = findViewById(R.id.topAppBar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            // Mostrar flecha hacia atrás por defecto
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // El título lo controlamos manualmente desde el XML
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menú común para todas las pantallas
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Botón de navegación "atrás"
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // Botón de Ajustes
        else if (id == R.id.ic_settings) {

            if (!this.getClass().equals(SettingsActivity.class)) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            return true;
        }

        // Botón de ayuda
        else if (id == R.id.ic_help) {

            if (!this.getClass().equals(HelpActivity.class)) {
                Intent intent = new Intent(this, HelpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
