package com.example.prm392dictionaryapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.example.prm392dictionaryapp.entities.Flashcard;
import com.example.prm392dictionaryapp.entities.FlashcardSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "flashcards.db";
    private static final int DB_VERSION = 1;
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sets(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE TABLE flashcards(id INTEGER PRIMARY KEY AUTOINCREMENT, set_id INTEGER, term TEXT, definition TEXT, FOREIGN KEY(set_id) REFERENCES sets(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS flashcards");
        db.execSQL("DROP TABLE IF EXISTS sets");
        onCreate(db);
    }

    public long insertSet(String title, List<Pair<String, String>> flashcards) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        long setId = db.insert("sets", null, values);

        for (Pair<String, String> fc : flashcards) {
            ContentValues fcValues = new ContentValues();
            fcValues.put("set_id", setId);
            fcValues.put("term", fc.first);
            fcValues.put("definition", fc.second);
            db.insert("flashcards", null, fcValues);
        }
        return setId;
    }

    public List<FlashcardSet> getAllSetsWithFlashcardCount() {
        List<FlashcardSet> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT sets.id, sets.title, COUNT(flashcards.id) AS total " +
                        "FROM sets LEFT JOIN flashcards ON sets.id = flashcards.set_id " +
                        "GROUP BY sets.id", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                int count = cursor.getInt(2);
                list.add(new FlashcardSet(id, title, count));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
    public List<Flashcard> getFlashcardsBySetId(int setId) {
        List<Flashcard> flashcards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT term, definition FROM flashcards WHERE set_id = ?", new String[]{String.valueOf(setId)});

        if (cursor.moveToFirst()) {
            do {
                String term = cursor.getString(0);
                String definition = cursor.getString(1);
                flashcards.add(new Flashcard(term, definition));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return flashcards;
    }
    public String getSetTitleById(int setId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT title FROM sets WHERE id = ?", new String[]{String.valueOf(setId)});
        String title = "";
        if (c.moveToFirst()) title = c.getString(0);
        c.close();
        return title;
    }

    public void updateSet(int setId, String newTitle, List<Pair<String, String>> flashcards) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        db.update("sets", values, "id=?", new String[]{String.valueOf(setId)});

        db.delete("flashcards", "set_id=?", new String[]{String.valueOf(setId)});
        for (Pair<String, String> fc : flashcards) {
            ContentValues fcValues = new ContentValues();
            fcValues.put("set_id", setId);
            fcValues.put("term", fc.first);
            fcValues.put("definition", fc.second);
            db.insert("flashcards", null, fcValues);
        }
    }
}
