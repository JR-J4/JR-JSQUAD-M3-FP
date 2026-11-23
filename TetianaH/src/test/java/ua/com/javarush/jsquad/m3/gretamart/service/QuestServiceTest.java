package ua.com.javarush.jsquad.m3.gretamart.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.javarush.jsquad.m3.gretamart.model.Step;
import ua.com.javarush.jsquad.m3.gretamart.repository.StepRepository;

import static org.junit.jupiter.api.Assertions.*;

class QuestServiceTest {

    private QuestService service;
    private GameState state;

    @BeforeEach
    void setUp() {
        service = new QuestService();
        state = service.newGame("Грета");
    }

    @Test
    void newGameStartsAtFirstStepWithZeroStats() {
        assertEquals(StepRepository.START_ID, state.getCurrentStepId());
        assertEquals("Грета", state.getPlayerName());
        assertEquals(0, state.getGamesPlayed());
    }

    @Test
    void blankOrMissingNameFallsBackToDefault() {
        assertEquals(QuestService.DEFAULT_PLAYER_NAME, service.newGame(null).getPlayerName());
        assertEquals(QuestService.DEFAULT_PLAYER_NAME, service.newGame("   ").getPlayerName());
    }

    @Test
    void longNameIsTrimmedAndCut() {
        assertEquals("Ann", service.normalizeName("  Ann  "));
        assertEquals(QuestService.MAX_NAME_LENGTH, service.normalizeName("x".repeat(100)).length());
    }

    @Test
    void winningPath() {
        service.answer(state, 0);                    // accept the challenge
        service.answer(state, 0);                    // go to the bridge
        Step end = service.answer(state, 0);         // tell the truth

        assertTrue(end.isFinal());
        assertTrue(end.isWin());
        assertEquals(1, state.getGamesPlayed());
        assertEquals(1, state.getWins());
        assertEquals(0, state.getLosses());
    }

    @Test
    void declineChallengeLoses() {
        Step end = service.answer(state, 1);
        assertTrue(end.isFinal());
        assertFalse(end.isWin());
        assertEquals(1, state.getLosses());
    }

    @Test
    void refuseToGoToBridgeLoses() {
        service.answer(state, 0);
        Step end = service.answer(state, 1);
        assertFalse(end.isWin());
        assertEquals(1, state.getGamesPlayed());
    }

    @Test
    void lyingLoses() {
        service.answer(state, 0);
        service.answer(state, 0);
        Step end = service.answer(state, 1);
        assertFalse(end.isWin());
        assertEquals(1, state.getLosses());
    }

    @Test
    void invalidAnswerIndexIsRejectedAndStateUnchanged() {
        assertThrows(IllegalArgumentException.class, () -> service.answer(state, -1));
        assertThrows(IllegalArgumentException.class, () -> service.answer(state, 2));
        assertEquals(StepRepository.START_ID, state.getCurrentStepId());
        assertEquals(0, state.getGamesPlayed());
    }

    @Test
    void cannotAnswerAfterTheGameIsOver() {
        service.answer(state, 1);
        assertThrows(IllegalStateException.class, () -> service.answer(state, 0));
        assertEquals(1, state.getGamesPlayed());   // not counted twice
    }

    @Test
    void restartGoesBackToStartAndKeepsStatistics() {
        service.answer(state, 1);                    // lose
        service.restart(state);
        assertEquals(StepRepository.START_ID, state.getCurrentStepId());
        assertEquals(1, state.getGamesPlayed());
        assertEquals("Грета", state.getPlayerName());

        service.answer(state, 0);
        service.answer(state, 0);
        service.answer(state, 0);                    // win
        assertEquals(2, state.getGamesPlayed());
        assertEquals(1, state.getWins());
        assertEquals(1, state.getLosses());
    }
}
