package com.example.practica2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String texto;
    public String opcionA;
    public String opcionB;
    public String opcionC;
    public String opcionD;
    public String respuestaCorrecta;
}
