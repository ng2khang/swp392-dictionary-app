package com.example.prm392dictionaryapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.Vocabulary;
import com.example.prm392dictionaryapp.utils.VocabularyDAO;

public class VocabDetailActivity extends AppCompatActivity {

    TextView tvWord, word, tvPro, tvMeaning, tvExample, tvCategory;
    ImageView btnback, btnEdit, btnstar, btndelete;

    Vocabulary vocab;
    VocabularyDAO vocabSqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vocab_detailscreen);

        vocabSqlite = new VocabularyDAO(this);
        tvWord = findViewById(R.id.tvWord);
        word = findViewById(R.id.word);
        tvPro = findViewById(R.id.tvPro);
        tvMeaning = findViewById(R.id.tvMeaning);
        tvExample = findViewById(R.id.tvExample);
        tvCategory = findViewById(R.id.tvCategory);

        btnback = findViewById(R.id.btnback);
        btnEdit = findViewById(R.id.btnEdit);
        btnstar = findViewById(R.id.btnstar);
        btndelete = findViewById(R.id.btndelete);

        int wordId = getIntent().getIntExtra("wordId", -1);
        if (wordId != -1) {
            vocab = vocabSqlite.getVocabularyById(wordId);
            boolean isfavor = vocabSqlite.isFavorite(wordId);
            if (vocab != null) {
                tvWord.setText(vocab.getWord());
                word.setText(vocab.getWord());
                tvPro.setText(vocab.getPronunciation());
                tvMeaning.setText(vocab.getMeaning());
                tvExample.setText(vocab.getExample());
                tvCategory.setText(vocab.getCategoryName());
                if (isfavor) {
                    btnstar.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    btnstar.setImageResource(android.R.drawable.btn_star_big_off);
                }
            } else {
                Toast.makeText(this, "Không tìm thấy từ vựng!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Lỗi: Không có từ vựng!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnback.setOnClickListener(view -> {
            finish();
        });

        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(VocabDetailActivity.this, AddEditVocabActivity.class);
            intent.putExtra("wordId", vocab.getId());
            startActivity(intent);
        });

        btndelete.setOnClickListener(view -> {
            new AlertDialog.Builder(VocabDetailActivity.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa từ \"" + vocab.getWord() + "\" không?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        vocabSqlite.deleteWord(wordId);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("deletedWord", word.getText().toString());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnstar.setOnClickListener(view -> {
            if (vocab != null) {
                boolean isFavor = vocabSqlite.isFavorite(vocab.getId());

                if (isFavor) {
                    vocabSqlite.removeFromFavorites(vocab.getId());
                    btnstar.setImageResource(android.R.drawable.btn_star_big_off);
                    Toast.makeText(this, "Đã bỏ khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                } else {
                    vocabSqlite.addToFavorites(vocab.getId());
                    btnstar.setImageResource(android.R.drawable.btn_star_big_on);
                    Toast.makeText(this, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}