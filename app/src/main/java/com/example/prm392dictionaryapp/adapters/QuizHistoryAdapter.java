package com.example.prm392dictionaryapp.adapters;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.QuizResult;

import java.util.List;
import java.util.Locale;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder> {
    private List<QuizResult> quizHistoryList;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public QuizHistoryAdapter(List<QuizResult> quizHistoryList) {
        this.quizHistoryList = quizHistoryList;
    }

    public interface OnItemClickListener {
        void onItemClick(QuizResult result);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_history, parent, false);
        return new QuizHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHistoryViewHolder holder, int position) {
        QuizResult result = quizHistoryList.get(position);
        holder.tvTitle.setText(result.getQuizSet().getTitle());
        holder.tvScore.setText(String.format("%d/%d", result.getScore(), result.getQuizSet().getTotalQuestion()));
        holder.tvCompletedAt.setText(SDF.format(result.getCompletedAt()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizHistoryList.size();
    }

    public static class QuizHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvScore, tvCompletedAt;

        public QuizHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvCompletedAt = itemView.findViewById(R.id.tv_completed_at);
        }
    }
} 