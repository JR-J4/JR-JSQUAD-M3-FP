package ua.com.javarush.jsquad.m3.gretamart.model;

import java.util.List;

/** A node of the quest: a text, the answers to choose from and an outcome. */
public record Step(String id, String text, List<Option> options, Outcome outcome) {

    public Step {
        options = List.copyOf(options);
    }

    public static Step question(String id, String text, Option... options) {
        return new Step(id, text, List.of(options), Outcome.CONTINUE);
    }

    public static Step ending(String id, String text, Outcome outcome) {
        return new Step(id, text, List.of(), outcome);
    }

    // JavaBean-style getters so that JSP/EL can use ${step.text}, ${step.final}, ...
    public String getId() { return id; }
    public String getText() { return text; }
    public List<Option> getOptions() { return options; }
    public Outcome getOutcome() { return outcome; }
    public boolean isFinal() { return outcome.isFinal(); }
    public boolean isFinished() { return isFinal(); }
    public boolean isWin() { return outcome == Outcome.WIN; }
}
