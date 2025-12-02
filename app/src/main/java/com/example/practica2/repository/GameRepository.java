package com.example.practica2.repository;

import android.content.Context;
import com.example.practica2.data.AppDatabase;
import com.example.practica2.data.Question;
import com.example.practica2.data.QuestionDao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameRepository {

    public interface QuestionsCallback {
        void onQuestionsLoaded(List<Question> questions);
    }

    private final QuestionDao questionDao;
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    public GameRepository(Context context) {
        // Contexto para obtener la instancia de la DB
        AppDatabase db = AppDatabase.getInstance(context);
        this.questionDao = db.questionDao();
    }

    public void getRandomQuestionsAsync(int count, QuestionsCallback callback) {
        ioExecutor.execute(() -> {
            // La DB ya está lista.
            List<Question> questions = questionDao.getRandom(count);
            if (callback != null) {
                callback.onQuestionsLoaded(questions);
            }
        });
    }
}