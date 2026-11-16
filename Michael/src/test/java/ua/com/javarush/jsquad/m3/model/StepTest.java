package ua.com.javarush.jsquad.m3.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepTest {

    @Test
    void questionStepIsNotFinal() {
        Step step = Step.question("s", "Text", new Option("Yes", "a"));

        assertFalse(step.isFinal());
        assertEquals(Outcome.NONE, step.outcome());
        assertEquals(1, step.options().size());
    }

    @Test
    void winStepIsFinalAndHasNoOptions() {
        Step step = Step.win("w", "Won");

        assertTrue(step.isFinal());
        assertEquals(Outcome.WIN, step.outcome());
        assertTrue(step.options().isEmpty());
    }

    @Test
    void loseStepIsFinalAndHasNoOptions() {
        Step step = Step.lose("l", "Lost");

        assertTrue(step.isFinal());
        assertEquals(Outcome.LOSE, step.outcome());
        assertTrue(step.options().isEmpty());
    }
}
