package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.QuizQuestionAdapter;
import com.example.prm392dictionaryapp.entities.Flashcard;
import com.example.prm392dictionaryapp.entities.QuizQuestion;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;
import com.example.prm392dictionaryapp.utils.MyHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizSetDetailFragment extends Fragment {
    private Toolbar toolbar;
    private Button btnDoQuiz, btnDeleteQuizSet, btnEditQuizInfo, btnSaveChanges;
    private EditText etQuizSetName, etQuizSetDescription, etQuizSetTotalQuestion, etQuizSetTime;
    private LinearLayout layoutQuizSetInfo;
    private RecyclerView rvQuestions;
    private QuizQuestionAdapter questionAdapter;
    private ArrayList<QuizQuestion> questionList;
    private boolean isEditing = false;
    private int quizSetId, flashcardSetId;

    private MyHelper quizHelper;
    private DatabaseHelper dbHelper;
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
        etQuizSetName = view.findViewById(R.id.et_quiz_set_name);
        etQuizSetDescription = view.findViewById(R.id.et_quiz_set_description);
        etQuizSetTotalQuestion = view.findViewById(R.id.et_quiz_set_total_question);
        etQuizSetTime = view.findViewById(R.id.et_quiz_set_time);

        rvQuestions = view.findViewById(R.id.rv_questions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));
        questionList = new ArrayList<>();
        questionAdapter = new QuizQuestionAdapter(questionList);
        rvQuestions.setAdapter(questionAdapter);

        quizHelper = new MyHelper(getActivity(), "quiz_database.db", null, 1);

        if (getArguments() != null) {
            quizSetId = getArguments().getInt("quizSetId");
            flashcardSetId = getArguments().getInt("flashcardSetId");
            loadQuizSetDetail();
            loadQuizQuestions();
        }

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnEditQuizInfo.setOnClickListener(v -> {
            isEditing = !isEditing;
            if (isEditing) {
                btnEditQuizInfo.setText("Done");
            } else {
                btnEditQuizInfo.setText("Edit detail");
                updateQuizSetInfo();
            }
            setQuizInfoEditable(isEditing);
        });

        btnDeleteQuizSet.setOnClickListener(v -> deleteQuizSet());
        btnDoQuiz.setOnClickListener(v -> startQuiz());
        btnSaveChanges.setOnClickListener(v -> saveChanges());
        return view;
    }
    private void loadQuizSetDetail() {
        try {
            SQLiteDatabase db = quizHelper.getReadableDatabase();
            Cursor cursor = db.query(MyHelper.TABLE_QUIZ_SET, null, "id = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int totalQuestion = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));
                int quizTime = cursor.getInt(cursor.getColumnIndexOrThrow("quizTime"));
                etQuizSetName.setText(title);
                etQuizSetDescription.setText(description);
                etQuizSetTotalQuestion.setText("Total question: " + totalQuestion);
                etQuizSetTime.setText("Time: " + quizTime + "minutes");
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error loading quiz set: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void loadQuizQuestions() {
        questionList.clear();
        dbHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);
        List<Flashcard> flashcardList = dbHelper.getFlashcardsBySetId(flashcardSetId);
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = db.query(MyHelper.TABLE_QUIZ_QUESTION, null, "quizSetId = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int qId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow("question"));
                String answerText = cursor.getString(cursor.getColumnIndexOrThrow("answer"));
                QuizQuestion question = QuizQuestion.builder().id(qId).question(questionText).answer(answerText).build();
                questionList.add(question);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        questionAdapter.notifyDataSetChanged();
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
    private void setQuizInfoEditable(boolean editable) {
        etQuizSetName.setEnabled(editable);
        etQuizSetDescription.setEnabled(editable);
        etQuizSetTotalQuestion.setEnabled(editable);
        etQuizSetTime.setEnabled(editable);
    }
    private void updateQuizSetInfo() {
        String updatedName = etQuizSetName.getText().toString().trim();
        String updatedDescription = etQuizSetDescription.getText().toString().trim();

        // Giả sử ta extract số từ chuỗi "Total question: ?"
        String totalQuestionText = etQuizSetTotalQuestion.getText().toString().replaceAll("[^0-9]", "");
        String quizTimeText = etQuizSetTime.getText().toString().replaceAll("[^0-9]", "");

        int updatedTotalQuestion = totalQuestionText.isEmpty() ? 0 : Integer.parseInt(totalQuestionText);
        int updatedQuizTime = quizTimeText.isEmpty() ? 0 : Integer.parseInt(quizTimeText);

        ContentValues cv = new ContentValues();
        cv.put("title", updatedName);
        cv.put("description", updatedDescription);
        cv.put("totalQuestion", updatedTotalQuestion);
        cv.put("quizTime", updatedQuizTime);

        try (SQLiteDatabase db = quizHelper.getWritableDatabase()) {
            int rows = db.update(MyHelper.TABLE_QUIZ_SET, cv, "id = ?", new String[]{String.valueOf(quizSetId)});
            if (rows > 0) {
                Toast.makeText(getActivity(), "Quiz set updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error updating quiz set: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}