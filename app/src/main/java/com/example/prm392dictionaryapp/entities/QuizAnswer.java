package com.example.prm392dictionaryapp.entities;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class QuizAnswer {
    private int id;
    private String answer;
    private int questionId;
    private int isCorrected;
}
