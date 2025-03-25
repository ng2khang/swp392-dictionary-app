package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.prm392dictionaryapp.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class ManageNoteActivity extends AppCompatActivity{
    EditText inputReportId, inputNote;
    Button btnAddNote, btnShowNote, btnEditNote, btnDeleteNote, btnBackToReport, btnRecommendForUsingReport;
    ListView lvReportNote;
    ArrayList<String> mylist;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.noteContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputReportId = findViewById(R.id.inputReportId);
        inputNote = findViewById(R.id.inputNote);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnShowNote = findViewById(R.id.btnShowNote);
        btnEditNote = findViewById(R.id.btnEditNote);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnBackToReport = findViewById(R.id.btnBackToReport);
        lvReportNote = findViewById(R.id.lvReportNote);
        mylist = new ArrayList<>();
        myAdapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, mylist);
        lvReportNote.setAdapter(myAdapter);
        mydatabase = openOrCreateDatabase("dictionary.db",MODE_PRIVATE,null);
        try {
            String sql = "CREATE TABLE tbReport(" +
                    "ReportId TEXT primary key," +
                    "Date TEXT," +
                    "WordAdded TEXT," +
                    "WordLearned TEXT," +
                    "MostRemember TEXT," +
                    "Note TEXT)";
            mydatabase.execSQL(sql);
        } catch (Exception e){
            Log.e("Error","Table existed");
        }
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ReportId = inputReportId.getText().toString();
                if (TextUtils.isEmpty(ReportId)){
                    Toast.makeText(ManageNoteActivity.this,"Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues myvalue = new ContentValues();
                    myvalue.put("Note", "");
                    int n = mydatabase.update("tbReport", myvalue, "ReportId = ?", new String[]{ReportId});
                    String msg = "";
                    if (n > 0){
                        msg = "Note Deleted!";
                    } else {
                        msg = n + "Deleted fail!";
                    }
                    Toast.makeText(ManageNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });
                btnAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ReportId = inputReportId.getText().toString();
                        String Note = inputNote.getText().toString();
                        if (TextUtils.isEmpty(ReportId) || TextUtils.isEmpty(Note)){
                            Toast.makeText(ManageNoteActivity.this,"Please fill in all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            ContentValues myvalue = new ContentValues();
                            myvalue.put("ReportId", ReportId);
                            myvalue.put("Note", Note);
                            int n = mydatabase.update("tbReport",myvalue,"ReportId = ?", new String[]{ReportId});
                            String msg;
                            if (n == 0){
                                msg = "Fail to add note";
                            } else {
                                msg = "Note" + n + "added successfully";
                            }
                            Toast.makeText(ManageNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnEditNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ReportId = inputReportId.getText().toString();
                        String Note = inputNote.getText().toString();
                        if (TextUtils.isEmpty(ReportId) || TextUtils.isEmpty(Note)){
                            Toast.makeText(ManageNoteActivity.this,"Please fill in all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            ContentValues myvalue = new ContentValues();
                            myvalue.put("ReportId", ReportId);
                            myvalue.put("Note", Note);
                            int n = mydatabase.update("tbReport",myvalue,"ReportId = ?", new String[]{ReportId});
                            String msg;
                            if (n == 0){
                                msg = "Fail to add note";
                            } else {
                                msg = "Note" + n + "added successfully";
                            }
                            Toast.makeText(ManageNoteActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        btnShowNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist.clear();
                Cursor c = mydatabase.query("tbReport",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false){
                    data = c.getString(0) + "    " + c.getString(5);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myAdapter.notifyDataSetChanged();
            }
        });
        btnBackToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageNoteActivity.this, ManageReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
