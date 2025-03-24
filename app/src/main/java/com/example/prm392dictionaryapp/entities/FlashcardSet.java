package com.example.prm392dictionaryapp.entities;

public class FlashcardSet {
    int id;
    String title;
    int flashcardCount;

    public FlashcardSet(int id, String title, int flashcardCount) {
        this.id = id;
        this.title = title;
        this.flashcardCount = flashcardCount;
    }

    public FlashcardSet() {

    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getFlashcardCount() { return flashcardCount; }
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setFlashcardCount(int flashcardCount) { this.flashcardCount = flashcardCount; }
}
