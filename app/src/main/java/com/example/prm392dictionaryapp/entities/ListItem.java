package com.example.prm392dictionaryapp.entities;

public abstract class ListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_VOCAB = 1;

    abstract public int getType();
}

