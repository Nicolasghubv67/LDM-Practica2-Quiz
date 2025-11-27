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
        public final String  questionText;
        public final Integer questionImageRes;
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

        public static Question imageWithText(Integer imageRes, List<String> options, int correct) {
            return new Question(QuestionType.IMAGE_WITH_TEXT_OPTIONS, null, imageRes, options, null, correct);
        }

        public static Question textWithImages(String q, List<Integer> imageOptions, int correct) {
            return new Question(QuestionType.TEXT_WITH_IMAGE_OPTIONS, q, null, null, imageOptions, correct);
        }
    }

    private final MutableLiveData<Integer> score              = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> wrongAnswers   = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> validated          = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> currentIndexLive   = new MutableLiveData<>(0);
    private final MutableLiveData<Question> currentQuestionLive = new MutableLiveData<>();

    private final List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;

    public GameViewModel() {}

    public void setQuestionsFromDb(List<Question> newQuestions) {
        if (newQuestions == null || newQuestions.isEmpty()) return;

        questions.clear();
        questions.addAll(newQuestions);

        currentIndex = 0;
        currentIndexLive.setValue(0);
        score.setValue(0);
        validated.setValue(false);
        correctAnswers.setValue(0);
        wrongAnswers.setValue(0);
        currentQuestionLive.setValue(questions.get(0));
    }

    public LiveData<Integer> getScore()                { return score; }
    public LiveData<Boolean> getValidated()            { return validated; }
    public LiveData<Integer> getCurrentIndexLive()     { return currentIndexLive; }
    public LiveData<Question> getCurrentQuestionLive() { return currentQuestionLive; }

    public int getCurrentIndex() { return currentIndex; }
    public int getTotal()        { return questions.size(); }

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

        int s = score.getValue() == null ? 0 : score.getValue();
        s += isCorrect ? 3 : -2;
        score.setValue(s);

        Integer c = correctAnswers.getValue();
        Integer w = wrongAnswers.getValue();
        if (c == null) c = 0;
        if (w == null) w = 0;

        if (isCorrect) {
            correctAnswers.setValue(c + 1);
        } else {
            wrongAnswers.setValue(w + 1);
        }

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
        correctAnswers.setValue(0);
        wrongAnswers.setValue(0);
        currentQuestionLive.setValue(questions.get(0));
    }
}