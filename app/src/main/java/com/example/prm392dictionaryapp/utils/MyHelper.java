package com.example.prm392dictionaryapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class MyHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_QUIZ_SET = "quiz_set";
    public static final String TABLE_QUIZ_QUESTION = "quiz_question";
    public static final String TABLE_QUIZ_RESULT = "quiz_result";
    public static final String TABLE_FLASHCARD_SET = "flashcard_set";
    public static final String TABLE_FLASHCARD = "flashcard";

    public MyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
        //quiz_set
        String CREATE_QUIZ_SET_TABLE = "CREATE TABLE "+TABLE_QUIZ_SET +" ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, description TEXT, totalQuestion INT, quizTime INT, flashcardSetId INT, createdAt DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(flashcardSetId) REFERENCES " + TABLE_FLASHCARD_SET + " (id) ON DELETE CASCADE)";
        ;
        db.execSQL(CREATE_QUIZ_SET_TABLE);

        //quiz_question
        String CREATE_QUIZ_QUESTION_TABLE = "CREATE TABLE "+ TABLE_QUIZ_QUESTION +" (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question TEXT, answer TEXT, addedAt DATETIME, quizSetId INT, " +
                "FOREIGN KEY(quizSetId) REFERENCES " + TABLE_QUIZ_SET + " (id) ON DELETE CASCADE)";
        db.execSQL(CREATE_QUIZ_QUESTION_TABLE);

        //quiz_result
        String CREATE_QUIZ_RESULT_TABLE = "CREATE TABLE " + TABLE_QUIZ_RESULT +" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quizSetId INT , score INT, isCompleted INT, completedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (quizSetId) REFERENCES " + TABLE_QUIZ_SET + " (id) ON DELETE CASCADE)";
        db.execSQL(CREATE_QUIZ_RESULT_TABLE);

            db.execSQL("CREATE TABLE "+TABLE_FLASHCARD_SET+"(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            db.execSQL("CREATE TABLE "+TABLE_FLASHCARD+"(id INTEGER PRIMARY KEY AUTOINCREMENT, flashcardSetId INTEGER, term TEXT, definition TEXT, " +
                    "FOREIGN KEY(flashcardSetId) REFERENCES "+TABLE_FLASHCARD_SET+"(id) ON DELETE CASCADE)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_QUESTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_SET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD_SET);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
