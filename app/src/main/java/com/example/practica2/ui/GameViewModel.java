package com.example.practica2.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.practica2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameViewModel extends ViewModel {

    public enum QuestionType {
        TEXT_WITH_TEXT_OPTIONS,            // texto + opciones de texto
        IMAGE_WITH_TEXT_OPTIONS,           // imagen + opciones de texto
        TEXT_WITH_IMAGE_OPTIONS            // texto + opciones de imagen
    }

    public static class Question {
        public final QuestionType type;

        // Enunciado
        public final String questionText;     // si la pregunta es textual
        public final Integer questionImageRes; // si la pregunta es una imagen (R.drawable.*)

        // Opciones
        public final List<String> optionTexts;        // si las opciones son de texto
        public final List<Integer> optionImageRes;    // si las opciones son imágenes (R.drawable.*)

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

    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> validated = new MutableLiveData<>(false);

    private final MutableLiveData<Integer> currentIndexLive = new MutableLiveData<>(0);
    private final MutableLiveData<Question> currentQuestionLive = new MutableLiveData<>();

    private final List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;

    // Se añade cada pregunta según el tipo de pregunta
    public GameViewModel() {
        questions.add(Question.textWithText(
                "¿Cuál es el océano más grande?",
                Arrays.asList("Atlántico", "Índico", "Pacífico", "Ártico"),
                2
        ));

        questions.add(Question.imageWithText(
                R.drawable.imagen_bandera,
                Arrays.asList("España", "Italia", "Rumanía", "Colombia"),
                1
        ));

        questions.add(Question.textWithImages(
                "¿Cuál de estas imágenes corresponde a un mamífero?",
                Arrays.asList(
                        R.drawable.ave,    // no
                        R.drawable.girasol,    // no
                        R.drawable.mamifero,   // sí
                        R.drawable.reptil  // no
                ),
                2
        ));
        questions.add(Question.textWithText(
                "¿En qué año llegó el hombre a la Luna?",
                Arrays.asList("1965", "1969", "1972", "1959"),
                1
        ));
        questions.add(Question.textWithText(
                "¿Cuál es el elemento químico del símbolo Fe?",
                Arrays.asList("Flúor", "Hierro", "Fermio", "Francio"),
                1
        ));

        currentQuestionLive.setValue(questions.get(currentIndex));
    }

    public LiveData<Integer> getScore() { return score; }
    public LiveData<Boolean> getValidated() { return validated; }
    public Question getCurrentQuestion() { return questions.get(currentIndex); }
    public int getCurrentIndex() { return currentIndex; }
    public int getTotal() { return questions.size(); }
    public LiveData<Integer> getCurrentIndexLive() { return currentIndexLive; }
    public LiveData<Question> getCurrentQuestionLive() { return currentQuestionLive; }


    public void validateAnswer(int selectedIndex) {
        if (Boolean.TRUE.equals(validated.getValue())) return;
        int s = score.getValue() == null ? 0 : score.getValue();
        s += (selectedIndex == getCurrentQuestion().correctIndex) ? 3 : -2;
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
        return currentIndex == questions.size() - 1;
    }

    public void reset() {
        currentIndex = 0;
        currentIndexLive.setValue(0);

        score.setValue(0);
        validated.setValue(false);
        currentQuestionLive.setValue(questions.get(0));
    }

}
