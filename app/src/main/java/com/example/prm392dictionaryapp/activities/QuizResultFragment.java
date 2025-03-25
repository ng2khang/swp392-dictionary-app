package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import android.icu.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizResultFragment extends Fragment {
    private TextView tvScore, tvPercentage, tvCorrectAnswers, tvWrongAnswers, tvTimeTaken, tvCompletedAt;
    private Button btnBack;
    private DatabaseHelper quizHelper;
    private int quizSetId;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvScore = view.findViewById(R.id.tv_score);
        tvPercentage = view.findViewById(R.id.tv_percentage);
        tvCorrectAnswers = view.findViewById(R.id.tv_correct_answers);
        tvWrongAnswers = view.findViewById(R.id.tv_wrong_answers);
        tvTimeTaken = view.findViewById(R.id.tv_time_taken);
        tvCompletedAt = view.findViewById(R.id.tv_completed_at);
        btnBack = view.findViewById(R.id.btn_back_to_list);

        quizHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);

        if (getArguments() != null) {
            boolean saveResult = getArguments().getBoolean("saveResult", false);
            if (!saveResult) {
                int totalQuestions = getArguments().getInt("totalQuestions", 0);
                int correctAnswers = getArguments().getInt("correctAnswers", 0);
                int timeTakenMinutes = getArguments().getInt("timeTakenMinutes", 0);
                int timeTakenSeconds = getArguments().getInt("timeTakenSeconds", 0);
                quizSetId = getArguments().getInt("quizSetId", -1);

                int wrongAnswers = totalQuestions - correctAnswers;
                int percentage = totalQuestions > 0 ? (correctAnswers * 100) / totalQuestions : 0;

                tvScore.setText(String.format("%d/%d", correctAnswers, totalQuestions));
                tvPercentage.setText(String.format("%d%%", percentage));
                tvCorrectAnswers.setText(String.format("Correct Answers: %d", correctAnswers));
                tvWrongAnswers.setText(String.format("Wrong Answers: %d", wrongAnswers));
                tvTimeTaken.setText(String.format("Time Taken: %02d:%02d", timeTakenMinutes, timeTakenSeconds));

                Date currentTime = new Date();
                tvCompletedAt.setText(String.format("Completed At: %s", SDF.format(currentTime)));

                if (quizSetId != -1) {
                    saveQuizResult(correctAnswers, currentTime);
                }
            }
        }

        btnBack.setText("Back to History");
        btnBack.setOnClickListener(v -> {
            // Navigate to QuizHistoryFragment
            QuizHistoryFragment historyFragment = new QuizHistoryFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, historyFragment)
                    .commit();
        });
    }

    private void saveQuizResult(int score, Date completedAt) {
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quizSetId", quizSetId);
        values.put("score", score);
        values.put("isCompleted", 1);
        values.put("completedAt", SDF.format(completedAt));
        db.insert(DatabaseHelper.TABLE_QUIZ_RESULT, null, values);
        db.close();

        // Notify QuizHistoryFragment to refresh
        Bundle result = new Bundle();
        result.putBoolean("reload", true);
        getParentFragmentManager().setFragmentResult("refreshQuizHistory", result);
    }
} 