package com.example.practica2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad Room para preguntas.
 * type:
 *  0 = TEXT_WITH_TEXT_OPTIONS
 *  1 = IMAGE_WITH_TEXT_OPTIONS
 *  2 = TEXT_WITH_IMAGE_OPTIONS
 *-
 * Solo algunos campos se usan según el tipo.
 */
@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Tipo de pregunta
    public int type;

    // Enunciado
    public String questionText;
    public Integer questionImageRes;

    // Opciones de texto
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;

    // Opciones de imagen
    public Integer optionAImageRes;
    public Integer optionBImageRes;
    public Integer optionCImageRes;
    public Integer optionDImageRes;

    // Índice de la respuesta correcta: 0=A,1=B,2=C,3=D
    public int correctIndex;
}
