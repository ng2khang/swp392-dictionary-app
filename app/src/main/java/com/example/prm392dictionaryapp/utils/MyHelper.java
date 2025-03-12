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
    public static final String TABLE_QUIZ_ANSWER = "quiz_answer";
    public MyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
        //quiz_set
        String CREATE_QUIZ_SET_TABLE = "CREATE TABLE "+TABLE_QUIZ_SET +" ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, description TEXT, totalQuestion INT, quizTime INT, createdAt DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_QUIZ_SET_TABLE);

        //quiz_question
        String CREATE_QUIZ_QUESTION_TABLE = "CREATE TABLE "+ TABLE_QUIZ_QUESTION +" (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question TEXT, addedAt DATETIME, quizSetId INT, " +
                "FOREIGN KEY(quizSetId) REFERENCES " + TABLE_QUIZ_SET + " (id) ON DELETE CASCADE)";
        db.execSQL(CREATE_QUIZ_QUESTION_TABLE);

        //quiz_answer
        String CREATE_QUIZ_ANSWER_TABLE = "CREATE TABLE " + TABLE_QUIZ_ANSWER + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "answer TEXT, questionId INT, isCorrected INT," +
                "FOREIGN KEY(questionId) REFERENCES "+ TABLE_QUIZ_QUESTION +" (id) ON DELETE CASCADE)" ;
        db.execSQL(CREATE_QUIZ_ANSWER_TABLE);

        //quiz_result
        String CREATE_QUIZ_RESULT_TABLE = "CREATE TABLE " + TABLE_QUIZ_RESULT +" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quizSetId INT , score INT, isCompleted INT, completedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (quizSetId) REFERENCES " + TABLE_QUIZ_SET + " (id) ON DELETE CASCADE)";
        db.execSQL(CREATE_QUIZ_RESULT_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_QUESTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_ANSWER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_SET);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
