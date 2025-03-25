package com.example.prm392dictionaryapp.entities;

public class Flashcard {
    private int id;
    private int set_id;
    private String term;
    private String definition;

    public Flashcard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public Flashcard() {

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getFlashcardSetId() { return set_id; }

    public void setFlashcardSetId(int flashcardSetId) { this.set_id = flashcardSetId; }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
