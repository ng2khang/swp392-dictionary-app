package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.Category;
import com.example.prm392dictionaryapp.entities.Vocabulary;
import com.example.prm392dictionaryapp.utils.VocabularyDAO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditVocabActivity extends AppCompatActivity {

    TextInputEditText inputWord, inputPro, inputMean, inputExample;
    AutoCompleteTextView AuCategory;
    Vocabulary vocab;
    VocabularyDAO vocabSqlite;
    TextView word;
    int selectedCategoryId = -1;

    ImageView btnback, btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vocab_detail_createupdate_screen);
        vocabSqlite = new VocabularyDAO(this);

        inputWord = findViewById(R.id.inputWord);
        inputPro = findViewById(R.id.inputPro);
        inputMean = findViewById(R.id.inputMean);
        inputExample = findViewById(R.id.inputExample);
        word = findViewById(R.id.word);
        word.setText("");

        btnback = findViewById(R.id.btnback);
        btnSave = findViewById(R.id.btnSave);

        AuCategory = findViewById(R.id.autoCompleteCategory);

        List<Category> categoriesList = vocabSqlite.getAllCategory();
        List<String> categoryNames = new ArrayList<>();
        Map<String, Integer> categoryMap = new HashMap<>();

        for (Category category : categoriesList) {
            categoryNames.add(category.getCategoryName());
            categoryMap.put(category.getCategoryName(), category.getCategoryId()); // Lưu ánh xạ
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categoryNames);
        AuCategory.setAdapter(adapter);

        AuCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            selectedCategoryId = categoryMap.get(selectedCategory);
        });

        int wordId = getIntent().getIntExtra("wordId", -1);
        if (wordId != -1) {
            vocab = vocabSqlite.getVocabularyById(wordId);
            if (vocab != null) {
                inputWord.setText(vocab.getWord());
                word.setText(vocab.getWord());
                inputPro.setText(vocab.getPronunciation());
                inputMean.setText(vocab.getMeaning());
                inputExample.setText(vocab.getExample());
                AuCategory.setText(vocab.getCategoryName());
                selectedCategoryId = vocab.getCategoryId();
            }
        }

            btnback.setOnClickListener(view -> {
                finish();
            });

            btnSave.setOnClickListener(view -> {
                String word = inputWord.getText().toString();
                String pro = inputPro.getText().toString();
                String mean = inputMean.getText().toString();
                String example = inputExample.getText().toString();
                if (word.isEmpty() || pro.isEmpty() || mean.isEmpty() || example.isEmpty() || selectedCategoryId == -1) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ tất cả các mục!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (wordId != -1) {
                    vocabSqlite.updateVocabulary(wordId, word, mean, pro, example, selectedCategoryId);
                } else {
                    vocabSqlite.addVocabulary(new Vocabulary(word, pro, mean, example, selectedCategoryId));
                }
                Intent intent = new Intent(AddEditVocabActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
    }
}