package com.example.prm392dictionaryapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.QuizSet;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {
    private List<QuizSet> quizList;
    public QuizListAdapter(List<QuizSet> quizList) {
        this.quizList = quizList;
    }
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    public interface OnItemClickListener {
        void onItemClick(QuizSet quiz);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizSet quiz = quizList.get(position);
        holder.tvTitle.setText(quiz.getTitle());
        holder.tvDescription.setText(quiz.getDescription());
        holder.tvTotalQuestionAndTime.setText(quiz.getTotalQuestion() + "c/" + quiz.getQuizTime() + "p");

        String dateString = SDF.format(quiz.getCreatedAt());
        holder.tvCreatedAt.setText(dateString);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(quiz);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvCreatedAt, tvTotalQuestionAndTime;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvDescription = itemView.findViewById(R.id.tv_quiz_description);
            tvCreatedAt = itemView.findViewById(R.id.tv_quiz_created_at);
            tvTotalQuestionAndTime = itemView.findViewById(R.id.tv_quiz_total_question_and_time);
        }
    }
}
