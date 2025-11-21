package com.example.practica2.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insert(Question question);

    @Insert
    void insertAll(List<Question> questions);

    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Query("SELECT * FROM Question WHERE id = :id")
    Question getById(int id);

    @Delete
    void delete(Question question);

    @Update
    void update(Question question);

    @Query("DELETE FROM Question")
    void deleteAll();
}
