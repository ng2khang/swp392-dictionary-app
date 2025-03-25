package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizCreateFragment extends Fragment {

    private TextView tvSelectedVocab;
    private LinearLayout layoutQuizDetails;
    private EditText etQuizSetName, etQuizSetDescription, etTotalQuestions, etQuizTime;
    private DatabaseHelper quizHelper;
    private List<FlashcardSet> flashcardSetList;
    private FlashcardSet selectedFlashcardSet;
    private Button btnChooseVocab, btnCreateQuizSet;
    private View view;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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

        quizHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);
        loadFlashcardSets();
        btnChooseVocab.setOnClickListener(v -> showFlashcardSetDialog());
        btnCreateQuizSet.setOnClickListener(v -> createQuizSet());

        return view;
    }
    private void loadFlashcardSets() {
        flashcardSetList = new ArrayList<>();
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {"id", "title"};
            cursor = db.query(DatabaseHelper.TABLE_FLASHCARD_SET, columns, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    int flashcardCount = getFlashcardCount(id);

                    FlashcardSet fs = new FlashcardSet();
                    fs.setId(id);
                    fs.setTitle(title);
                    fs.setFlashcardCount(flashcardCount);
                    flashcardSetList.add(fs);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private int getFlashcardCount(int flashcardSetId) {
        int count = 0;
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_FLASHCARD + " WHERE set_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(flashcardSetId)});
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return count;
    }
    private void showFlashcardSetDialog() {
        if (flashcardSetList == null || flashcardSetList.isEmpty()) {
            Toast.makeText(getActivity(), "No flashcard sets available", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> titles = new ArrayList<>();
        for (FlashcardSet fs : flashcardSetList) {
            titles.add(fs.getTitle());
        }
        String[] items = titles.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Flashcard Set");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFlashcardSet = flashcardSetList.get(which);
                tvSelectedVocab.setText("Selected: " + selectedFlashcardSet.getTitle());
                tvSelectedVocab.setVisibility(View.VISIBLE);
                layoutQuizDetails.setVisibility(View.VISIBLE);
                etTotalQuestions.setHint("Total question (Max: " + selectedFlashcardSet.getFlashcardCount() + ")");
            }
        });
        builder.show();
    }
    private void createQuizSet() {
        String title = etQuizSetName.getText().toString().trim();
        String description = etQuizSetDescription.getText().toString().trim();
        String totalQuestionsStr = etTotalQuestions.getText().toString().trim();
        String quizTimeStr = etQuizTime.getText().toString().trim();

        if (title.isEmpty() || totalQuestionsStr.isEmpty() || quizTimeStr.isEmpty() || selectedFlashcardSet == null) {
            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int totalQuestions = Integer.parseInt(totalQuestionsStr);
        int quizTime = Integer.parseInt(quizTimeStr);

        if (totalQuestions > selectedFlashcardSet.getFlashcardCount()) {
            Toast.makeText(getActivity(), "Total questions cannot exceed flashcard count (" +
                    selectedFlashcardSet.getFlashcardCount() + ")", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("totalQuestion", totalQuestions);
        values.put("quizTime", quizTime);
        values.put("set_id", selectedFlashcardSet.getId());
        long quizSetId = db.insert(DatabaseHelper.TABLE_QUIZ_SET, null, values);
        db.close();

        if (quizSetId != -1) {
            insertQuizQuestions(quizSetId, totalQuestions, selectedFlashcardSet.getId());
            Toast.makeText(getActivity(), "Create successfully", Toast.LENGTH_SHORT).show();
            QuizSetDetailFragment detailFragment = new QuizSetDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("quizSetId",quizSetId);
            bundle.putInt("set_id", selectedFlashcardSet.getId());
            detailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Failed to create quiz set", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertQuizQuestions(long quizSetId, int totalQuestions, int flashcardSetId) {
        List<Flashcard> flashcards = new ArrayList<>();
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {"id", "set_id", "term", "definition"};
            cursor = db.query(DatabaseHelper.TABLE_FLASHCARD, columns, "set_id = ?",
                    new String[]{String.valueOf(flashcardSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Flashcard fc = new Flashcard();
                    fc.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    fc.setFlashcardSetId(cursor.getInt(cursor.getColumnIndexOrThrow("set_id")));
                    fc.setTerm(cursor.getString(cursor.getColumnIndexOrThrow("term")));
                    fc.setDefinition(cursor.getString(cursor.getColumnIndexOrThrow("definition")));
                    flashcards.add(fc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }

        if (flashcards.isEmpty()) {
            Toast.makeText(getActivity(), "No flashcards found for the selected set", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(flashcards);

        List<Flashcard> selectedFlashcards = flashcards.subList(0, Math.min(totalQuestions, flashcards.size()));

        SQLiteDatabase writeDb = quizHelper.getWritableDatabase();
        try {
            for (Flashcard fc : selectedFlashcards) {
                ContentValues cv = new ContentValues();
                //question = definition và answer = term
                cv.put("question", fc.getDefinition());
                cv.put("answer", fc.getTerm());
                cv.put("addedAt", sdf.format(new Date()));
                cv.put("quizSetId", quizSetId);
                writeDb.insert(DatabaseHelper.TABLE_QUIZ_QUESTION, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeDb.close();
        }
    }

}