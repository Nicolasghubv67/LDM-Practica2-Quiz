package com.example.practica2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameResultDao {

    @Insert
    void insert(GameResult result);

    @Query("SELECT * FROM game_result ORDER BY timestamp DESC")
    List<GameResult> getAllOrdered();
}
