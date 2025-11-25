package com.example.practica2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity para Room que soporta:
 *  - TEXT_WITH_TEXT_OPTIONS   (texto + opciones de texto)
 *  - IMAGE_WITH_TEXT_OPTIONS  (imagen + opciones de texto)
 *  - TEXT_WITH_IMAGE_OPTIONS  (texto + opciones de imagen)
 *-
 * El campo "type" representa ese tipo:
 *  0 = TEXT_WITH_TEXT_OPTIONS
 *  1 = IMAGE_WITH_TEXT_OPTIONS
 *  2 = TEXT_WITH_IMAGE_OPTIONS
 */
@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int type;

    // Si es TEXT_WITH_TEXT_OPTIONS o TEXT_WITH_IMAGE_OPTIONS se usa questionText.
    // Si es IMAGE_WITH_TEXT_OPTIONS se usa questionImageRes.
    public String questionText;
    public Integer questionImageRes;

    // Opciones en formato texto para TEXT_WITH_TEXT_OPTIONS o IMAGE_WITH_TEXT_OPTIONS
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;

    // Opciones en formato imagen para TEXT_WITH_IMAGE_OPTIONS
    public Integer optionAImageRes;
    public Integer optionBImageRes;
    public Integer optionCImageRes;
    public Integer optionDImageRes;

    // Índice de la opción correcta: 0 = A, 1 = B, 2 = C, 3 = D
    public int correctIndex;
}
