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

import java.util.List;

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.QuestionViewHolder> {
    private List<QuizQuestion> questionList;
    private boolean isEditing = false;

    public QuizQuestionAdapter(List<QuizQuestion> questionList) {
        this.questionList = questionList;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuizQuestion question = questionList.get(position);
        // Gán dữ liệu câu hỏi và đáp án cho các view
        holder.etQuestion.setText(question.getQuestion());
        // Giả sử question có 4 đáp án và đáp án đúng được lưu trong thuộc tính nào đó
        // Ví dụ:
        // holder.rbAnswer1.setText(question.getAnswer1());
        // ...
        // Chọn đáp án đúng trong RadioGroup
        // Cũng dựa vào isEditing, bạn có thể enable/disable các EditText và RadioButton
        holder.etQuestion.setEnabled(isEditing);
        holder.rbAnswer1.setEnabled(isEditing);
        holder.rbAnswer2.setEnabled(isEditing);
        holder.rbAnswer3.setEnabled(isEditing);
        holder.rbAnswer4.setEnabled(isEditing);

        // Sự kiện Edit & Delete của từng câu
        holder.btnEdit.setOnClickListener(v -> {
            // Mở dialog hoặc chuyển item sang chế độ edit riêng
        });
        holder.btnDelete.setOnClickListener(v -> {
            // Xác nhận và xóa câu hỏi khỏi danh sách
            questionList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        EditText etQuestion;
        RadioGroup rgAnswers;
        RadioButton rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;
        Button btnEdit, btnDelete;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            etQuestion = itemView.findViewById(R.id.et_question_text);
            rgAnswers = itemView.findViewById(R.id.rg_answers);
            rbAnswer1 = itemView.findViewById(R.id.rb_answer1);
            rbAnswer2 = itemView.findViewById(R.id.rb_answer2);
            rbAnswer3 = itemView.findViewById(R.id.rb_answer3);
            rbAnswer4 = itemView.findViewById(R.id.rb_answer4);
            btnEdit = itemView.findViewById(R.id.btn_edit_question);
            btnDelete = itemView.findViewById(R.id.btn_delete_question);
        }
    }
}