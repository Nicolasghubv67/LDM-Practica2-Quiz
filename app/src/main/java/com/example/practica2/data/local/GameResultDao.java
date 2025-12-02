package com.example.practica2.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.practica2.data.model.GameResult;

import java.util.List;

@Dao
public interface GameResultDao {

    @Insert
    void insert(GameResult result);

    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    androidx.lifecycle.LiveData<List<GameResult>> getAllOrderedLive();
}
