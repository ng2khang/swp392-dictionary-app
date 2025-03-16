package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.FlashCardSetAdapter;
import com.example.prm392dictionaryapp.entities.FlashcardSet;
import com.example.prm392dictionaryapp.utils.DatabaseHelper;

import java.util.List;

public class ListSetActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FlashCardSetAdapter flashCardSetAdapter;
    List<FlashcardSet> setList;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_set);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewSets);
        dbHelper = new DatabaseHelper(getApplicationContext(), "flashcards.db", null, 1);
        setList = dbHelper.getAllSetsWithFlashcardCount();

        flashCardSetAdapter = new FlashCardSetAdapter(this, setList, flashcardSet -> {
            Intent intent = new Intent(ListSetActivity.this, FlashCardActivity.class);
            intent.putExtra("SET_ID", flashcardSet.getId());
            startActivity(intent);
        }, flashcardSet -> {
            Intent intent = new Intent(ListSetActivity.this, EditFlashcardActivity.class);
            intent.putExtra("SET_ID", flashcardSet.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flashCardSetAdapter);
        Button btnAddNewSet = findViewById(R.id.btnAddNewSet);
        btnAddNewSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListSetActivity.this, CreateSetActivity.class);
                startActivity(intent);
            }
        });
    }
}