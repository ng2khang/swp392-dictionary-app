package com.example.prm392dictionaryapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GrammarDatabaseManager {
    private GrammarDatabaseHelper dbHelper;

    public GrammarDatabaseManager(Context context) {
        dbHelper = new GrammarDatabaseHelper(context);
    }

    // Thêm ngữ pháp
    public void addGrammar(String title, String description, String example) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GrammarDatabaseHelper.COLUMN_TITLE, title);
        values.put(GrammarDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(GrammarDatabaseHelper.COLUMN_EXAMPLE, example);
        values.put(GrammarDatabaseHelper.COLUMN_STATUS, 1); // Mặc định là enabled

        db.insert(GrammarDatabaseHelper.TABLE_GRAMMAR, null, values);
        db.close();
    }

    // Cập nhật ngữ pháp
    public void updateGrammar(int id, String title, String description, String example) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GrammarDatabaseHelper.COLUMN_TITLE, title);
        values.put(GrammarDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(GrammarDatabaseHelper.COLUMN_EXAMPLE, example);

        db.update(GrammarDatabaseHelper.TABLE_GRAMMAR, values, GrammarDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Cập nhật trạng thái (disable hoặc enable)
    public void updateGrammarStatus(int id, int status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GrammarDatabaseHelper.COLUMN_STATUS, status);

        db.update(GrammarDatabaseHelper.TABLE_GRAMMAR, values, GrammarDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Lấy tất cả ngữ pháp
    public Cursor getAllGrammar() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(GrammarDatabaseHelper.TABLE_GRAMMAR, null, null, null, null, null, null);
    }
    public void deleteGrammar(int id) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(GrammarDatabaseHelper.TABLE_GRAMMAR, GrammarDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public Cursor getGrammarById(int id) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        // Kiểm tra câu truy vấn SQL của bạn
        String query = "SELECT title, description, example FROM " + GrammarDatabaseHelper.TABLE_GRAMMAR + " WHERE id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(id)});
    }
    public Cursor searchGrammar(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Tìm kiếm ngữ pháp dựa trên tiêu đề, mô tả hoặc ví dụ
        String selection = GrammarDatabaseHelper.COLUMN_TITLE + " LIKE ? OR " +
                GrammarDatabaseHelper.COLUMN_DESCRIPTION + " LIKE ? OR " +
                GrammarDatabaseHelper.COLUMN_EXAMPLE + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};
        return db.query(GrammarDatabaseHelper.TABLE_GRAMMAR, null, selection, selectionArgs, null, null, null);
    }



}
