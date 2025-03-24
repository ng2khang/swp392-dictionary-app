package com.example.prm392dictionaryapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.QuizQuestion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.QuestionViewHolder> {
    private List<QuizQuestion> questionList;
    private OnQuestionActionListener listener;

    public QuizQuestionAdapter(List<QuizQuestion> questionList) {
        this.questionList = questionList;
    }

    public interface OnQuestionActionListener {
        void onEditQuestion(QuizQuestion question, int position);
        void onDeleteQuestion(QuizQuestion question, int position);
    }

    public void setOnQuestionActionListener(OnQuestionActionListener listener) {
        this.listener = listener;
    }

    public void setEditing(boolean editing) {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuizQuestion question = questionList.get(position);
        holder.etQuestion.setText(question.getQuestion());
        holder.etAnswer.setText(question.getAnswer());

        holder.btnEdit.setVisibility(View.VISIBLE);
        holder.btnDelete.setVisibility(View.VISIBLE);

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditQuestion(question, position);
            }
        });

        // Set sự kiện cho nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteQuestion(question, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList != null ? questionList.size() : 0;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etQuestion, etAnswer;
        MaterialButton btnEdit, btnDelete;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            etQuestion = itemView.findViewById(R.id.et_question_text);
            etAnswer = itemView.findViewById(R.id.et_answer_text);
            btnEdit = itemView.findViewById(R.id.btn_edit_question);
            btnDelete = itemView.findViewById(R.id.btn_delete_question);
        }
    }
}