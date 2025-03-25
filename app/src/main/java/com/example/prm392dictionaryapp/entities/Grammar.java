package com.example.prm392dictionaryapp.entities;

public class Grammar {
    private int id;
    private String title;
    private String description;
    private String example;
    private int status;

    public Grammar(int id, String title, String description, String example, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.example = example;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public int getStatus() {
        return status;
    }
}
