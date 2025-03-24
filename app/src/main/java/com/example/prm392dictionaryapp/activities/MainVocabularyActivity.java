package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.VocabularyAdapter;
import com.example.prm392dictionaryapp.entities.Vocabulary;
import com.example.prm392dictionaryapp.utils.VocabularyDAO;

import java.util.ArrayList;

public class MainVocabularyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etSearch;
    ArrayList<Vocabulary> vocabList = new ArrayList<>();
    VocabularyDAO vocabSqlite;

    Button btnAdd, btnTopic, btnFavor;
    VocabularyAdapter adepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vocab_mainscreen);


//        Import dữ liệu từ file sample data
//        DatabaseHelper dbHelper = new DatabaseHelper(this, "flashcards.db", null, 2);
//        dbHelper.importSampleData(this);

        recyclerView = findViewById(R.id.VocabViewHolder);
        etSearch = findViewById(R.id.etSearch);
        vocabSqlite = new VocabularyDAO(this);

        btnAdd = findViewById(R.id.btnAddTopic);
        btnTopic = findViewById(R.id.btnAll);
        btnFavor = findViewById(R.id.btnFavor);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData("");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = etSearch.getText().toString();
                loadData(key);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainVocabularyActivity.this, AddEditVocabActivity.class);
            startActivity(intent);
        });

        btnFavor.setOnClickListener(view -> {
            Intent intent = new Intent(MainVocabularyActivity.this, FavoVocabularyActivity.class);
            startActivity(intent);
        });

        btnTopic.setOnClickListener(view -> {
            Intent intent = new Intent(MainVocabularyActivity.this, TopicVocabularyActivity.class);
            startActivity(intent);
        });

    }

    public void loadData(String key){
        vocabList.clear();
        if (key.isEmpty() || key == "") {
//            vocabList.addAll(vocabSqlite.getAllVocabulary());
        } else {
            vocabList.addAll(vocabSqlite.searchByWord(key));
        }

//        if (!vocabList.isEmpty()) {
            adepter = new VocabularyAdapter(vocabList, MainVocabularyActivity.this);
            recyclerView.setAdapter(adepter);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK) {
            String deletedWord = data.getStringExtra("deletedWord");
            if (deletedWord != null) {
                Toast.makeText(this, "Từ '" + deletedWord + "' đã được xóa thành công", Toast.LENGTH_SHORT).show();
                etSearch.setText("");
                loadData("");
            }
        }
    }


}