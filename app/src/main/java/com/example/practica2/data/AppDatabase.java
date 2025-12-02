package com.example.practica2.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.practica2.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {GameResult.class, Question.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract GameResultDao gameResultDao();
    public abstract QuestionDao questionDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "practica2_db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Insertar datos iniciales en un hilo separado
                                    Executors.newSingleThreadExecutor().execute(() ->
                                            getInstance(context).questionDao().insertAll(DataGenerator.getQuestions())
                                    );
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class DataGenerator {
        static List<Question> getQuestions() {
            List<Question> questions = new ArrayList<>();

            // --------------------------------------------------------------------------------
            // PREGUNTA 1: TIPO TEXTO - OPCIONES TEXTO
            // --------------------------------------------------------------------------------
            questions.add(createTextQuestion(
                    R.string.q1_question,
                    R.string.q1_option_a, R.string.q1_option_b, R.string.q1_option_c, R.string.q1_option_d,
                    2 // Índice correcto (C)
            ));

            // --------------------------------------------------------------------------------
            // PREGUNTA 2: TIPO IMAGEN - OPCIONES TEXTO
            // --------------------------------------------------------------------------------
            questions.add(createImageQuestion(
                    R.drawable.question_flag_italy,
                    R.string.q2_option_a, R.string.q2_option_b, R.string.q2_option_c, R.string.q2_option_d,
                    1 // Índice correcto (B)
            ));

            // --------------------------------------------------------------------------------
            // PREGUNTA 3: TIPO TEXTO - OPCIONES IMAGEN
            // --------------------------------------------------------------------------------
            questions.add(createImageOptionsQuestion(
                    R.string.q3_question,
                    R.drawable.option_question_1_bird,
                    R.drawable.option_question_1_plant,
                    R.drawable.option_question_1_mammal,
                    R.drawable.option_question_1_reptile,
                    2 // Índice correcto (C)
            ));

            // --------------------------------------------------------------------------------
            // PREGUNTAS 4 - 41 (TEXTO CON TEXTO)
            // --------------------------------------------------------------------------------

            questions.add(createTextQuestion(R.string.q4_question, R.string.q4_option_a, R.string.q4_option_b, R.string.q4_option_c, R.string.q4_option_d, 1));
            questions.add(createTextQuestion(R.string.q5_question, R.string.q5_option_a, R.string.q5_option_b, R.string.q5_option_c, R.string.q5_option_d, 1));
            questions.add(createTextQuestion(R.string.q6_question, R.string.q6_option_a, R.string.q6_option_b, R.string.q6_option_c, R.string.q6_option_d, 2));
            questions.add(createTextQuestion(R.string.q7_question, R.string.q7_option_a, R.string.q7_option_b, R.string.q7_option_c, R.string.q7_option_d, 1));
            questions.add(createTextQuestion(R.string.q8_question, R.string.q8_option_a, R.string.q8_option_b, R.string.q8_option_c, R.string.q8_option_d, 0));
            questions.add(createTextQuestion(R.string.q9_question, R.string.q9_option_a, R.string.q9_option_b, R.string.q9_option_c, R.string.q9_option_d, 1));
            questions.add(createTextQuestion(R.string.q10_question, R.string.q10_option_a, R.string.q10_option_b, R.string.q10_option_c, R.string.q10_option_d, 1));

            questions.add(createTextQuestion(R.string.q11_question, R.string.q11_option_a, R.string.q11_option_b, R.string.q11_option_c, R.string.q11_option_d, 2));
            questions.add(createTextQuestion(R.string.q12_question, R.string.q12_option_a, R.string.q12_option_b, R.string.q12_option_c, R.string.q12_option_d, 2));
            questions.add(createTextQuestion(R.string.q13_question, R.string.q13_option_a, R.string.q13_option_b, R.string.q13_option_c, R.string.q13_option_d, 1));
            questions.add(createTextQuestion(R.string.q14_question, R.string.q14_option_a, R.string.q14_option_b, R.string.q14_option_c, R.string.q14_option_d, 2));
            questions.add(createTextQuestion(R.string.q15_question, R.string.q15_option_a, R.string.q15_option_b, R.string.q15_option_c, R.string.q15_option_d, 0));
            questions.add(createTextQuestion(R.string.q16_question, R.string.q16_option_a, R.string.q16_option_b, R.string.q16_option_c, R.string.q16_option_d, 1));
            questions.add(createTextQuestion(R.string.q17_question, R.string.q17_option_a, R.string.q17_option_b, R.string.q17_option_c, R.string.q17_option_d, 1));
            questions.add(createTextQuestion(R.string.q18_question, R.string.q18_option_a, R.string.q18_option_b, R.string.q18_option_c, R.string.q18_option_d, 1));
            questions.add(createTextQuestion(R.string.q19_question, R.string.q19_option_a, R.string.q19_option_b, R.string.q19_option_c, R.string.q19_option_d, 0));
            questions.add(createTextQuestion(R.string.q20_question, R.string.q20_option_a, R.string.q20_option_b, R.string.q20_option_c, R.string.q20_option_d, 1));

            questions.add(createTextQuestion(R.string.q21_question, R.string.q21_option_a, R.string.q21_option_b, R.string.q21_option_c, R.string.q21_option_d, 2));
            questions.add(createTextQuestion(R.string.q22_question, R.string.q22_option_a, R.string.q22_option_b, R.string.q22_option_c, R.string.q22_option_d, 2));
            questions.add(createTextQuestion(R.string.q23_question, R.string.q23_option_a, R.string.q23_option_b, R.string.q23_option_c, R.string.q23_option_d, 1));
            questions.add(createTextQuestion(R.string.q24_question, R.string.q24_option_a, R.string.q24_option_b, R.string.q24_option_c, R.string.q24_option_d, 2));
            questions.add(createTextQuestion(R.string.q25_question, R.string.q25_option_a, R.string.q25_option_b, R.string.q25_option_c, R.string.q25_option_d, 0));
            questions.add(createTextQuestion(R.string.q26_question, R.string.q26_option_a, R.string.q26_option_b, R.string.q26_option_c, R.string.q26_option_d, 2));
            questions.add(createTextQuestion(R.string.q27_question, R.string.q27_option_a, R.string.q27_option_b, R.string.q27_option_c, R.string.q27_option_d, 2));
            questions.add(createTextQuestion(R.string.q28_question, R.string.q28_option_a, R.string.q28_option_b, R.string.q28_option_c, R.string.q28_option_d, 1));
            questions.add(createTextQuestion(R.string.q29_question, R.string.q29_option_a, R.string.q29_option_b, R.string.q29_option_c, R.string.q29_option_d, 2));
            questions.add(createTextQuestion(R.string.q30_question, R.string.q30_option_a, R.string.q30_option_b, R.string.q30_option_c, R.string.q30_option_d, 2));

            questions.add(createTextQuestion(R.string.q31_question, R.string.q31_option_a, R.string.q31_option_b, R.string.q31_option_c, R.string.q31_option_d, 1));
            questions.add(createTextQuestion(R.string.q32_question, R.string.q32_option_a, R.string.q32_option_b, R.string.q32_option_c, R.string.q32_option_d, 0));
            questions.add(createTextQuestion(R.string.q33_question, R.string.q33_option_a, R.string.q33_option_b, R.string.q33_option_c, R.string.q33_option_d, 0));
            questions.add(createTextQuestion(R.string.q34_question, R.string.q34_option_a, R.string.q34_option_b, R.string.q34_option_c, R.string.q34_option_d, 3));
            questions.add(createTextQuestion(R.string.q35_question, R.string.q35_option_a, R.string.q35_option_b, R.string.q35_option_c, R.string.q35_option_d, 0));
            questions.add(createTextQuestion(R.string.q36_question, R.string.q36_option_a, R.string.q36_option_b, R.string.q36_option_c, R.string.q36_option_d, 1));
            questions.add(createTextQuestion(R.string.q37_question, R.string.q37_option_a, R.string.q37_option_b, R.string.q37_option_c, R.string.q37_option_d, 1));
            questions.add(createTextQuestion(R.string.q38_question, R.string.q38_option_a, R.string.q38_option_b, R.string.q38_option_c, R.string.q38_option_d, 1));
            questions.add(createTextQuestion(R.string.q39_question, R.string.q39_option_a, R.string.q39_option_b, R.string.q39_option_c, R.string.q39_option_d, 2));
            questions.add(createTextQuestion(R.string.q40_question, R.string.q40_option_a, R.string.q40_option_b, R.string.q40_option_c, R.string.q40_option_d, 0));
            questions.add(createTextQuestion(R.string.q41_question, R.string.q41_option_a, R.string.q41_option_b, R.string.q41_option_c, R.string.q41_option_d, 0));

            return questions;
        }

        // --------------------------------------------------------------------------------
        // MÉTODOS AUXILIARES (Para simplificar la creación y evitar repetición)
        // --------------------------------------------------------------------------------

        private static Question createTextQuestion(int qTextRes, int opA, int opB, int opC, int opD, int correct) {
            Question q = new Question();
            q.type = 0; // GameViewModel.TYPE_TEXT_TEXT
            q.questionTextRes = qTextRes;
            q.optionATextRes = opA;
            q.optionBTextRes = opB;
            q.optionCTextRes = opC;
            q.optionDTextRes = opD;
            q.correctIndex = correct;
            return q;
        }

        private static Question createImageQuestion(int qImgRes, int opA, int opB, int opC, int opD, int correct) {
            Question q = new Question();
            q.type = 1; // GameViewModel.TYPE_IMAGE_TEXT
            q.questionImageRes = qImgRes;
            q.optionATextRes = opA;
            q.optionBTextRes = opB;
            q.optionCTextRes = opC;
            q.optionDTextRes = opD;
            q.correctIndex = correct;
            return q;
        }

        private static Question createImageOptionsQuestion(int qTextRes, int imgA, int imgB, int imgC, int imgD, int correct) {
            Question q = new Question();
            q.type = 2; // GameViewModel.TYPE_TEXT_IMAGE
            q.questionTextRes = qTextRes;
            q.optionAImageRes = imgA;
            q.optionBImageRes = imgB;
            q.optionCImageRes = imgC;
            q.optionDImageRes = imgD;
            q.correctIndex = correct;
            return q;
        }

    }
}