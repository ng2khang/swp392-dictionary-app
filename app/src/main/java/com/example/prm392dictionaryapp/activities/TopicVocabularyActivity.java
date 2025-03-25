package com.example.prm392dictionaryapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.VocabularyAdapter;
import com.example.prm392dictionaryapp.adapters.VocabularyCategoryAdapter;
import com.example.prm392dictionaryapp.entities.CategoryHeader;
import com.example.prm392dictionaryapp.entities.ListItem;
import com.example.prm392dictionaryapp.entities.Vocabulary;
import com.example.prm392dictionaryapp.entities.VocabularyItem;
import com.example.prm392dictionaryapp.utils.VocabularyDAO;

import java.util.ArrayList;

public class TopicVocabularyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Vocabulary> vocabList = new ArrayList<>();
    VocabularyDAO vocabSqlite;

    Button btnAddTopic, btnAll;
    ImageView btnMenu;

    VocabularyCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.vocab_topicscreen);

        vocabSqlite = new VocabularyDAO(this);
        recyclerView = findViewById(R.id.VocabViewHolder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddTopic = findViewById(R.id.btnAddTopic);
        btnAll = findViewById(R.id.btnAll);
        btnMenu = findViewById(R.id.Menu);

        vocabList = vocabSqlite.getAllVocabularies();

        ArrayList<ListItem> listItems = new ArrayList<>();

        String currentCategory = "";
        for (Vocabulary vocab : vocabList) {

            if (vocab.getCategoryName() != null && !vocab.getCategoryName().equals(currentCategory)) {
                currentCategory = vocab.getCategoryName();
                listItems.add(new CategoryHeader(currentCategory));
            }
            if (vocab.getWord() != null) {
                listItems.add(new VocabularyItem(vocab));
            }
        }

        adapter = new VocabularyCategoryAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

        btnAddTopic.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm Chủ Đề Mới");

            final EditText input = new EditText(this);
            input.setHint("Nhập tên chủ đề...");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Nút Xác Nhận
            builder.setPositiveButton("Thêm", (dialog, which) -> {
                String topicName = input.getText().toString().trim();
                if (!topicName.isEmpty()) {
                    String result = vocabSqlite.addNewCategory(topicName);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Tên chủ đề không được để trống!", Toast.LENGTH_SHORT).show();
                }
            });

            // Nút Hủy
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        btnAll.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainVocabularyActivity.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(view -> {
            Intent intent = new Intent(TopicVocabularyActivity.this, HomepageActivity.class);
            startActivity(intent);
        });
    }
}