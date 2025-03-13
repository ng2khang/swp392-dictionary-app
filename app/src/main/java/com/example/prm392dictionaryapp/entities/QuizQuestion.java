package com.example.prm392dictionaryapp.entities;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class QuizQuestion {
    private  int id;
    private String question;
    private Date addedAt;
    private int quizSetId;
}
