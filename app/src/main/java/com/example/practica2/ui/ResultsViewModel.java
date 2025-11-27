package com.example.practica2.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.practica2.data.AppDatabase;
import com.example.practica2.data.GameResult;
import com.example.practica2.data.GameResultDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResultsViewModel extends AndroidViewModel {

    private final GameResultDao dao;
    private final LiveData<List<GameResult>> results;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ResultsViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).gameResultDao();
        results = dao.getAllOrderedLive();
    }

    public LiveData<List<GameResult>> getResults() {
        return results;
    }

    public void insert(GameResult result) {
        executor.execute(() -> dao.insert(result));
    }
}
