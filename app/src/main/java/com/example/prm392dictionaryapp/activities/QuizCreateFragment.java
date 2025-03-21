package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.Flashcard;
import com.example.prm392dictionaryapp.entities.FlashcardSet;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;
import com.example.prm392dictionaryapp.utils.MyHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizCreateFragment extends Fragment {

    private Button btnChooseVocab;
    private TextView tvSelectedVocab;
    private LinearLayout layoutQuizDetails;
    private EditText etQuizSetName, etQuizSetDescription, etTotalQuestions, etQuizTime;
    private Button btnCreateQuizSet;
    private MyHelper quizHelper;
    private DatabaseHelper dbHelper;

    private String selectedVocabSet = null;
    private int selectedSetFlashcardSetId = -1;
    View view;

    public QuizCreateFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_create, container, false);

        btnChooseVocab = view.findViewById(R.id.btn_choose_vocab);
        tvSelectedVocab = view.findViewById(R.id.tv_selected_vocab);
        layoutQuizDetails = view.findViewById(R.id.layout_quiz_details);
        etQuizSetName = view.findViewById(R.id.et_quiz_set_name);
        etQuizSetDescription = view.findViewById(R.id.et_quiz_set_description);
        etTotalQuestions = view.findViewById(R.id.et_total_questions);
        etQuizTime = view.findViewById(R.id.et_quiz_time);
        btnCreateQuizSet = view.findViewById(R.id.btn_create_quiz_set);

        quizHelper = new MyHelper(getActivity(), "quiz_database.db", null, 1);

        btnChooseVocab.setOnClickListener(v -> showVocabSelectionDialog());
        btnCreateQuizSet.setOnClickListener(v -> createQuizSet());

        return view;
    }
    private void showVocabSelectionDialog() {
        dbHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);

        List<FlashcardSet> flashcardSetList = dbHelper.getAllSetsWithFlashcardCount();

        if (flashcardSetList.isEmpty()) {
            Toast.makeText(getActivity(), "No flashcard sets available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] setTitles = new String[flashcardSetList.size()];
        for (int i = 0; i < flashcardSetList.size(); i++) {
            setTitles[i] = flashcardSetList.get(i).getTitle();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Flashcard Set");
        builder.setItems(setTitles, (dialog, which) -> {
            FlashcardSet selectedSet = flashcardSetList.get(which);
            selectedVocabSet = selectedSet.getTitle();
            selectedSetFlashcardSetId = selectedSet.getId();
            tvSelectedVocab.setText("Flashcard Set selected: " + selectedSet.getTitle());
            tvSelectedVocab.setVisibility(View.VISIBLE);
            layoutQuizDetails.setVisibility(View.VISIBLE);
        });
        builder.show();
    }

    private void createQuizSet() {
        if (selectedVocabSet == null) {
            Toast.makeText(getActivity(), "Please select Vocab set first", Toast.LENGTH_SHORT).show();
            return;
        }

        String quizSetName = etQuizSetName.getText().toString().trim();
        String description = etQuizSetDescription.getText().toString().trim();
        String totalQuestionsStr = etTotalQuestions.getText().toString().trim();
        String quizTimeStr = etQuizTime.getText().toString().trim();

        if (quizSetName.isEmpty() || totalQuestionsStr.isEmpty() || quizTimeStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter full require information.", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalQuestions, quizTime;
        try {
            totalQuestions = Integer.parseInt(totalQuestionsStr);
            quizTime = Integer.parseInt(quizTimeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Total of questions and time must be number", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", quizSetName);
        cv.put("description", selectedVocabSet + " - " + description);
        cv.put("totalQuestion", totalQuestions);
        cv.put("quizTime", quizTime);

        long rowId = db.insert(MyHelper.TABLE_QUIZ_SET, null, cv);
        db.close();

        if (rowId != -1) {
            Toast.makeText(getActivity(), "Create successfully", Toast.LENGTH_SHORT).show();
            QuizSetDetailFragment detailFragment = new QuizSetDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("quizSetId",Integer.parseInt(String.valueOf(rowId)));
            bundle.putInt("flashcardSetId", selectedSetFlashcardSetId);
            detailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Error while create new quiz set", Toast.LENGTH_SHORT).show();
        }
    }
}