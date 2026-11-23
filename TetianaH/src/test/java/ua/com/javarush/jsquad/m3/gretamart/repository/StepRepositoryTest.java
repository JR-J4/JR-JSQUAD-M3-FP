package ua.com.javarush.jsquad.m3.gretamart.repository;

import org.junit.jupiter.api.Test;
import ua.com.javarush.jsquad.m3.gretamart.model.Option;
import ua.com.javarush.jsquad.m3.gretamart.model.Outcome;
import ua.com.javarush.jsquad.m3.gretamart.model.Step;

import static org.junit.jupiter.api.Assertions.*;

class StepRepositoryTest {

    private final StepRepository repository = StepRepository.getInstance();

    @Test
    void startStepExistsAndIsAQuestion() {
        Step start = repository.getStart();
        assertEquals(StepRepository.START_ID, start.id());
        assertFalse(start.isFinal());
        assertEquals(2, start.options().size());
    }

    @Test
    void everyOptionLeadsToAnExistingStep() {
        for (Step step : repository.getAll().values()) {
            for (Option option : step.options()) {
                assertTrue(repository.findById(option.nextStepId()).isPresent(),
                        "Broken link " + step.id() + " -> " + option.nextStepId());
            }
        }
    }

    @Test
    void finalStepsHaveNoOptionsAndQuestionsHaveSome() {
        for (Step step : repository.getAll().values()) {
            if (step.isFinal()) {
                assertTrue(step.options().isEmpty(), step.id());
            } else {
                assertFalse(step.options().isEmpty(), step.id());
            }
        }
    }

    @Test
    void questHasAtLeastOneWinAndOneLoss() {
        assertTrue(repository.getAll().values().stream().anyMatch(s -> s.outcome() == Outcome.WIN));
        assertTrue(repository.getAll().values().stream().anyMatch(s -> s.outcome() == Outcome.LOSE));
    }

    @Test
    void unknownIdIsEmpty() {
        assertTrue(repository.findById("nope").isEmpty());
    }
}
