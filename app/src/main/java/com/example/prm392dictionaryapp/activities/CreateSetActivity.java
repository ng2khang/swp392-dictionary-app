package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateSetActivity extends AppCompatActivity {
    private EditText editTextSetTitle;
    private LinearLayout flashcardContainer;
    private Button btnSaveSet;
    private ImageButton btnAddFlashcard, btnBack;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_set);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextSetTitle = findViewById(R.id.editTextSetTitle);
        flashcardContainer = findViewById(R.id.flashcardContainer);
        btnSaveSet = findViewById(R.id.btnSaveSet);
        btnAddFlashcard = findViewById(R.id.btnAddFlashcard);
        btnBack = findViewById(R.id.btnBackToList);
        dbHelper = new DatabaseHelper(getApplicationContext(), "flashcards.db", null, 1);

        btnAddFlashcard.setOnClickListener(v -> addFlashcardView());
        btnSaveSet.setOnClickListener(v -> saveSet());
        addFlashcardView();
        addFlashcardView();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSetActivity.this, ListSetActivity.class);
                startActivity(intent);
            }
        });

    }
    private void addFlashcardView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_flashcard, null);
        flashcardContainer.addView(view);
    }
    private void saveSet() {
        String title = editTextSetTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter the Flashcard Set name", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Pair<String, String>> flashcards = new ArrayList<>();
        for (int i = 0; i < flashcardContainer.getChildCount(); i++) {
            View cardView = flashcardContainer.getChildAt(i);
            EditText term = cardView.findViewById(R.id.edtTerm);
            EditText def = cardView.findViewById(R.id.edtDefinition);

            String termText = term.getText().toString().trim();
            String defText = def.getText().toString().trim();

            if (!termText.isEmpty() && !defText.isEmpty()) {
                flashcards.add(new Pair<>(termText, defText));
            }
        }

        if (flashcards.isEmpty()) {
            Toast.makeText(this, "Add at least one flashcard.", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.insertSet(title, flashcards);
        if (id > 0) {
            Toast.makeText(this, "Flashcard set saved.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateSetActivity.this, ListSetActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Data error!", Toast.LENGTH_SHORT).show();
        }
    }
}