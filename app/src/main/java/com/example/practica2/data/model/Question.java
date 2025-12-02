package com.example.practica2.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad Room para preguntas.
 * Almacena IDs de recursos (R.string.x, R.drawable.y)
 */
@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // 0=Text/Text, 1=Image/Text, 2=Text/Image
    public int type;

    // Guardamos el ID del recurso (ej: R.string.q1_question), no el texto.
    // Usamos Integer para permitir nulos.
    public Integer questionTextRes;
    public Integer questionImageRes;

    // Opciones de texto (IDs de R.string)
    public Integer optionATextRes;
    public Integer optionBTextRes;
    public Integer optionCTextRes;
    public Integer optionDTextRes;

    // Opciones de imagen (IDs de R.drawable)
    public Integer optionAImageRes;
    public Integer optionBImageRes;
    public Integer optionCImageRes;
    public Integer optionDImageRes;

    public int correctIndex;
}