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

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getFlashcardCount() { return flashcardCount; }
}
