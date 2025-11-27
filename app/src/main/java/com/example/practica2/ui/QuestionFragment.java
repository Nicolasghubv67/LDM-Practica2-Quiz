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
        soundPlayer = ((QuizApplication) requireActivity().getApplication()).getSoundPlayer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        // Observa la pregunta actual
        viewModel.getCurrentQuestionLive().observe(
                getViewLifecycleOwner(),
                q -> {
                    if (q != null) renderQuestion(q);
                    else renderEmptyState();
                }
        );

        // Número de pregunta
        viewModel.getCurrentIndexLive().observe(
                getViewLifecycleOwner(),
                idx -> tvHeader.setText(
                        getString(R.string.question_header, idx + 1, viewModel.getTotal())
                )
        );

        // Habilitación/inhabilitación por validación
        viewModel.getValidated().observe(
                getViewLifecycleOwner(),
                validated -> {
                    boolean disable = Boolean.TRUE.equals(validated);
                    btnCheck.setEnabled(!disable);
                    setOptionsEnabled(!disable);
                }
        );

        btnCheck.setOnClickListener(view -> handleCheck());
    }

    // --------------------------------------------------------------------
    //  Renderizado de preguntas
    // --------------------------------------------------------------------

    private void renderEmptyState() {
        tvQuestion.setText(getString(R.string.loading_question));
        cardImage.setVisibility(View.GONE);

        clearRadio(rb1);
        clearRadio(rb2);
        clearRadio(rb3);
        clearRadio(rb4);
    }

    private void renderQuestion(GameViewModel.Question q) {
        rgOptions.clearCheck();
        clearRadio(rb1);
        clearRadio(rb2);
        clearRadio(rb3);
        clearRadio(rb4);

        switch (q.type) {
            case TEXT_WITH_TEXT_OPTIONS:
                tvQuestion.setVisibility(View.VISIBLE);
                tvQuestion.setText(q.questionText);

                cardImage.setVisibility(View.GONE);

                rb1.setText(q.optionTexts.get(0));
                rb2.setText(q.optionTexts.get(1));
                rb3.setText(q.optionTexts.get(2));
                rb4.setText(q.optionTexts.get(3));
                break;

            case IMAGE_WITH_TEXT_OPTIONS:
                tvQuestion.setVisibility(View.GONE);

                if (q.questionImageRes != null) {
                    cardImage.setVisibility(View.VISIBLE);
                    imgQuestion.setImageResource(q.questionImageRes);
                } else {
                    cardImage.setVisibility(View.GONE);
                }

                rb1.setText(q.optionTexts.get(0));
                rb2.setText(q.optionTexts.get(1));
                rb3.setText(q.optionTexts.get(2));
                rb4.setText(q.optionTexts.get(3));
                break;

            case TEXT_WITH_IMAGE_OPTIONS:
                tvQuestion.setVisibility(View.VISIBLE);
                tvQuestion.setText(q.questionText);

                cardImage.setVisibility(View.GONE);

                setupImageRadio(rb1, q.optionImageRes.get(0));
                setupImageRadio(rb2, q.optionImageRes.get(1));
                setupImageRadio(rb3, q.optionImageRes.get(2));
                setupImageRadio(rb4, q.optionImageRes.get(3));
                break;
        }
    }

    // --------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------

    private void setOptionsEnabled(boolean enabled) {
        for (int i = 0; i < rgOptions.getChildCount(); i++) {
            rgOptions.getChildAt(i).setEnabled(enabled);
        }
    }

    private void clearRadio(RadioButton rb) {
        rb.setText("");
        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rb.setPadding(dp(16), dp(16), dp(16), dp(16));
    }

    private void setupImageRadio(RadioButton rb, int imageRes) {
        rb.setText("");
        Drawable d = AppCompatResources.getDrawable(requireContext(), imageRes);
        rb.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
        rb.setCompoundDrawablePadding(dp(8));
        rb.setPadding(dp(16), dp(16), dp(16), dp(16));
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
    }

    // --------------------------------------------------------------------
    // Validación
    // --------------------------------------------------------------------

    private void handleCheck() {

        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1) {
            ((GameActivity) requireActivity()).showFeedback(getString(R.string.select_option), false);
            return;
        }

        int selectedIndex =
                checkedId == R.id.rb1 ? 0 :
                        checkedId == R.id.rb2 ? 1 :
                                checkedId == R.id.rb3 ? 2 : 3;

        viewModel.validateAnswer(selectedIndex);

        GameViewModel.Question current = viewModel.getCurrentQuestion();
        if (current == null) return;

        boolean correct = current.correctIndex == selectedIndex;

        if (correct) soundPlayer.playCorrect();
        else         soundPlayer.playWrong();

        ((GameActivity) requireActivity()).showFeedback(
                correct ? getString(R.string.correcto_3)
                        : getString(R.string.incorrecto_2),
                correct
        );

        btnCheck.setEnabled(false);
        setOptionsEnabled(false);
    }
}
