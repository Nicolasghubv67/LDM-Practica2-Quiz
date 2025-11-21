package com.example.practica2.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practica2.QuizApplication;
import com.example.practica2.R;
import com.example.practica2.media.SoundManager;
import com.example.practica2.media.SoundPlayer;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class QuestionFragment extends Fragment {

    private GameViewModel viewModel;

    private TextView tvHeader, tvQuestion;
    private MaterialCardView cardImage;
    private ImageView imgQuestion;

    private RadioGroup rgOptions;
    private MaterialRadioButton rb1, rb2, rb3, rb4;
    private Button btnCheck;

    private SoundPlayer soundPlayer;

    public QuestionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundPlayer = ((QuizApplication) requireActivity()
                .getApplication())
                .getSoundPlayer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        tvHeader    = v.findViewById(R.id.tvHeader);
        tvQuestion  = v.findViewById(R.id.tvQuestion);
        cardImage   = v.findViewById(R.id.cardImage);
        imgQuestion = v.findViewById(R.id.imgQuestion);

        rgOptions = v.findViewById(R.id.rgOptions);
        rb1 = v.findViewById(R.id.rb1);
        rb2 = v.findViewById(R.id.rb2);
        rb3 = v.findViewById(R.id.rb3);
        rb4 = v.findViewById(R.id.rb4);
        btnCheck = v.findViewById(R.id.btnCheck);

        // El Fragment observa y se actualiza solo
        viewModel.getCurrentQuestionLive().observe(getViewLifecycleOwner(), q -> {
            if (q != null) renderQuestion(q);
        });

        // Número de pregunta
        viewModel.getCurrentIndexLive().observe(getViewLifecycleOwner(), idx -> {
            String header = "Pregunta " + (idx + 1) + "/" + viewModel.getTotal();
            tvHeader.setText(header);
        });

        // Habilitación/inhabilitación por validación
        viewModel.getValidated().observe(getViewLifecycleOwner(), validated -> {
            boolean disabled = Boolean.TRUE.equals(validated);
            btnCheck.setEnabled(!disabled);
            for (int i = 0; i < rgOptions.getChildCount(); i++) {
                rgOptions.getChildAt(i).setEnabled(!disabled);
            }
        });

        btnCheck.setOnClickListener(view -> handleCheck());
    }



    private void renderQuestion(GameViewModel.Question q) {
        // Reset selección/estado
        rgOptions.clearCheck();
        clearRadio(rb1);
        clearRadio(rb2);
        clearRadio(rb3);
        clearRadio(rb4);

        switch (q.type) {
            case TEXT_WITH_TEXT_OPTIONS: {
                tvQuestion.setVisibility(View.VISIBLE);
                tvQuestion.setText(q.questionText);

                cardImage.setVisibility(View.GONE);
                imgQuestion.setImageDrawable(null);

                rb1.setText(q.optionTexts.get(0));
                rb2.setText(q.optionTexts.get(1));
                rb3.setText(q.optionTexts.get(2));
                rb4.setText(q.optionTexts.get(3));
                break;
            }

            case IMAGE_WITH_TEXT_OPTIONS: {
                tvQuestion.setVisibility(View.GONE);

                if (q.questionImageRes != null && q.questionImageRes != 0) {
                    cardImage.setVisibility(View.VISIBLE);
                    imgQuestion.setImageDrawable(
                            AppCompatResources.getDrawable(requireContext(), q.questionImageRes)
                    );
                } else {
                    cardImage.setVisibility(View.GONE);
                    imgQuestion.setImageDrawable(null);
                }

                rb1.setText(q.optionTexts.get(0));
                rb2.setText(q.optionTexts.get(1));
                rb3.setText(q.optionTexts.get(2));
                rb4.setText(q.optionTexts.get(3));
                break;
            }

            case TEXT_WITH_IMAGE_OPTIONS: {
                tvQuestion.setVisibility(View.VISIBLE);
                tvQuestion.setText(q.questionText);

                cardImage.setVisibility(View.GONE);
                imgQuestion.setImageDrawable(null);

                setupImageRadio(rb1, q.optionImageRes.get(0));
                setupImageRadio(rb2, q.optionImageRes.get(1));
                setupImageRadio(rb3, q.optionImageRes.get(2));
                setupImageRadio(rb4, q.optionImageRes.get(3));
                break;
            }
        }
    }


    // ————— Helpers ——————————————————————————————————————————————

    private void clearRadio(RadioButton rb) {
        rb.setText("");
        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rb.setCompoundDrawablePadding(dp(8));
        int p = dp(16);
        rb.setPadding(p, p, p, p);
    }

    private void setupImageRadio(RadioButton rb, int imageRes) {
        rb.setText("");
        if (imageRes != 0) {
            Drawable d = AppCompatResources.getDrawable(requireContext(), imageRes);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
        } else {
            rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        rb.setCompoundDrawablePadding(dp(8));
        int p = dp(16);
        rb.setPadding(p, p, p, p);
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
    /**
     * Maneja la validación de la respuesta seleccionada por el usuario.
     * - Verifica si se ha seleccionado alguna opción.
     * - Llama al ViewModel para validar la respuesta.
     * - Muestra un feedback animado (correcto/incorrecto) en la Activity.
     */
    private void handleCheck() {
        // Obtener el ID del RadioButton seleccionado
        int checkedId = rgOptions.getCheckedRadioButtonId();

        // Si no hay nada seleccionado, avisamos con un feedback visual
        if (checkedId == -1) {
            if (getActivity() instanceof GameActivity) {
                ((GameActivity) getActivity()).showFeedback("Selecciona una opción", false);
            }
            return;
        }

        // Determinar el índice seleccionado según el ID del RadioButton
        int selectedIndex;
        if (checkedId == R.id.rb1) {
            selectedIndex = 0;
        } else if (checkedId == R.id.rb2) {
            selectedIndex = 1;
        } else if (checkedId == R.id.rb3) {
            selectedIndex = 2;
        } else {
            selectedIndex = 3;
        }

        // ViewModel valida la respuesta
        viewModel.validateAnswer(selectedIndex);

        // Calculamos si fue correcta o no (para mostrar feedback)
        boolean esCorrecta = selectedIndex == viewModel.getCurrentQuestion().correctIndex;

        if (esCorrecta) {
            soundPlayer.playCorrect();
        } else {
            soundPlayer.playWrong();
        }

        // Mostrar feedback visual desde la Activity (animación + color)
        if (getActivity() instanceof GameActivity) {
            ((GameActivity) getActivity()).showFeedback(
                    esCorrecta ? getString(R.string.correcto_3) : getString(R.string.incorrecto_2),
                    esCorrecta
            );
        }

        // Deshabilitar los botones y Check temporalmente (la observación de validated lo hará igual)
        btnCheck.setEnabled(false);
        for (int i = 0; i < rgOptions.getChildCount(); i++) {
            rgOptions.getChildAt(i).setEnabled(false);
        }
    }

}
