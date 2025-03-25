package com.example.prm392dictionaryapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.GrammarAdapter;
import com.example.prm392dictionaryapp.utils.GrammarDatabaseManager;

public class ManageGrammarActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editExample, editSearch;
    private GrammarDatabaseManager dbManager;
    private RecyclerView recyclerView;
    private GrammarAdapter grammarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_grammar);

        editTitle = findViewById(R.id.editText_title);
        editDescription = findViewById(R.id.editText_description);
        editExample = findViewById(R.id.editText_example);
        editSearch = findViewById(R.id.editText_search);

        dbManager = new GrammarDatabaseManager(this);

        recyclerView = findViewById(R.id.recyclerView_grammar_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter cho RecyclerView
        Cursor cursor = dbManager.getAllGrammar();
        grammarAdapter = new GrammarAdapter(this, cursor);
        recyclerView.setAdapter(grammarAdapter);

        // Sử dụng TextWatcher để tự động tìm kiếm khi người dùng gõ vào ô tìm kiếm
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần xử lý trước khi thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Lọc danh sách ngữ pháp khi người dùng nhập
                String query = charSequence.toString().trim();
                if (!query.isEmpty()) {
                    // Tìm kiếm ngữ pháp dựa trên từ khóa
                    Cursor searchCursor = dbManager.searchGrammar(query);
                    grammarAdapter.changeCursor(searchCursor);
                } else {
                    // Nếu không có từ khóa tìm kiếm, hiển thị toàn bộ ngữ pháp
                    Cursor allCursor = dbManager.getAllGrammar();
                    grammarAdapter.changeCursor(allCursor);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý sau khi thay đổi
            }
        });

        // Thêm sự kiện cho nút Lưu ngữ pháp
        Button btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            String example = editExample.getText().toString();

            if (!title.isEmpty() && !description.isEmpty() && !example.isEmpty()) {
                dbManager.addGrammar(title, description, example);
                Toast.makeText(ManageGrammarActivity.this, "Ngữ pháp đã được lưu", Toast.LENGTH_SHORT).show();
                // Cập nhật lại danh sách ngữ pháp sau khi thêm mới
                Cursor newCursor = dbManager.getAllGrammar();
                grammarAdapter.changeCursor(newCursor);
                // Xóa dữ liệu trong form
                editTitle.setText("");
                editDescription.setText("");
                editExample.setText("");
            } else {
                Toast.makeText(ManageGrammarActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
