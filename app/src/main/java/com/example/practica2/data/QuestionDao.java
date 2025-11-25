package com.example.practica2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insert(Question question);

    @Insert
    void insertAll(List<Question> questions);

    // Para sacar N preguntas aleatorias
    @Query("SELECT * FROM Question ORDER BY RANDOM() LIMIT :limit")
    List<Question> getRandom(int limit);

    @Update
    void update(Question question);

    @Query("SELECT COUNT(*) FROM Question")
    int count();
}
