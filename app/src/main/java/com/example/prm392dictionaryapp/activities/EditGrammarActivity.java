package com.example.prm392dictionaryapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.utils.GrammarDatabaseManager;

public class EditGrammarActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editExample;
    private GrammarDatabaseManager dbManager;
    private int grammarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grammar);

        editTitle = findViewById(R.id.editText_title);
        editDescription = findViewById(R.id.editText_description);
        editExample = findViewById(R.id.editText_example);

        dbManager = new GrammarDatabaseManager(this);

        // Lấy id ngữ pháp từ Intent
        grammarId = getIntent().getIntExtra("id", -1);

        // Tải thông tin ngữ pháp từ cơ sở dữ liệu
        loadGrammarDetails();

        Button btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();
                String example = editExample.getText().toString();

                if (!title.isEmpty() && !description.isEmpty() && !example.isEmpty()) {
                    // Cập nhật ngữ pháp vào cơ sở dữ liệu
                    dbManager.updateGrammar(grammarId, title, description, example);
                    Toast.makeText(EditGrammarActivity.this, "Ngữ pháp đã được cập nhật", Toast.LENGTH_SHORT).show();
                    finish();  // Trở lại màn hình trước
                } else {
                    Toast.makeText(EditGrammarActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadGrammarDetails() {
        // Sử dụng grammarId để lấy thông tin ngữ pháp từ cơ sở dữ liệu
        Cursor cursor = dbManager.getGrammarById(grammarId);
        if (cursor != null && cursor.moveToFirst()) {
            // Kiểm tra xem cột có hợp lệ không
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int exampleIndex = cursor.getColumnIndex("example");

            // Kiểm tra nếu getColumnIndex() trả về -1
            if (titleIndex == -1 || descriptionIndex == -1 || exampleIndex == -1) {
                Toast.makeText(this, "Cột không tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                return;  // Dừng nếu cột không hợp lệ
            }

            // Lấy thông tin và điền vào các ô nhập liệu
            editTitle.setText(cursor.getString(titleIndex));
            editDescription.setText(cursor.getString(descriptionIndex));
            editExample.setText(cursor.getString(exampleIndex));
        }
    }
}
