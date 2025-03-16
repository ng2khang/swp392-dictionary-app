package com.example.prm392dictionaryapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.FlashcardAdapter;
import com.example.prm392dictionaryapp.entities.Flashcard;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.List;

public class FlashCardActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TextView textProgress;
    private DatabaseHelper dbHelper;
    private List<Flashcard> flashcards;
    private FlashcardAdapter adapter;
    private int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewPager = findViewById(R.id.viewPagerFlashcards);
        textProgress = findViewById(R.id.textProgress);
        dbHelper = new DatabaseHelper(getApplicationContext(), "flashcards.db", null, 1);

        setId = getIntent().getIntExtra("SET_ID", -1);

        if (setId != -1) {
            flashcards = dbHelper.getFlashcardsBySetId(setId);
            Toast.makeText(getApplicationContext(), "Hello " + flashcards.size(), Toast.LENGTH_SHORT).show();
            adapter = new FlashcardAdapter(this, flashcards);
            viewPager.setAdapter(adapter);
            updateProgress(0);
        }
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateProgress(position);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
    private void updateProgress(int position) {
        textProgress.setText((position + 1) + " / " + flashcards.size());
    }
}