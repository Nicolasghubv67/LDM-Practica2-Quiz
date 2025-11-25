package com.example.practica2.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_results")
public class GameResult {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long timestamp;       // System.currentTimeMillis()
    public int score;
    public int totalQuestions;
    public int correctAnswers;
    public int wrongAnswers;

    public GameResult(long timestamp, int score, int totalQuestions,
                      int correctAnswers, int wrongAnswers) {
        this.timestamp = timestamp;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }
}
