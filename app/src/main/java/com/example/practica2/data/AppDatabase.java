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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GameResult.class, Question.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract GameResultDao gameResultDao();
    public abstract QuestionDao questionDao();

    private static final ExecutorService IO_EXECUTOR =
            Executors.newSingleThreadExecutor();

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
                                    seedInitialQuestions(context.getApplicationContext());
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedInitialQuestions(Context context) {
        IO_EXECUTOR.execute(() -> {
            AppDatabase database = getInstance(context);
            QuestionDao dao = database.questionDao();

            if (dao.count() > 0) return;

            List<Question> seed = new ArrayList<>();

            // 1) TEXT_WITH_TEXT_OPTIONS
            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es el océano más grande del mundo?";
                q.questionImageRes = null;
                q.optionA = "Atlántico";
                q.optionB = "Índico";
                q.optionC = "Pacífico";
                q.optionD = "Ártico";
                q.correctIndex = 2;
                seed.add(q);
            }

            // 2) IMAGE_WITH_TEXT_OPTIONS
            {
                Question q = new Question();
                q.type = 1;
                q.questionText = null;
                q.questionImageRes = R.drawable.question_flag_italy;

                q.optionA = "España";
                q.optionB = "Italia";
                q.optionC = "Rumanía";
                q.optionD = "Portugal";
                q.correctIndex = 1;
                seed.add(q);
            }

            // 3) TEXT_WITH_IMAGE_OPTIONS
            {
                Question q = new Question();
                q.type = 2;
                q.questionText = "¿Qué imagen muestra un mamífero?";
                q.questionImageRes = null;

                q.optionAImageRes = R.drawable.option_question_1_mammal;
                q.optionBImageRes = R.drawable.option_question_1_plant;
                q.optionCImageRes = R.drawable.option_question_1_bird;
                q.optionDImageRes = R.drawable.option_question_1_reptile;
                q.correctIndex = 0;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿En qué país nació el escritor Franz Kafka?";
                q.questionImageRes = null;
                q.optionA = "Alemania";
                q.optionB = "Austria-Hungría";
                q.optionC = "Suiza";
                q.optionD = "Checoslovaquia";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es el elemento químico con el número atómico 79?";
                q.questionImageRes = null;
                q.optionA = "Mercurio";
                q.optionB = "Oro";
                q.optionC = "Cobre";
                q.optionD = "Uranio";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Qué civilización construyó la ciudad de Machu Picchu?";
                q.questionImageRes = null;
                q.optionA = "Azteca";
                q.optionB = "Maya";
                q.optionC = "Inca";
                q.optionD = "Moche";
                q.correctIndex = 2;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Qué filósofo griego fue maestro de Aristóteles?";
                q.questionImageRes = null;
                q.optionA = "Pitágoras";
                q.optionB = "Platón";
                q.optionC = "Sócrates";
                q.optionD = "Heráclito";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es la obra más famosa del pintor noruego Edvard Munch?";
                q.questionImageRes = null;
                q.optionA = "El Grito";
                q.optionB = "La Virgen";
                q.optionC = "El Beso";
                q.optionD = "La Danza";
                q.correctIndex = 0;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Quién fue el primer europeo en llegar a la India bordeando África?";
                q.questionImageRes = null;
                q.optionA = "Hernando de Magallanes";
                q.optionB = "Vasco da Gama";
                q.optionC = "Bartolomé Díaz";
                q.optionD = "Marco Polo";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es el río más largo del continente europeo?";
                q.questionImageRes = null;
                q.optionA = "Rin";
                q.optionB = "Volga";
                q.optionC = "Danubio";
                q.optionD = "Dniéper";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Quién compuso la ópera 'La Flauta Mágica'?";
                q.questionImageRes = null;
                q.optionA = "Beethoven";
                q.optionB = "Haydn";
                q.optionC = "Mozart";
                q.optionD = "Wagner";
                q.correctIndex = 2;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿En qué año cayó el Muro de Berlín?";
                q.questionImageRes = null;
                q.optionA = "1985";
                q.optionB = "1987";
                q.optionC = "1989";
                q.optionD = "1991";
                q.correctIndex = 2;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Qué matemático formuló el último teorema que tardó más de 350 años en demostrarse?";
                q.questionImageRes = null;
                q.optionA = "Carl Friedrich Gauss";
                q.optionB = "Pierre de Fermat";
                q.optionC = "Leonhard Euler";
                q.optionD = "Bernhard Riemann";
                q.correctIndex = 1;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es el libro más influyente de Charles Darwin?";
                q.questionImageRes = null;
                q.optionA = "El origen del hombre";
                q.optionB = "El viaje del Beagle";
                q.optionC = "El origen de las especies";
                q.optionD = "Selección natural";
                q.correctIndex = 2;
                seed.add(q);
            }

            {
                Question q = new Question();
                q.type = 0;
                q.questionText = "¿Cuál es el país con mayor número de islas del mundo?";
                q.questionImageRes = null;
                q.optionA = "Indonesia";
                q.optionB = "Filipinas";
                q.optionC = "Suecia";
                q.optionD = "Japón";
                q.correctIndex = 2;
                seed.add(q);
            }

            dao.insertAll(seed);
        });
    }
}
