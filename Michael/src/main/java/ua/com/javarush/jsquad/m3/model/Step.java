package ua.com.javarush.jsquad.m3.model;

import java.util.List;

public record Step(String id, String text, List<Option> options, Outcome outcome) {

    public static Step question(String id, String text, Option... options) {
        return new Step(id, text, List.of(options), Outcome.NONE);
    }

    public static Step win(String id, String text) {
        return new Step(id, text, List.of(), Outcome.WIN);
    }

    public static Step lose(String id, String text) {
        return new Step(id, text, List.of(), Outcome.LOSE);
    }

    public boolean isFinal() {
        return outcome != Outcome.NONE;
    }
}