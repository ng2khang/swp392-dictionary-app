package com.example.prm392dictionaryapp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Vocabulary {
    private int id;
    private String word;
    private String meaning;
    private String example;
    private int pronunciation;
    private int category_id;
}
