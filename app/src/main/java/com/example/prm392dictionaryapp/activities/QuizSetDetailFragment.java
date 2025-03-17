package com.example.prm392dictionaryapp.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.QuizQuestionAdapter;
import com.example.prm392dictionaryapp.entities.QuizQuestion;
import com.example.prm392dictionaryapp.utils.MyHelper;

import java.util.ArrayList;

public class QuizSetDetailFragment extends Fragment {
    private Toolbar toolbar;
    private Button btnDoQuiz, btnDeleteQuizSet, btnEditQuizInfo, btnSaveChanges;
    private LinearLayout layoutQuizSetInfo;
    private TextView tvQuizSetName, tvQuizSetDescription, tvQuizSetInfoExtra;
    private RecyclerView rvQuestions;
    private QuizQuestionAdapter questionAdapter;
    private ArrayList<QuizQuestion> questionList;
    private boolean isEditing = false;
    private long quizSetId;

    private MyHelper quizHelper;
    View view;

    public QuizSetDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_set_detail, container, false);

        // Inflate the layout for this fragment
        toolbar = view.findViewById(R.id.toolbar_quiz_set_detail);
        btnDoQuiz = view.findViewById(R.id.btn_do_quiz);
        btnDeleteQuizSet = view.findViewById(R.id.btn_delete_quiz_set);
        btnEditQuizInfo = view.findViewById(R.id.btn_edit_quiz_info);
        btnSaveChanges = view.findViewById(R.id.btn_save_changes);

        layoutQuizSetInfo = view.findViewById(R.id.layout_quiz_set_info);
        tvQuizSetName = view.findViewById(R.id.tv_quiz_set_name);
        tvQuizSetDescription = view.findViewById(R.id.tv_quiz_set_description);
        tvQuizSetInfoExtra = view.findViewById(R.id.tv_quiz_set_info_extra);

        rvQuestions = view.findViewById(R.id.rv_questions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));
        questionList = new ArrayList<>();
        questionAdapter = new QuizQuestionAdapter(questionList);
        rvQuestions.setAdapter(questionAdapter);

        quizHelper = new MyHelper(getActivity(), "quiz_database.db", null, 1);

        if (getArguments() != null) {
            quizSetId = getArguments().getLong("quizSetId");
            loadQuizSetDetail();
            loadQuizQuestions();
        }

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        btnEditQuizInfo.setOnClickListener(v -> toggleEditMode());
        btnDeleteQuizSet.setOnClickListener(v -> deleteQuizSet());
        btnDoQuiz.setOnClickListener(v -> startQuiz());
        btnSaveChanges.setOnClickListener(v -> saveChanges());

        return view;
    }
    private void loadQuizSetDetail() {
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = db.query(MyHelper.TABLE_QUIZ_SET, null, "id = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            int totalQuestion = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));
            int quizTime = cursor.getInt(cursor.getColumnIndexOrThrow("quizTime"));
            tvQuizSetName.setText(title);
            tvQuizSetDescription.setText(description);
            tvQuizSetInfoExtra.setText("Total question: " + totalQuestion + " | Time: " + quizTime + "m");
            cursor.close();
        }
        db.close();
    }

    private void loadQuizQuestions() {
        questionList.clear();
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = db.query(MyHelper.TABLE_QUIZ_QUESTION, null, "quizSetId = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int qId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow("question"));
                QuizQuestion question = QuizQuestion.builder().id(qId).question(questionText).build();
                questionList.add(question);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        questionAdapter.notifyDataSetChanged();
    }

    private void toggleEditMode() {
        isEditing = !isEditing;
        if (isEditing) {
            btnEditQuizInfo.setText("Done");
            // Nếu muốn cho phép chỉnh sửa thông tin Quiz Set, bạn có thể chuyển các TextView thành EditText
            // hoặc mở dialog chỉnh sửa. Tương tự, kích hoạt chế độ edit cho adapter câu hỏi.
        } else {
            btnEditQuizInfo.setText("Edit thông tin");
            // Lưu thay đổi thông tin Quiz Set nếu cần.
        }
        questionAdapter.setEditing(isEditing);
    }

    private void deleteQuizSet() {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        int deleted = db.delete(MyHelper.TABLE_QUIZ_SET, "id = ?", new String[]{String.valueOf(quizSetId)});
        db.close();
        if (deleted > 0) {
            Toast.makeText(getActivity(), "Quiz Set Removed", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Error while remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void startQuiz() {
        //logic làm quiz
        Toast.makeText(getActivity(), "Do this quiz...", Toast.LENGTH_SHORT).show();
    }

    private void saveChanges() {
        //do code here
        Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
        isEditing = false;
        btnEditQuizInfo.setText("Edit detail");
        questionAdapter.setEditing(false);
    }
}