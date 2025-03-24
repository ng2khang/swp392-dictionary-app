package com.example.prm392dictionaryapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm392dictionaryapp.entities.Category;
import com.example.prm392dictionaryapp.entities.Vocabulary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VocabularyDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public VocabularyDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    // Mở kết nối
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Đóng kết nối
    public void close() {
        dbHelper.close();
    }

    public long addVocabulary(Vocabulary vocab) {
        ContentValues values = new ContentValues();
        values.put("word", vocab.getWord());
        values.put("meaning", vocab.getMeaning());
        values.put("pronunciation", vocab.getPronunciation());
        values.put("example", vocab.getExample());
        values.put("category_id", vocab.getCategoryId());

        return db.insert("vocabulary", null, values);
    }

    public List<Vocabulary> getAllVocabulary() {
        List<Vocabulary> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM vocabulary", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToVocabulary(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateVocabulary(int id, String word, String meaning, String pronunciation, String example, int categoryId) {
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("meaning", meaning);
        values.put("pronunciation", pronunciation);
        values.put("example", example);
        values.put("category_id", categoryId);

        int rowsAffected = db.update("vocabulary", values, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public List<Vocabulary> getVocabularyByCategory(int categoryId) {
        List<Vocabulary> vocabList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM vocabulary WHERE category_id = ?", new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                vocabList.add(cursorToVocabulary(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vocabList;
    }

    public Vocabulary getVocabularyById(int wordId) {
        String query = "SELECT v.id, v.word, v.meaning, v.pronunciation, v.example, v.category_id , v.created_at, c.name AS categoryName " +
                "FROM vocabulary v " +
                "LEFT JOIN categories c ON v.category_id = c.id " +
                "WHERE v.id = ? LIMIT 1";

        Cursor cursor = null;
        Vocabulary vocab = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(wordId)});

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                String meaning = cursor.getString(cursor.getColumnIndexOrThrow("meaning"));
                String pronunciation = cursor.getString(cursor.getColumnIndexOrThrow("pronunciation"));
                String example = cursor.getString(cursor.getColumnIndexOrThrow("example"));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));

                vocab = new Vocabulary(id, word, meaning, pronunciation, example, categoryId, createdAt, categoryName);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching vocabulary", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return vocab;
    }

    public boolean isFavorite(int wordId) {
        String query = "SELECT 1 FROM favorite_words WHERE word_id = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(wordId)});

        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    public void addToFavorites(int wordId) {
        ContentValues values = new ContentValues();
        values.put("word_id", wordId);
        db.insert("favorite_words", null, values);
    }

    public void removeFromFavorites(int wordId) {
        db.delete("favorite_words", "word_id = ?", new String[]{String.valueOf(wordId)});
    }

    public List<Vocabulary> searchByWord(String keyword) {
        List<Vocabulary> vocabList = new ArrayList<>();
        String query = "SELECT v.id, v.word, v.meaning, v.category_id, c.name AS category_name " +
                        "FROM vocabulary v " +
                        "LEFT JOIN categories c ON v.category_id = c.id " +
                        "WHERE v.word LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%"});
        while (cursor.moveToNext()) {
            vocabList.add(cursorToVocabulary(cursor));
        }
        cursor.close();
        return vocabList;
    }

    public ArrayList<Vocabulary> getAllVocabularies() {
        ArrayList<Vocabulary> vocabList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT v.id AS id, v.word, v.meaning, c.id AS category_id, c.name AS category_name " +
                                "FROM categories c " +
                                "LEFT JOIN vocabulary v ON c.id = v.category_id " +
                                "ORDER BY c.name;", null);

        while (cursor.moveToNext()) {
            vocabList.add(cursorToVocabulary(cursor));
        }
        cursor.close();
        return vocabList;
    }

    public List<Vocabulary> getFavoriteWords() {
        List<Vocabulary> favoriteWords = new ArrayList<>();
        String query = "SELECT v.id, v.word, v.meaning, v.category_id, c.name AS category_name " +
                "FROM vocabulary v " +
                "INNER JOIN favorite_words f ON v.id = f.word_id " +
                "LEFT JOIN categories c ON v.category_id = c.id;";

        Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                favoriteWords.add(cursorToVocabulary(cursor));
            }
        cursor.close();
        return favoriteWords;
    }

    public List<Vocabulary> searchFavoriteWords(String keyword) {
        List<Vocabulary> favoriteWords = new ArrayList<>();

        String query = "SELECT v.id, v.word, v.meaning, v.category_id, c.name AS category_name " +
                "FROM vocabulary v " +
                "INNER JOIN favorite_words f ON v.id = f.word_id " +
                "LEFT JOIN categories c ON v.category_id = c.id " +
                "WHERE v.word LIKE ?";

        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%"});

            while (cursor.moveToNext()) {
                favoriteWords.add(cursorToVocabulary(cursor));
            }

        cursor.close();
        return favoriteWords;
    }



    public void deleteVocabulary(int id) {
        db.delete("vocabulary", "id=?", new String[]{String.valueOf(id)});
    }

    private Vocabulary cursorToVocabulary(Cursor cursor) {
        int id = cursor.getInt(0);
        String word = cursor.getString(1);
        String meaning = cursor.getString(2);
        int categoryId = cursor.getInt(3);
        String categoryName = cursor.getString(4);
        return new Vocabulary(id, word, meaning, categoryId, categoryName);
    }

    public void deleteWord(int wordId) {
        db.delete("favorite_words", "word_id = ?", new String[]{String.valueOf(wordId)}); // Xóa khỏi danh sách yêu thích
        db.delete("vocabulary", "id = ?", new String[]{String.valueOf(wordId)}); // Xóa khỏi bảng từ vựng
    }

    public List<Category> getAllCategory(){
        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM categories";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            categoryList.add(new Category(id, name));
        }

        cursor.close();
        return categoryList;
    }

    public String addNewCategory(String topicName) {
        Cursor cursor = db.rawQuery("SELECT * FROM categories WHERE name = ?", new String[]{topicName});
        if (cursor.getCount() > 0) {
            cursor.close();
            return "Chủ đề đã tồn tại!";
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("name", topicName);
        long result = db.insert("categories", null, values);

        if (result == -1) {
            return "Lỗi khi thêm chủ đề!";
        } else {
            return "Thêm chủ đề thành công: ";
        }
    }

    public boolean deleteCategory(String categoryName) {
        int rowsAffected = db.delete("categories", "name = ?", new String[]{categoryName});
        return rowsAffected > 0;
    }

    public boolean hasWordsInCategory(String categoryName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM vocabulary v JOIN categories c ON v.category_id = c.id WHERE c.name = ?", new String[]{categoryName});
        boolean hasWords = false;
        if (cursor.moveToFirst()) {
            hasWords = cursor.getInt(0) > 0;
        }

        cursor.close();
        return hasWords;
    }
}
