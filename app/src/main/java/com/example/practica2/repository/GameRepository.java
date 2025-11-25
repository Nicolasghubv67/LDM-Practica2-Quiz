package com.example.practica2.repository;

import android.content.Context;

import com.example.practica2.data.AppDatabase;
import com.example.practica2.data.QuestionDao;
import com.example.practica2.ui.GameViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameRepository {

    public interface QuestionsCallback {
        void onQuestionsLoaded(List<GameViewModel.Question> questions);
    }

    private final QuestionDao questionDao;
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    public GameRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        this.questionDao = db.questionDao();
    }

    public void getRandomQuestionsAsync(int count, QuestionsCallback callback) {
        ioExecutor.execute(() -> {
            List<com.example.practica2.data.Question> entities =
                    questionDao.getRandom(count);

            List<GameViewModel.Question> mapped = new ArrayList<>();
            for (com.example.practica2.data.Question e : entities) {

                GameViewModel.QuestionType type;
                switch (e.type) {
                    case 1:
                        type = GameViewModel.QuestionType.IMAGE_WITH_TEXT_OPTIONS;
                        break;
                    case 2:
                        type = GameViewModel.QuestionType.TEXT_WITH_IMAGE_OPTIONS;
                        break;
                    default:
                        type = GameViewModel.QuestionType.TEXT_WITH_TEXT_OPTIONS;
                }

                GameViewModel.Question qVm;

                switch (type) {
                    case TEXT_WITH_TEXT_OPTIONS:
                        qVm = GameViewModel.Question.textWithText(
                                e.questionText,
                                Arrays.asList(e.optionA, e.optionB, e.optionC, e.optionD),
                                e.correctIndex
                        );
                        break;

                    case IMAGE_WITH_TEXT_OPTIONS:
                        qVm = GameViewModel.Question.imageWithText(
                                e.questionImageRes,
                                Arrays.asList(e.optionA, e.optionB, e.optionC, e.optionD),
                                e.correctIndex
                        );
                        break;

                    case TEXT_WITH_IMAGE_OPTIONS:
                        qVm = GameViewModel.Question.textWithImages(
                                e.questionText,
                                Arrays.asList(
                                        e.optionAImageRes,
                                        e.optionBImageRes,
                                        e.optionCImageRes,
                                        e.optionDImageRes
                                ),
                                e.correctIndex
                        );
                        break;

                    default:
                        qVm = null;
                }
                mapped.add(qVm);
            }

            if (callback != null) {
                callback.onQuestionsLoaded(mapped);
            }
        });
    }
}