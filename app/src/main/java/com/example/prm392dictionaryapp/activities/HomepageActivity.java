package com.example.prm392dictionaryapp.activities;
import com.example.prm392dictionaryapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        Button btnReport = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, DailyReportActivity.class);
                startActivity(intent);
            }
        });

        Button btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        Button btnFlashcard = findViewById(R.id.btnFlashcard);
        btnFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, ListSetActivity.class);
                startActivity(intent);
            }
        });

        Button btnWord = findViewById(R.id.btnWord);
        btnWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, MainVocabularyActivity.class);
                startActivity(intent);
            }
        });

        Button btnGrammar = findViewById(R.id.btnGrammar);
        btnGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển hướng sang GrammarManageActivity
                Intent intent = new Intent(HomepageActivity.this, ManageGrammarActivity.class);
                startActivity(intent);
            }
        });

        Button btnQuiz = findViewById(R.id.btnQuiz);
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}