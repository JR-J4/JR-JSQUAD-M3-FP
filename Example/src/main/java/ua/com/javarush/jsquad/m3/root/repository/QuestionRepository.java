package ua.com.javarush.jsquad.m3.root.repository;

import ua.com.javarush.jsquad.m3.root.model.Question;

import java.util.ArrayList;

public class QuestionRepository {
    private static QuestionRepository INSTANCE;

    private final ArrayList<Question> questions = new ArrayList<>();


    private QuestionRepository() {
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("Programming language");

        questions.add(Question.builder()
                .text("What is java")
                .options(options)
                .correctAnswer(3)
                .build());
    }

    public static QuestionRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionRepository();
        }

        return INSTANCE;

    }


    public ArrayList<Question> getAll() {
        return questions;
    }


}
