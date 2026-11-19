package ua.com.javarush.jsquad.m3.chipetsmariia;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String candidateName;
    private final int questionNumber;
    private final int lives;
    private final Outcome outcome;

    public GameState(String candidateName, int questionNumber, int lives, Outcome outcome) {
        this.candidateName = Objects.requireNonNull(candidateName, "candidateName");
        this.questionNumber = questionNumber;
        this.lives = lives;
        this.outcome = outcome;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getLives() {
        return lives;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public boolean isFinished() {
        return outcome != null;
    }
}
