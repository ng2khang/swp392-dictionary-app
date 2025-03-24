package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.content.Intent;

public class ManageReportActivity extends AppCompatActivity {
    EditText inputReportId, inputDate, inputWordAdded, inputWordLearned, inputMostRemember, inputNote;
    Button btnAddNewReport, btnShowReport, btnEditReport, btnDeleteReport, btnToNote, btnRecommendForUsingReport;
    ListView lvReport;
    ArrayList<String> mylist;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputReportId = findViewById(R.id.inputReportId);
        inputDate = findViewById(R.id.inputDate);
        inputWordAdded = findViewById(R.id.inputWordAdded);
        inputWordLearned = findViewById(R.id.inputWordLearned);
        inputMostRemember = findViewById(R.id.inputMostRemember);
        //inputNote = findViewById(R.id.inputNote);
        btnAddNewReport = findViewById(R.id.btnAddNewReport);
        btnShowReport = findViewById(R.id.btnShowReport);
        btnEditReport = findViewById(R.id.btnEditReport);
        btnDeleteReport = findViewById(R.id.btnDeleteReport);
        btnToNote = findViewById(R.id.btnToNote);
        btnRecommendForUsingReport = findViewById(R.id.btnRecommendForUsingReport1);
        lvReport = findViewById(R.id.lvReport);
        mylist = new ArrayList<>();
        myAdapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, mylist);
        lvReport.setAdapter(myAdapter);
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
        btnAddNewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ReportId = inputReportId.getText().toString();
                String Date = inputDate.getText().toString();
                String WordAdded = inputWordAdded.getText().toString();
                String WordLearned = inputWordLearned.getText().toString();
                String MostRemember = inputMostRemember.getText().toString();
                //String Note = inputNote.getText().toString();

                if (TextUtils.isEmpty(ReportId) || TextUtils.isEmpty(Date) || TextUtils.isEmpty(WordAdded) ||
                        TextUtils.isEmpty(WordLearned) || TextUtils.isEmpty(MostRemember)) {
                    Toast.makeText(ManageReportActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues myvalue = new ContentValues();
                    myvalue.put("ReportId", ReportId);
                    myvalue.put("Date", Date);
                    myvalue.put("WordAdded", WordAdded);
                    myvalue.put("WordLearned", WordLearned);
                    myvalue.put("MostRemember", MostRemember);
                    //myvalue.put("Note", Note);

                    String msg = "";
                    if (mydatabase.insert("tbReport", null, myvalue) == -1) {
                        msg = "Add report fail!";
                    } else {
                        msg = "Add report successfully!";
                    }
                    Toast.makeText(ManageReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ReportId = inputReportId.getText().toString();
                if (TextUtils.isEmpty(ReportId)){
                    Toast.makeText(ManageReportActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int n = mydatabase.delete("tbReport", "ReportId = ?", new String[]{ReportId});
                    String msg = "";
                    if (n == 0){
                        msg = "Delete report fail!";
                    } else {
                        msg = n + "Report is deleted!";
                    }
                    Toast.makeText(ManageReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEditReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ReportId = inputReportId.getText().toString();
                String Date = inputDate.getText().toString();
                String WordAdded = inputWordAdded.getText().toString();
                String WordLearned = inputWordLearned.getText().toString();
                String MostRemember = inputMostRemember.getText().toString();
                //String Note = inputNote.getText().toString();

                if (TextUtils.isEmpty(ReportId) || TextUtils.isEmpty(Date) || TextUtils.isEmpty(WordAdded) ||
                        TextUtils.isEmpty(WordLearned) || TextUtils.isEmpty(MostRemember)) {
                    Toast.makeText(ManageReportActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues myvalue = new ContentValues();
                    myvalue.put("ReportId", ReportId);
                    myvalue.put("Date", Date);
                    myvalue.put("WordAdded", WordAdded);
                    myvalue.put("WordLearned", WordLearned);
                    myvalue.put("MostRemember", MostRemember);
                    //myvalue.put("Note", Note);

                    int n = mydatabase.update("tbReport", myvalue, "ReportId = ?", new String[]{ReportId});
                    String msg = "";
                    if (n == 0) {
                        msg = "Fail to edit report!";
                    } else {
                        msg = "Report updated successfully!";
                    }
                    Toast.makeText(ManageReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowReport.setOnClickListener(new View.OnClickListener() {
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
                    data = c.getString(0) + "    " + c.getString(1) + "    " +
                            c.getString(2) + "    " + c.getString(3) + "    " +
                            c.getString(4);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myAdapter.notifyDataSetChanged();
            }
        });
        btnToNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageReportActivity.this, ManageNoteActivity.class);
                startActivity(intent);
            }
        });
        btnRecommendForUsingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageReportActivity.this, RecommendForUsingReport.class);
                startActivity(intent);
            }
        });
    }
}
