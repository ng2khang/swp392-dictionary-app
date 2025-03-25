package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.QuizQuestionAdapter;
import com.example.prm392dictionaryapp.entities.QuizQuestion;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.ArrayList;
public class QuizSetDetailFragment extends Fragment {
    private Toolbar toolbar;
    private Button btnDoQuiz, btnDeleteQuizSet, btnEditQuizInfo;
    private EditText etQuizSetName, etQuizSetDescription, etQuizSetTotalQuestion, etQuizSetTime;
    private RecyclerView rvQuestions;
    private QuizQuestionAdapter questionAdapter;
    private ArrayList<QuizQuestion> questionList;
    private boolean isEditing = false;
    private int quizSetId, flashcardSetId;
    private DatabaseHelper quizHelper;
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
        etQuizSetName = view.findViewById(R.id.et_quiz_set_name);
        etQuizSetDescription = view.findViewById(R.id.et_quiz_set_description);
        etQuizSetTotalQuestion = view.findViewById(R.id.et_quiz_set_total_question);
        etQuizSetTime = view.findViewById(R.id.et_quiz_set_time);
        rvQuestions = view.findViewById(R.id.rv_questions);
        btnEditQuizInfo = view.findViewById(R.id.btn_edit_quiz_info);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);

        if (getArguments() != null) {
            quizSetId = (int) getArguments().getLong("quizSetId", -1);
            flashcardSetId = getArguments().getInt("set_id", -1);
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
        return view;
    }
    private void loadQuizSetDetail() {
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {"title", "description", "totalQuestion", "quizTime"};
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_SET, columns, "id = ?",
                    new String[]{String.valueOf(quizSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int totalQuestion = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));
                int quizTime = cursor.getInt(cursor.getColumnIndexOrThrow("quizTime"));

                etQuizSetName.setText(title);
                etQuizSetDescription.setText(description);
                etQuizSetTotalQuestion.setText("Total question: " + totalQuestion);
                etQuizSetTime.setText("Time: " + quizTime + " minutes");
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error loading quiz set detail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    private void loadQuizQuestions() {
        questionList = new ArrayList<>();
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {"id", "question", "answer", "addedAt", "quizSetId"};
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_QUESTION, columns, "quizSetId = ?",
                    new String[]{String.valueOf(quizSetId)}, null, null, "addedAt DESC");
            if (cursor != null && cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String questionText = cursor.getString(cursor.getColumnIndexOrThrow("question"));
                    String answerText = cursor.getString(cursor.getColumnIndexOrThrow("answer"));
                    QuizQuestion question = new QuizQuestion();
                    question.setId(id);
                    question.setQuestion(questionText);
                    question.setAnswer(answerText);
                    questionList.add(question);
                } while (cursor.moveToNext());
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error loading quiz questions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        questionAdapter = new QuizQuestionAdapter(questionList);
        questionAdapter.setOnQuestionActionListener(new QuizQuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onEditQuestion(QuizQuestion question, int position) {
                showEditQuestionDialog(question, position);
            }

            @Override
            public void onDeleteQuestion(QuizQuestion question, int position) {
                deleteQuizQuestion(question.getId(), position);
            }
        });
        rvQuestions.setAdapter(questionAdapter);
    }

    private void showEditQuestionDialog(QuizQuestion question, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Question");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_question, null);
        EditText etEditQuestion = dialogView.findViewById(R.id.et_edit_question);
        EditText etEditAnswer = dialogView.findViewById(R.id.et_edit_answer);

        etEditQuestion.setText(question.getQuestion());
        etEditAnswer.setText(question.getAnswer());

        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newQuestion = etEditQuestion.getText().toString().trim();
            String newAnswer = etEditAnswer.getText().toString().trim();
            if (!newQuestion.isEmpty() && !newAnswer.isEmpty()) {
                updateQuizQuestion(question.getId(), newQuestion, newAnswer, position);
            } else {
                Toast.makeText(getActivity(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateQuizQuestion(int questionId, String newQuestion, String newAnswer, int position) {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("question", newQuestion);
        cv.put("answer", newAnswer);
        int updated = db.update(DatabaseHelper.TABLE_QUIZ_QUESTION, cv, "id = ?", new String[]{String.valueOf(questionId)});
        db.close();
        if (updated > 0) {
            QuizQuestion q = questionList.get(position);
            q.setQuestion(newQuestion);
            q.setAnswer(newAnswer);
            questionAdapter.notifyItemChanged(position);
            Toast.makeText(getActivity(), "Question updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteQuizQuestion(int questionId, int position) {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        int deleted = db.delete(DatabaseHelper.TABLE_QUIZ_QUESTION, "id = ?", new String[]{String.valueOf(questionId)});
        db.close();
        if (deleted > 0) {
            questionList.remove(position);
            questionAdapter.notifyItemRemoved(position);
            Toast.makeText(getActivity(), "Question deleted", Toast.LENGTH_SHORT).show();
            updateTotalQuestionUI();
        } else {
            Toast.makeText(getActivity(), "Failed to delete question", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteQuizSet() {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        int deleted = db.delete(DatabaseHelper.TABLE_QUIZ_SET, "id = ?", new String[]{String.valueOf(quizSetId)});
        db.close();
        if (deleted > 0) {
            Toast.makeText(getActivity(), "Quiz Set Removed", Toast.LENGTH_SHORT).show();
            Bundle result = new Bundle();
            result.putBoolean("reload", true);
            getParentFragmentManager().setFragmentResult("refreshQuizList", result);
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getActivity(), "Error while remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void startQuiz() {
        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(getActivity(), "No questions available for this quiz", Toast.LENGTH_SHORT).show();
            return;
        }
        QuizTakingFragment quizTakingFragment = new QuizTakingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("quizSetId", quizSetId);
        quizTakingFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, quizTakingFragment)
                .addToBackStack(null)
                .commit();
    }

    private void setQuizInfoEditable(boolean editable) {
        etQuizSetName.setEnabled(editable);
        etQuizSetDescription.setEnabled(editable);
        etQuizSetTime.setEnabled(editable);
    }
    private void updateQuizSetInfo() {
        String updatedName = etQuizSetName.getText().toString().trim();
        String updatedDescription = etQuizSetDescription.getText().toString().trim();

        String quizTimeText = etQuizSetTime.getText().toString().replaceAll("[^0-9]", "");

        int updatedQuizTime = quizTimeText.isEmpty() ? 0 : Integer.parseInt(quizTimeText);

        ContentValues cv = new ContentValues();
        cv.put("title", updatedName);
        cv.put("description", updatedDescription);
        cv.put("quizTime", updatedQuizTime);

        SQLiteDatabase db = quizHelper.getWritableDatabase();
        try {
            int rows = db.update(DatabaseHelper.TABLE_QUIZ_SET, cv, "id = ?", new String[]{String.valueOf(quizSetId)});
            if (rows > 0) {
                Toast.makeText(getActivity(), "Quiz set updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error updating quiz set: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private void updateTotalQuestionUI() {
        int count = 0;
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_QUIZ_QUESTION + " WHERE quizSetId = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(quizSetId)});
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            updateTotalQuestionInDB(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        etQuizSetTotalQuestion.setText("Total question: " + count);
    }
    private void updateTotalQuestionInDB(int newTotal) {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("totalQuestion", newTotal);
        db.update(DatabaseHelper.TABLE_QUIZ_SET, cv, "id = ?", new String[]{String.valueOf(quizSetId)});
        db.close();
    }

}