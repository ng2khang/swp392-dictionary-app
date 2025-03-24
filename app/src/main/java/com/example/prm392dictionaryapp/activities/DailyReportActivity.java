package com.example.prm392dictionaryapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class DailyReportActivity extends AppCompatActivity{

    Button btnShowRP;
    Button btnRecommendForUsingReport;
    ListView lvReportRP;
    ArrayList<String> mylist;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnShowRP = findViewById(R.id.btnShowRP);
        btnRecommendForUsingReport = findViewById(R.id.btnRecommendForUsingReport);
        lvReportRP = findViewById(R.id.lvReportRP);
        mylist = new ArrayList<>();
        myAdapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, mylist);
        lvReportRP.setAdapter(myAdapter);
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
        btnShowRP.setOnClickListener(new View.OnClickListener() {
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
                    data = c.getString(0) + " " + c.getString(1) + "     " +
                            c.getString(2) + "         " + c.getString(3) + "            " +
                            c.getString(4);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myAdapter.notifyDataSetChanged();
            }
        });

        Button btnManageRecord = findViewById(R.id.btnManageRecord);
        btnManageRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportActivity.this, ManageReportActivity.class);
                startActivity(intent);
            }
        });

        Button btnToNote = findViewById(R.id.btnToNote);
        btnToNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportActivity.this, ManageNoteActivity.class);
                startActivity(intent);
            }
        });

        btnRecommendForUsingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReportActivity.this, RecommendForUsingReport.class);
                startActivity(intent);
            }
        });

    }
}
