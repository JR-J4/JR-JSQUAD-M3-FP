package ua.com.javarush.jsquad.m3.root.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class Question {
    private int id;

    private String text;

    private ArrayList<String> options;

    private int correctAnswer;
}
