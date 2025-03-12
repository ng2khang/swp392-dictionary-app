package com.example.prm392dictionaryapp.entities;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class QuizSet {
    private int id;
    private String title;
    private String description;
    private int totalQuestion;
    private int quizTime;
    private Date createdAt;
}
