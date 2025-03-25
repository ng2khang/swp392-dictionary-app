package com.example.prm392dictionaryapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GrammarDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "grammar.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GRAMMAR = "grammar";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_EXAMPLE = "example";
    public static final String COLUMN_STATUS = "status";  // Trạng thái (1: active, 0: disabled)

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_GRAMMAR + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_EXAMPLE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_STATUS + " INTEGER DEFAULT 1" + // Trạng thái (1: active, 0: disabled)
                    ");";

    public GrammarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRAMMAR);
        onCreate(db);
    }
}
