package com.example.prm392dictionaryapp.entities;

import java.util.Date;

public class favoriteWords {
    private int id;
    private int wordId;
    private Date addedAt;
    private String example;

    public favoriteWords(int id, int wordId, Date addedAt, String example) {
        this.id = id;
        this.wordId = wordId;
        this.addedAt = addedAt;
        this.example = example;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public favoriteWords() {
    }
}
