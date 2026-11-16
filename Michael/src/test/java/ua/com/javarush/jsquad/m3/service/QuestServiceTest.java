package ua.com.javarush.jsquad.m3.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.javarush.jsquad.m3.model.Option;
import ua.com.javarush.jsquad.m3.model.Outcome;
import ua.com.javarush.jsquad.m3.model.Step;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestServiceTest {

    private QuestService quest;

    @BeforeEach
    void setUp() {
        quest = QuestService.createDefault();
    }

    @Test
    void startStepIsAQuestionWithTwoOptions() {
        Step start = quest.getStep(QuestService.START_STEP_ID);

        assertFalse(start.isFinal());
        assertEquals(Outcome.NONE, start.outcome());
        assertEquals(2, start.options().size());
    }

    @Test
    void restartingServerLeadsToSecondAttempt() {
        assertEquals("restarted", quest.choose("start", 0).id());
    }

    @Test
    void openingLogsLeadsToLogsStep() {
        assertEquals("logs", quest.choose("start", 1).id());
    }

    @Test
    void restartingTwiceIsALoss() {
        assertEquals(Outcome.LOSE, quest.choose("restarted", 0).outcome());
    }

    @Test
    void afterFailedRestartYouCanStillOpenLogs() {
        assertEquals("logs", quest.choose("restarted", 1).id());
    }

    @Test
    void rollbackAndHonestyWins() {
        Step step = quest.choose("start", 1);          // logs
        step = quest.choose(step.id(), 0);             // rollback
        step = quest.choose(step.id(), 0);             // honest

        assertEquals(Outcome.WIN, step.outcome());
        assertTrue(step.isFinal());
    }

    @Test
    void blamingOthersIsALoss() {
        assertEquals(Outcome.LOSE, quest.choose("rollback", 1).outcome());
    }

    @Test
    void hotfixTestedFirstWins() {
        Step step = quest.choose("logs", 1);           // hotfix
        step = quest.choose(step.id(), 1);             // tested first

        assertEquals(Outcome.WIN, step.outcome());
    }

    @Test
    void deployingHotfixStraightToProductionIsALoss() {
        assertEquals(Outcome.LOSE, quest.choose("hotfix", 0).outcome());
    }

    @Test
    void unknownStepThrows() {
        assertThrows(IllegalArgumentException.class, () -> quest.getStep("nope"));
        assertThrows(IllegalArgumentException.class, () -> quest.choose("nope", 0));
    }

    @Test
    void optionIndexOutOfRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> quest.choose("start", -1));
        assertThrows(IllegalArgumentException.class, () -> quest.choose("start", 2));
    }

    @Test
    void cannotChooseOnAFinalStep() {
        assertThrows(IllegalArgumentException.class, () -> quest.choose("win_honest", 0));
    }

    @Test
    void questGraphIsConsistent() {
        Set<String> visited = new HashSet<>();
        Set<Outcome> outcomes = new HashSet<>();
        Deque<Step> queue = new ArrayDeque<>();
        queue.add(quest.getStep(QuestService.START_STEP_ID));

        while (!queue.isEmpty()) {
            Step step = queue.poll();
            if (!visited.add(step.id())) {
                continue;
            }
            outcomes.add(step.outcome());
            for (Option option : step.options()) {
                queue.add(quest.getStep(option.nextStepId()));
            }
        }

        assertEquals(10, visited.size());
        assertTrue(outcomes.contains(Outcome.WIN));
        assertTrue(outcomes.contains(Outcome.LOSE));
    }
}