package com.example.practica2.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.example.practica2.R;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);

    }
}
