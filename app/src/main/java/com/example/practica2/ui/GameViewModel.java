package com.example.practica2.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {

    public enum QuestionType {
        TEXT_WITH_TEXT_OPTIONS,
        IMAGE_WITH_TEXT_OPTIONS,
        TEXT_WITH_IMAGE_OPTIONS
    }

    public static class Question {
        public final QuestionType type;

        // Enunciado
        public final String  questionText;
        public final Integer questionImageRes;

        // Opciones
        public final List<String>  optionTexts;
        public final List<Integer> optionImageRes;

        public final int correctIndex;

        private Question(QuestionType type,
                         String questionText,
                         Integer questionImageRes,
                         List<String> optionTexts,
                         List<Integer> optionImageRes,
                         int correctIndex) {
            this.type = type;
            this.questionText = questionText;
            this.questionImageRes = questionImageRes;
            this.optionTexts = optionTexts;
            this.optionImageRes = optionImageRes;
            this.correctIndex = correctIndex;
        }

        public static Question textWithText(String q, List<String> options, int correct) {
            return new Question(QuestionType.TEXT_WITH_TEXT_OPTIONS, q, null, options, null, correct);
        }

        public static Question imageWithText(int imageRes, List<String> options, int correct) {
            return new Question(QuestionType.IMAGE_WITH_TEXT_OPTIONS, null, imageRes, options, null, correct);
        }

        public static Question textWithImages(String q, List<Integer> imageOptions, int correct) {
            return new Question(QuestionType.TEXT_WITH_IMAGE_OPTIONS, q, null, null, imageOptions, correct);
        }
    }

    private final MutableLiveData<Integer> score              = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> validated          = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> currentIndexLive   = new MutableLiveData<>(0);
    private final MutableLiveData<Question> currentQuestionLive = new MutableLiveData<>();

    private final List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;

    public GameViewModel() {
        // Se rellenan las preguntas desde setQuestionsFromDb()
    }

    public void setQuestionsFromDb(List<Question> newQuestions) {
        if (newQuestions == null || newQuestions.isEmpty()) return;

        questions.clear();
        questions.addAll(newQuestions);

        currentIndex = 0;
        currentIndexLive.setValue(0);

        score.setValue(0);
        validated.setValue(false);
        currentQuestionLive.setValue(questions.get(0));
    }

    // -------- Getters de LiveData / estado --------

    public LiveData<Integer> getScore()                    { return score; }
    public LiveData<Boolean> getValidated()                { return validated; }
    public LiveData<Integer> getCurrentIndexLive()         { return currentIndexLive; }
    public LiveData<Question> getCurrentQuestionLive()     { return currentQuestionLive; }

    public int getCurrentIndex() { return currentIndex; }
    public int getTotal()        { return questions.size(); }

    @Nullable
    public Question getCurrentQuestion() {
        if (questions.isEmpty()) return null;
        return questions.get(currentIndex);
    }

    // -------- Lógica del juego --------

    public void validateAnswer(int selectedIndex) {
        if (Boolean.TRUE.equals(validated.getValue())) return;
        Question q = getCurrentQuestion();
        if (q == null) return;

        int s = score.getValue() == null ? 0 : score.getValue();
        s += (selectedIndex == q.correctIndex) ? 3 : -2;
        score.setValue(s);
        validated.setValue(true);
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
        currentQuestionLive.setValue(questions.get(0));
    }
}