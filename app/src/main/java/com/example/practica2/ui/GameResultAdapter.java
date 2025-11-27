package com.example.practica2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.R;
import com.example.practica2.data.GameResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameResultAdapter extends RecyclerView.Adapter<GameResultAdapter.ResultViewHolder> {

    private final List<GameResult> items = new ArrayList<>();
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void submitList(List<GameResult> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@SuppressWarnings("ClassEscapesDefinedScope") @NonNull ResultViewHolder holder, int position) {
        GameResult r = items.get(position);
        holder.tvCorrect.setText(String.valueOf(r.correctAnswers));
        holder.tvWrong.setText(String.valueOf(r.wrongAnswers));
        holder.tvTotal.setText(String.valueOf(r.totalQuestions));
        holder.tvScore.setText(String.valueOf(r.score));

        String date = dateFormat.format(new Date(r.timestamp));
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvCorrect, tvWrong, tvTotal, tvScore, tvDate;

        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCorrect = itemView.findViewById(R.id.tvCorrect);
            tvWrong   = itemView.findViewById(R.id.tvWrong);
            tvTotal   = itemView.findViewById(R.id.tvTotal);
            tvScore   = itemView.findViewById(R.id.tvScore);
            tvDate    = itemView.findViewById(R.id.tvDate);
        }
    }
}
