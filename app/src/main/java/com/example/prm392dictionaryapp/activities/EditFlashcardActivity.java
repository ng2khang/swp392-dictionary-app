package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.Flashcard;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EditFlashcardActivity extends AppCompatActivity {
    EditText editSetTitle;
    LinearLayout flashcardContainer;
    Button btnAddFlashcard, btnSave;
    DatabaseHelper dbHelper;
    int setId;
    List<Flashcard> flashcards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_flashcard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editSetTitle = findViewById(R.id.editSetTitle);
        flashcardContainer = findViewById(R.id.flashcardContainer);
        btnAddFlashcard = findViewById(R.id.btnAddFlashcard);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(getApplicationContext(), "flashcards.db", null, 1);
        setId = getIntent().getIntExtra("SET_ID", -1);
        if (setId == -1) {
            finish(); return;
        }
        // Load set title
        String title = dbHelper.getSetTitleById(setId);
        editSetTitle.setText(title);

        // Load flashcards
        flashcards = dbHelper.getFlashcardsBySetId(setId);
        for (Flashcard fc : flashcards) {
            addFlashcardView(fc.getTerm(), fc.getDefinition());
        }

        btnAddFlashcard.setOnClickListener(v -> addFlashcardView("", ""));
        btnSave.setOnClickListener(v -> saveChanges());
    }
    private void addFlashcardView(String term, String def) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_flashcard, null);
        EditText termText = view.findViewById(R.id.edtTerm);
        EditText defText = view.findViewById(R.id.edtDefinition);
        termText.setText(term);
        defText.setText(def);
        flashcardContainer.addView(view);
    }

    private void saveChanges() {
        String newTitle = editSetTitle.getText().toString().trim();
        if (newTitle.isEmpty()) {
            Toast.makeText(this, "Please enter the Flashcard Set name", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Pair<String, String>> updatedFlashcards = new ArrayList<>();
        for (int i = 0; i < flashcardContainer.getChildCount(); i++) {
            View v = flashcardContainer.getChildAt(i);
            EditText term = v.findViewById(R.id.edtTerm);
            EditText def = v.findViewById(R.id.edtDefinition);
            String termText = term.getText().toString().trim();
            String defText = def.getText().toString().trim();
            if (!termText.isEmpty() && !defText.isEmpty()) {
                updatedFlashcards.add(new Pair<>(termText, defText));
            }
        }

        dbHelper.updateSet(setId, newTitle, updatedFlashcards);
        Toast.makeText(this, "Flashcard set edited", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditFlashcardActivity.this, ListSetActivity.class);
        startActivity(intent);
        finish();
    }
}