package com.example.practica2.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.practica2.data.Question; // Importamos la entidad
import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {

    // Constantes para legibilidad (coinciden con la BD)
    public static final int TYPE_TEXT_TEXT = 0;
    public static final int TYPE_IMAGE_TEXT = 1;
    public static final int TYPE_TEXT_IMAGE = 2;

    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> wrongAnswers = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> validated = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> currentIndexLive = new MutableLiveData<>(0);

    // Usamos la entidad Question directamente
    private final MutableLiveData<Question> currentQuestionLive = new MutableLiveData<>();

    private final List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;

    public void setQuestionsFromDb(List<Question> newQuestions) {
        if (newQuestions == null || newQuestions.isEmpty()) return;
        questions.clear();
        questions.addAll(newQuestions);
        reset();
    }

    // Getters
    public LiveData<Integer> getScore() { return score; }
    public LiveData<Boolean> getValidated() { return validated; }
    public LiveData<Integer> getCurrentIndexLive() { return currentIndexLive; }
    public LiveData<Question> getCurrentQuestionLive() { return currentQuestionLive; }

    public int getCurrentIndex() { return currentIndex; }
    public int getTotal() { return questions.size(); }

    public int getCorrectAnswersValue() {
        Integer v = correctAnswers.getValue();
        return v == null ? 0 : v;
    }
    public int getWrongAnswersValue() {
        Integer v = wrongAnswers.getValue();
        return v == null ? 0 : v;
    }

    @Nullable
    public Question getCurrentQuestion() {
        if (questions.isEmpty()) return null;
        return questions.get(currentIndex);
    }

    public void validateAnswer(int selectedIndex) {
        if (Boolean.TRUE.equals(validated.getValue())) return;
        Question q = getCurrentQuestion();
        if (q == null) return;

        boolean isCorrect = (selectedIndex == q.correctIndex);
        updateScore(isCorrect);
        validated.setValue(true);
    }

    private void updateScore(boolean isCorrect) {
        int s = score.getValue() == null ? 0 : score.getValue();
        score.setValue(s + (isCorrect ? 3 : -2));

        if (isCorrect) {
            correctAnswers.setValue(getCorrectAnswersValue() + 1);
        } else {
            wrongAnswers.setValue(getWrongAnswersValue() + 1);
        }
    }

    public void nextQuestion() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            currentIndexLive.setValue(currentIndex);
            validated.setValue(false);
            currentQuestionLive.setValue(questions.get(currentIndex));
        }
    }

    public boolean isLastQuestion() {
        return !questions.isEmpty() && currentIndex == questions.size() - 1;
    }

    public void reset() {
        if (questions.isEmpty()) return;
        currentIndex = 0;
        currentIndexLive.setValue(0);
        score.setValue(0);
        validated.setValue(false);
        correctAnswers.setValue(0);
        wrongAnswers.setValue(0);
        currentQuestionLive.setValue(questions.get(0));
    }
}