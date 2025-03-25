package com.example.prm392dictionaryapp.entities;

public class Vocabulary {
    private int id;
    private String word;
    private String meaning;
    private String pronunciation;
    private String example;
    private int categoryId;
    private String createdAt;
    private String categoryName;

    public Vocabulary(int id, String word, String meaning, String pronunciation, String example, int categoryId, String createdAt) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.example = example;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }
    public Vocabulary(int id, String word, String meaning, int categoryId, String categoryName) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Vocabulary(int id, String word, String meaning, String pronunciation, String example, int categoryId, String createdAt, String categoryName) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.example = example;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Vocabulary(String word, String meaning, String pronunciation, String example, int categoryId) {
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.example = example;
        this.categoryId = categoryId;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
