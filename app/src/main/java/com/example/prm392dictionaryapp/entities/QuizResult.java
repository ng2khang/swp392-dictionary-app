package com.example.prm392dictionaryapp.entities;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class QuizResult {
    private int id;
    private int quizSetId;
    private int score;
    private int isCompleted;
    private Date completedAt;

    private QuizSet quizSet;
}
