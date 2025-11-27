package com.example.practica2.repository;

import android.content.Context;

import com.example.practica2.R;
import com.example.practica2.data.AppDatabase;
import com.example.practica2.data.QuestionDao;
import com.example.practica2.data.Question;
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
    private final Context context;


    public GameRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        this.context = context;
        this.questionDao = db.questionDao();
    }

    public void getRandomQuestionsAsync(int count, QuestionsCallback callback) {
        ioExecutor.execute(() -> {

            questionDao.deleteAll();
            questionDao.insertAll(createSeedQuestions());

            List<Question> entities = questionDao.getRandom(count);

             List<GameViewModel.Question> mapped = new ArrayList<>();

            for (Question e : entities) {

                GameViewModel.Question vmQuestion;

                switch (e.type) {
                    case 0: // TEXT_WITH_TEXT_OPTIONS
                        vmQuestion = GameViewModel.Question.textWithText(
                                e.questionText,
                                Arrays.asList(e.optionA, e.optionB, e.optionC, e.optionD),
                                e.correctIndex
                        );
                        break;

                    case 1: // IMAGE_WITH_TEXT_OPTIONS
                        vmQuestion = GameViewModel.Question.imageWithText(
                                e.questionImageRes,
                                Arrays.asList(e.optionA, e.optionB, e.optionC, e.optionD),
                                e.correctIndex
                        );
                        break;

                    case 2: // TEXT_WITH_IMAGE_OPTIONS
                        vmQuestion = GameViewModel.Question.textWithImages(
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
                        vmQuestion = GameViewModel.Question.textWithText(
                                e.questionText,
                                Arrays.asList(e.optionA, e.optionB, e.optionC, e.optionD),
                                e.correctIndex
                        );
                        break;
                }

                mapped.add(vmQuestion);
            }

            if (callback != null) {
                callback.onQuestionsLoaded(mapped);
            }
        });
    }

    // ---------------------------------------------------------------------
    // Preguntas
    // ---------------------------------------------------------------------

    public List<Question> createSeedQuestions() {
        List<Question> seed = new ArrayList<>();

        // 1) TEXT_WITH_TEXT_OPTIONS
        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q1_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q1_option_a);
            q.optionB = context.getString(R.string.q1_option_b);
            q.optionC = context.getString(R.string.q1_option_c);
            q.optionD = context.getString(R.string.q1_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        // 2) IMAGE_WITH_TEXT_OPTIONS
        {
            Question q = new Question();
            q.type = 1;
            q.questionText = null;
            q.questionImageRes = R.drawable.question_flag_italy;
            q.optionA = context.getString(R.string.q2_option_a);
            q.optionB = context.getString(R.string.q2_option_b);
            q.optionC = context.getString(R.string.q2_option_c);
            q.optionD = context.getString(R.string.q2_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        // 3) TEXT_WITH_IMAGE_OPTIONS
        {
            Question q = new Question();
            q.type = 2;
            q.questionText = context.getString(R.string.q3_question);
            q.questionImageRes = null;
            q.optionAImageRes = R.drawable.option_question_1_bird;
            q.optionBImageRes = R.drawable.option_question_1_plant;
            q.optionCImageRes = R.drawable.option_question_1_mammal;
            q.optionDImageRes = R.drawable.option_question_1_reptile;
            q.correctIndex = 2;
            seed.add(q);
        }

        // 4)–41) TEXT_WITH_TEXT_OPTIONS

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q4_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q4_option_a);
            q.optionB = context.getString(R.string.q4_option_b);
            q.optionC = context.getString(R.string.q4_option_c);
            q.optionD = context.getString(R.string.q4_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q5_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q5_option_a);
            q.optionB = context.getString(R.string.q5_option_b);
            q.optionC = context.getString(R.string.q5_option_c);
            q.optionD = context.getString(R.string.q5_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q6_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q6_option_a);
            q.optionB = context.getString(R.string.q6_option_b);
            q.optionC = context.getString(R.string.q6_option_c);
            q.optionD = context.getString(R.string.q6_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q7_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q7_option_a);
            q.optionB = context.getString(R.string.q7_option_b);
            q.optionC = context.getString(R.string.q7_option_c);
            q.optionD = context.getString(R.string.q7_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q8_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q8_option_a);
            q.optionB = context.getString(R.string.q8_option_b);
            q.optionC = context.getString(R.string.q8_option_c);
            q.optionD = context.getString(R.string.q8_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q9_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q9_option_a);
            q.optionB = context.getString(R.string.q9_option_b);
            q.optionC = context.getString(R.string.q9_option_c);
            q.optionD = context.getString(R.string.q9_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q10_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q10_option_a);
            q.optionB = context.getString(R.string.q10_option_b);
            q.optionC = context.getString(R.string.q10_option_c);
            q.optionD = context.getString(R.string.q10_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q11_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q11_option_a);
            q.optionB = context.getString(R.string.q11_option_b);
            q.optionC = context.getString(R.string.q11_option_c);
            q.optionD = context.getString(R.string.q11_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q12_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q12_option_a);
            q.optionB = context.getString(R.string.q12_option_b);
            q.optionC = context.getString(R.string.q12_option_c);
            q.optionD = context.getString(R.string.q12_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q13_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q13_option_a);
            q.optionB = context.getString(R.string.q13_option_b);
            q.optionC = context.getString(R.string.q13_option_c);
            q.optionD = context.getString(R.string.q13_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q14_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q14_option_a);
            q.optionB = context.getString(R.string.q14_option_b);
            q.optionC = context.getString(R.string.q14_option_c);
            q.optionD = context.getString(R.string.q14_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q15_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q15_option_a);
            q.optionB = context.getString(R.string.q15_option_b);
            q.optionC = context.getString(R.string.q15_option_c);
            q.optionD = context.getString(R.string.q15_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q16_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q16_option_a);
            q.optionB = context.getString(R.string.q16_option_b);
            q.optionC = context.getString(R.string.q16_option_c);
            q.optionD = context.getString(R.string.q16_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q17_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q17_option_a);
            q.optionB = context.getString(R.string.q17_option_b);
            q.optionC = context.getString(R.string.q17_option_c);
            q.optionD = context.getString(R.string.q17_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q18_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q18_option_a);
            q.optionB = context.getString(R.string.q18_option_b);
            q.optionC = context.getString(R.string.q18_option_c);
            q.optionD = context.getString(R.string.q18_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q19_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q19_option_a);
            q.optionB = context.getString(R.string.q19_option_b);
            q.optionC = context.getString(R.string.q19_option_c);
            q.optionD = context.getString(R.string.q19_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q20_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q20_option_a);
            q.optionB = context.getString(R.string.q20_option_b);
            q.optionC = context.getString(R.string.q20_option_c);
            q.optionD = context.getString(R.string.q20_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q21_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q21_option_a);
            q.optionB = context.getString(R.string.q21_option_b);
            q.optionC = context.getString(R.string.q21_option_c);
            q.optionD = context.getString(R.string.q21_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q22_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q22_option_a);
            q.optionB = context.getString(R.string.q22_option_b);
            q.optionC = context.getString(R.string.q22_option_c);
            q.optionD = context.getString(R.string.q22_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q23_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q23_option_a);
            q.optionB = context.getString(R.string.q23_option_b);
            q.optionC = context.getString(R.string.q23_option_c);
            q.optionD = context.getString(R.string.q23_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q24_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q24_option_a);
            q.optionB = context.getString(R.string.q24_option_b);
            q.optionC = context.getString(R.string.q24_option_c);
            q.optionD = context.getString(R.string.q24_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q25_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q25_option_a);
            q.optionB = context.getString(R.string.q25_option_b);
            q.optionC = context.getString(R.string.q25_option_c);
            q.optionD = context.getString(R.string.q25_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q26_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q26_option_a);
            q.optionB = context.getString(R.string.q26_option_b);
            q.optionC = context.getString(R.string.q26_option_c);
            q.optionD = context.getString(R.string.q26_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q27_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q27_option_a);
            q.optionB = context.getString(R.string.q27_option_b);
            q.optionC = context.getString(R.string.q27_option_c);
            q.optionD = context.getString(R.string.q27_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q28_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q28_option_a);
            q.optionB = context.getString(R.string.q28_option_b);
            q.optionC = context.getString(R.string.q28_option_c);
            q.optionD = context.getString(R.string.q28_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q29_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q29_option_a);
            q.optionB = context.getString(R.string.q29_option_b);
            q.optionC = context.getString(R.string.q29_option_c);
            q.optionD = context.getString(R.string.q29_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q30_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q30_option_a);
            q.optionB = context.getString(R.string.q30_option_b);
            q.optionC = context.getString(R.string.q30_option_c);
            q.optionD = context.getString(R.string.q30_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q31_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q31_option_a);
            q.optionB = context.getString(R.string.q31_option_b);
            q.optionC = context.getString(R.string.q31_option_c);
            q.optionD = context.getString(R.string.q31_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q32_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q32_option_a);
            q.optionB = context.getString(R.string.q32_option_b);
            q.optionC = context.getString(R.string.q32_option_c);
            q.optionD = context.getString(R.string.q32_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q33_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q33_option_a);
            q.optionB = context.getString(R.string.q33_option_b);
            q.optionC = context.getString(R.string.q33_option_c);
            q.optionD = context.getString(R.string.q33_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q34_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q34_option_a);
            q.optionB = context.getString(R.string.q34_option_b);
            q.optionC = context.getString(R.string.q34_option_c);
            q.optionD = context.getString(R.string.q34_option_d);
            q.correctIndex = 3;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q35_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q35_option_a);
            q.optionB = context.getString(R.string.q35_option_b);
            q.optionC = context.getString(R.string.q35_option_c);
            q.optionD = context.getString(R.string.q35_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q36_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q36_option_a);
            q.optionB = context.getString(R.string.q36_option_b);
            q.optionC = context.getString(R.string.q36_option_c);
            q.optionD = context.getString(R.string.q36_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q37_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q37_option_a);
            q.optionB = context.getString(R.string.q37_option_b);
            q.optionC = context.getString(R.string.q37_option_c);
            q.optionD = context.getString(R.string.q37_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q38_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q38_option_a);
            q.optionB = context.getString(R.string.q38_option_b);
            q.optionC = context.getString(R.string.q38_option_c);
            q.optionD = context.getString(R.string.q38_option_d);
            q.correctIndex = 1;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q39_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q39_option_a);
            q.optionB = context.getString(R.string.q39_option_b);
            q.optionC = context.getString(R.string.q39_option_c);
            q.optionD = context.getString(R.string.q39_option_d);
            q.correctIndex = 2;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q40_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q40_option_a);
            q.optionB = context.getString(R.string.q40_option_b);
            q.optionC = context.getString(R.string.q40_option_c);
            q.optionD = context.getString(R.string.q40_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        {
            Question q = new Question();
            q.type = 0;
            q.questionText = context.getString(R.string.q41_question);
            q.questionImageRes = null;
            q.optionA = context.getString(R.string.q41_option_a);
            q.optionB = context.getString(R.string.q41_option_b);
            q.optionC = context.getString(R.string.q41_option_c);
            q.optionD = context.getString(R.string.q41_option_d);
            q.correctIndex = 0;
            seed.add(q);
        }

        return seed;
    }
}
