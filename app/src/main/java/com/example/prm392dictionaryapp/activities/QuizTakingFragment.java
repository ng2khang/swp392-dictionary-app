package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.QuizQuestion;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizTakingFragment extends Fragment {

    private View view;
    private TextView tvTimer, tvQuestionNumber, tvQuestion;
    private RadioGroup rgAnswers;
    private RadioButton rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;
    private Button btnPrevious, btnNext, btnFinish;
    private DatabaseHelper quizHelper;
    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private CountDownTimer timer;
    private int quizTimeInMinutes;
    private int quizSetId;
    private List<String> userAnswers;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());


    public QuizTakingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_taking, container, false);

        // Initialize views
        tvTimer = view.findViewById(R.id.tv_timer);
        tvQuestionNumber = view.findViewById(R.id.tv_question_number);
        tvQuestion = view.findViewById(R.id.tv_question);
        rgAnswers = view.findViewById(R.id.rg_answers);
        rbAnswer1 = view.findViewById(R.id.rb_answer_1);
        rbAnswer2 = view.findViewById(R.id.rb_answer_2);
        rbAnswer3 = view.findViewById(R.id.rb_answer_3);
        rbAnswer4 = view.findViewById(R.id.rb_answer_4);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        btnFinish = view.findViewById(R.id.btn_finish);

        // Initialize database helper
        quizHelper = new DatabaseHelper(getActivity(), "flashcards.db", null, 1);

        // Get quiz set ID from arguments
        if (getArguments() != null) {
            quizSetId = getArguments().getInt("quizSetId", -1);
            loadQuizQuestions();
            loadQuizTime();
        }

        // Set up button click listeners
        btnNext.setOnClickListener(v -> submitAnswerAndNext());
        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                showCurrentQuestion();
            }
        });
        btnFinish.setOnClickListener(v -> showFinishConfirmation());

        return view;
    }

    private void loadQuizTime() {
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_SET, new String[]{"quizTime"},
                    "id = ?", new String[]{String.valueOf(quizSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                quizTimeInMinutes = cursor.getInt(cursor.getColumnIndexOrThrow("quizTime"));
                startTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void startTimer() {
        long totalTimeInMillis = quizTimeInMinutes * 60 * 1000;
        timer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                showQuizResult();
            }
        }.start();
    }

    private void loadQuizQuestions() {
        questionList = new ArrayList<>();
        userAnswers = new ArrayList<>();
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {"id", "question", "answer"};
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_QUESTION, columns, "quizSetId = ?",
                    new String[]{String.valueOf(quizSetId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    QuizQuestion question = new QuizQuestion();
                    question.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow("question")));
                    question.setAnswer(cursor.getString(cursor.getColumnIndexOrThrow("answer")));
                    questionList.add(question);
                    userAnswers.add("");
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        showCurrentQuestion();
    }

    private void showCurrentQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            QuizQuestion question = questionList.get(currentQuestionIndex);
            tvQuestionNumber.setText(String.format("Question %d/%d", currentQuestionIndex + 1, questionList.size()));
            tvQuestion.setText(question.getQuestion());

            // Generate random answers
            List<String> answers = generateRandomAnswers(question.getAnswer());
            rbAnswer1.setText(answers.get(0));
            rbAnswer2.setText(answers.get(1));
            rbAnswer3.setText(answers.get(2));
            rbAnswer4.setText(answers.get(3));

            // Clear previous selection
            rgAnswers.clearCheck();

            // Restore user's previous answer if exists
            String previousAnswer = userAnswers.get(currentQuestionIndex);
            if (previousAnswer != null && !previousAnswer.isEmpty()) {
                if (rbAnswer1.getText().toString().equals(previousAnswer)) {
                    rbAnswer1.setChecked(true);
                } else if (rbAnswer2.getText().toString().equals(previousAnswer)) {
                    rbAnswer2.setChecked(true);
                } else if (rbAnswer3.getText().toString().equals(previousAnswer)) {
                    rbAnswer3.setChecked(true);
                } else if (rbAnswer4.getText().toString().equals(previousAnswer)) {
                    rbAnswer4.setChecked(true);
                }
            }

            // Update button states
            btnPrevious.setEnabled(currentQuestionIndex > 0);
            btnNext.setEnabled(currentQuestionIndex < questionList.size() - 1 );
        } else {
            showQuizResult();
        }
    }

    private List<String> generateRandomAnswers(String correctAnswer) {
        List<String> answers = new ArrayList<>();
        answers.add(correctAnswer);

        // Get other random answers from other questions
        SQLiteDatabase db = quizHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {"answer"};
            cursor = db.query(DatabaseHelper.TABLE_QUIZ_QUESTION, columns,
                    "quizSetId = ? AND answer != ?", 
                    new String[]{String.valueOf(quizSetId), correctAnswer}, 
                    null, null, "RANDOM() LIMIT 3");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    answers.add(cursor.getString(cursor.getColumnIndexOrThrow("answer")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        // Shuffle answers
        Collections.shuffle(answers);
        return answers;
    }

    private void submitAnswerAndNext() {
        int selectedId = rgAnswers.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getActivity(), "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = view.findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();

        userAnswers.set(currentQuestionIndex, selectedAnswer);

        currentQuestionIndex++;
        showCurrentQuestion();
    }

    private void showQuizResult() {
        if (timer != null) {
            timer.cancel();
        }

        // Calculate score
        correctAnswers = 0;
        for (int i = 0; i < questionList.size(); i++) {
            String userAnswer = userAnswers.get(i);
            String correctAnswer = questionList.get(i).getAnswer();
            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                correctAnswers++;
            }
        }

        QuizResultFragment resultFragment = new QuizResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("totalQuestions", questionList.size());
        bundle.putInt("correctAnswers", correctAnswers);
        bundle.putInt("timeTakenMinutes", quizTimeInMinutes - Integer.parseInt(tvTimer.getText().toString().split(":")[0]));
        bundle.putInt("timeTakenSeconds", 60 - Integer.parseInt(tvTimer.getText().toString().split(":")[1]));
        bundle.putInt("quizSetId", quizSetId);
        resultFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showFinishConfirmation() {
        int unansweredCount = 0;
        for (String answer : userAnswers) {
            if (answer == null || answer.isEmpty()) {
                unansweredCount++;
            }
        }

        String message = unansweredCount == 0 ?
                "Are you sure you want to finish the quiz?" :
                String.format("You have %d unanswered question(s). Are you sure you want to finish the quiz?", unansweredCount);

        new AlertDialog.Builder(getActivity())
                .setTitle("Finish Quiz")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> finishQuiz())
                .setNegativeButton("No", null)
                .show();
    }

    private void finishQuiz() {
        if (timer != null) {
            timer.cancel();
        }

        // Save quiz result to database
        Date completedAt = new Date();
        SQLiteDatabase db = quizHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quizSetId", quizSetId);
        values.put("score", correctAnswers);
        values.put("isCompleted", 1);
        values.put("completedAt", SDF.format(completedAt));
        db.insert(DatabaseHelper.TABLE_QUIZ_RESULT, null, values);
        db.close();

        // Show quiz result
        QuizResultFragment resultFragment = new QuizResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("totalQuestions", questionList.size());
        bundle.putInt("correctAnswers", correctAnswers);
        bundle.putInt("timeTakenMinutes", quizTimeInMinutes - Integer.parseInt(tvTimer.getText().toString().split(":")[0]));
        bundle.putInt("timeTakenSeconds", 60 - Integer.parseInt(tvTimer.getText().toString().split(":")[1]));
        bundle.putInt("quizSetId", quizSetId);
        resultFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}