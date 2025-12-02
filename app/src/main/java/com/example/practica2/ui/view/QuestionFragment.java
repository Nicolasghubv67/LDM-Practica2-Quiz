package com.example.practica2.ui.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.practica2.data.model.Question;
import com.example.practica2.media.SoundPlayer;
import com.example.practica2.ui.viewmodel.GameViewModel;
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

        // Bindings
        tvHeader    = v.findViewById(R.id.tvHeader);
        tvQuestion  = v.findViewById(R.id.tvQuestion);
        cardImage   = v.findViewById(R.id.cardImage);
        imgQuestion = v.findViewById(R.id.imgQuestion);
        rgOptions   = v.findViewById(R.id.rgOptions);
        rb1         = v.findViewById(R.id.rb1);
        rb2         = v.findViewById(R.id.rb2);
        rb3         = v.findViewById(R.id.rb3);
        rb4         = v.findViewById(R.id.rb4);
        btnCheck    = v.findViewById(R.id.btnCheck);

        // Observers
        viewModel.getCurrentQuestionLive().observe(getViewLifecycleOwner(), q -> {
            if (q != null) renderQuestion(q);
        });

        viewModel.getCurrentIndexLive().observe(getViewLifecycleOwner(), idx ->
                tvHeader.setText(getString(R.string.question_header, idx + 1, viewModel.getTotal()))
        );

        viewModel.getValidated().observe(getViewLifecycleOwner(), validated -> {
            boolean disable = Boolean.TRUE.equals(validated);
            btnCheck.setEnabled(!disable);
            setOptionsEnabled(!disable);
        });

        btnCheck.setOnClickListener(view -> handleCheck());
    }

    private void renderQuestion(Question q) {
        rgOptions.clearCheck();
        resetRadio(rb1); resetRadio(rb2); resetRadio(rb3); resetRadio(rb4);

        // Lógica simplificada basada en tipos
        if (q.type == GameViewModel.TYPE_TEXT_TEXT) {
            tvQuestion.setVisibility(View.VISIBLE);
            cardImage.setVisibility(View.GONE);
            // setTexto acepta int Resource ID directamente
            if (q.questionTextRes != null) tvQuestion.setText(q.questionTextRes);

            if (q.optionATextRes != null) rb1.setText(q.optionATextRes);
            if (q.optionBTextRes != null) rb2.setText(q.optionBTextRes);
            if (q.optionCTextRes != null) rb3.setText(q.optionCTextRes);
            if (q.optionDTextRes != null) rb4.setText(q.optionDTextRes);

        } else if (q.type == GameViewModel.TYPE_IMAGE_TEXT) {
            tvQuestion.setVisibility(View.GONE);
            cardImage.setVisibility(View.VISIBLE);
            if (q.questionImageRes != null) imgQuestion.setImageResource(q.questionImageRes);

            if (q.optionATextRes != null) rb1.setText(q.optionATextRes);
            if (q.optionBTextRes != null) rb2.setText(q.optionBTextRes);
            if (q.optionCTextRes != null) rb3.setText(q.optionCTextRes);
            if (q.optionDTextRes != null) rb4.setText(q.optionDTextRes);

        } else if (q.type == GameViewModel.TYPE_TEXT_IMAGE) {
            tvQuestion.setVisibility(View.VISIBLE);
            cardImage.setVisibility(View.GONE);
            if (q.questionTextRes != null) tvQuestion.setText(q.questionTextRes);

            setupImageRadio(rb1, q.optionAImageRes);
            setupImageRadio(rb2, q.optionBImageRes);
            setupImageRadio(rb3, q.optionCImageRes);
            setupImageRadio(rb4, q.optionDImageRes);
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        for (int i = 0; i < rgOptions.getChildCount(); i++) rgOptions.getChildAt(i).setEnabled(enabled);
    }

    private void resetRadio(RadioButton rb) {
        rb.setText("");
        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    private void setupImageRadio(RadioButton rb, Integer imageRes) {
        rb.setText("");
        if (imageRes != null) {
            Drawable d = AppCompatResources.getDrawable(requireContext(), imageRes);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
            rb.setCompoundDrawablePadding(16); // Valor fijo o usar dp helper
        }
    }

    private void handleCheck() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1) return; // O mostrar toast

        int selectedIndex = (checkedId == R.id.rb1) ? 0 : (checkedId == R.id.rb2) ? 1 : (checkedId == R.id.rb3) ? 2 : 3;

        viewModel.validateAnswer(selectedIndex);

        Question current = viewModel.getCurrentQuestion();
        if (current != null) {
            boolean correct = (current.correctIndex == selectedIndex);
            if (correct) soundPlayer.playCorrect(); else soundPlayer.playWrong();

            ((GameActivity) requireActivity()).showFeedback(
                    getString(correct ? R.string.correcto_3 : R.string.incorrecto_2), correct
            );
        }
    }
}