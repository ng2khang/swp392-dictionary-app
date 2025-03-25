package com.example.prm392dictionaryapp.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.QuizHistoryAdapter;
import com.example.prm392dictionaryapp.entities.QuizResult;
import com.example.prm392dictionaryapp.entities.QuizSet;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuizHistoryFragment extends Fragment {
    private View view;
    private RecyclerView rvQuizHistory;
    private QuizHistoryAdapter adapter;
    private ArrayList<QuizResult> quizHistoryList;
    private DatabaseHelper quizHelper;
    private TextView tvNoHistory;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public QuizHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("refreshQuizHistory", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                boolean reload = result.getBoolean("reload", false);
                if (reload) {
                    quizHistoryList.clear();
                    loadQuizHistory();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_history, container, false);

        rvQuizHistory = view.findViewById(R.id.rv_quiz_history);
        rvQuizHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizHistoryList = new ArrayList<>();
        adapter = new QuizHistoryAdapter(quizHistoryList);
        rvQuizHistory.setAdapter(adapter);

        adapter.setOnItemClickListener(new QuizHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QuizResult result) {
                QuizResultFragment resultFragment = new QuizResultFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("quizSetId", result.getQuizSetId());
                bundle.putInt("totalQuestions", getTotalQuestions(result.getQuizSetId()));
                bundle.putInt("correctAnswers", result.getScore());
                bundle.putString("completedAt", SDF.format(result.getCompletedAt()));
                bundle.putBoolean("saveResult", true);
                resultFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, resultFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        quizHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);
        loadQuizHistory();

        tvNoHistory = view.findViewById(R.id.tv_no_history);
        if (quizHistoryList.isEmpty()) {
            tvNoHistory.setVisibility(View.VISIBLE);
            rvQuizHistory.setVisibility(View.GONE);
        } else {
            tvNoHistory.setVisibility(View.GONE);
            rvQuizHistory.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        quizHistoryList.clear();
        loadQuizHistory();
    }

    private void loadQuizHistory() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = quizHelper.getReadableDatabase();
            String query = "SELECT qr.*, qs.title, qs.totalQuestion " +
                    "FROM " + DatabaseHelper.TABLE_QUIZ_RESULT + " qr " +
                    "JOIN " + DatabaseHelper.TABLE_QUIZ_SET + " qs ON qr.quizSetId = qs.id " +
                    "WHERE qr.isCompleted = 1 " +
                    "ORDER BY qr.completedAt DESC";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    int quizSetId = cursor.getInt(cursor.getColumnIndexOrThrow("quizSetId"));
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                    int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted"));
                    String completedAtStr = cursor.getString(cursor.getColumnIndexOrThrow("completedAt"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    int totalQuestion = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));

                    QuizSet set = QuizSet.builder()
                            .title(title)
                            .totalQuestion(totalQuestion)
                            .build();
                    QuizResult result = QuizResult.builder()
                            .id(id)
                            .quizSetId(quizSetId)
                            .score(score)
                            .isCompleted(isCompleted)
                            .completedAt( SDF.parse(completedAtStr))
                            .quizSet(set)
                            .build();
                    quizHistoryList.add(result);
                } while (cursor.moveToNext());
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error loading quiz history: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private int getTotalQuestions(int quizSetId) {
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_SET, new String[]{"totalQuestion"},
                    "id = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return 0;
    }
} 