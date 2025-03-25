package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class FavoVocabularyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etSearch;
    ArrayList<Vocabulary> vocabList = new ArrayList<>();
    VocabularyDAO vocabSqlite;

    VocabularyAdapter adepter;
    ImageView btnMenu;
    Button btnAll, btnTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vocab_favorscreen);


        recyclerView = findViewById(R.id.VocabViewHolder);
        etSearch = findViewById(R.id.etSearch);
        vocabSqlite = new VocabularyDAO(this);

        btnAll = findViewById(R.id.btnAddTopic);
        btnTopic = findViewById(R.id.btnAll);
        btnMenu = findViewById(R.id.Menu);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData("");

        btnAll.setOnClickListener(view -> {
            Intent intent = new Intent(FavoVocabularyActivity.this, MainVocabularyActivity.class);
            startActivity(intent);
        });

        btnTopic.setOnClickListener(view -> {
            Intent intent = new Intent(FavoVocabularyActivity.this, TopicVocabularyActivity.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(view -> {
            Intent intent = new Intent(FavoVocabularyActivity.this, HomepageActivity.class);
            startActivity(intent);
        });

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
    }

    public void loadData(String key){
        vocabList.clear();
        if (key.isEmpty() || key == "") {
            vocabList.addAll(vocabSqlite.getFavoriteWords());
        } else {
            vocabList.addAll(vocabSqlite.searchFavoriteWords(key));
        }

        adepter = new VocabularyAdapter(vocabList, FavoVocabularyActivity.this);
        recyclerView.setAdapter(adepter);

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