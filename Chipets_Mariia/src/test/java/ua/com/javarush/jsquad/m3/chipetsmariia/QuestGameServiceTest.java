package ua.com.javarush.jsquad.m3.chipetsmariia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestGameServiceTest {
    private final QuestGameService service = new QuestGameService();

    @Test
    void newGameStartsAtFirstQuestionWithThreeLives() {
        GameState game = service.start("Марія");

        assertEquals("Марія", game.getCandidateName());
        assertEquals(1, game.getQuestionNumber());
        assertEquals(3, game.getLives());
        assertFalse(game.isFinished());
    }

    @Test
    void successfulAnswersAdvanceWithoutLosingLives() {
        GameState game = answerThrough(service.start("Марія"), 1, 1);

        assertEquals(2, game.getQuestionNumber());
        assertEquals(3, game.getLives());
        assertFalse(game.isFinished());
    }

    @Test
    void wrongAnswersReduceLivesAndReachingZeroImmediatelyLoses() {
        GameState game = service.start("Марія");
        game = service.answer(game, 1, 2);
        assertEquals(2, game.getLives());
        assertEquals(2, game.getQuestionNumber());

        game = service.answer(game, 2, 2);
        assertEquals(1, game.getLives());
        assertEquals(3, game.getQuestionNumber());

        game = service.answer(game, 3, 2);
        assertEquals(0, game.getLives());
        assertEquals(Outcome.LOSS, game.getOutcome());
        assertEquals(3, game.getQuestionNumber());
    }

    @Test
    void fatalAnswerImmediatelyEndsTheGameInDefeat() {
        GameState game = service.answer(service.start("Марія"), 1, 3);

        assertEquals(Outcome.LOSS, game.getOutcome());
        assertEquals(0, game.getLives());
        assertTrue(game.isFinished());
    }

    @Test
    void wrongAnswerThatWouldExhaustLastLifeDoesNotAdvance() {
        GameState game = service.start("Марія");
        game = service.answer(game, 1, 2);
        game = service.answer(game, 2, 2);
        game = service.answer(game, 3, 1);
        assertEquals(1, game.getLives());
        game = service.answer(game, 4, 2);

        assertEquals(Outcome.LOSS, game.getOutcome());
        assertEquals(0, game.getLives());
        assertEquals(4, game.getQuestionNumber());
    }

    @Test
    void finalQuestionProducesOfferDevopsOrLoss() {
        GameState beforeFinal = reachFinalQuestion("Марія");

        assertEquals(Outcome.OFFER, service.answer(beforeFinal, 5, 1).getOutcome());
        assertEquals(Outcome.DEVOPS, service.answer(beforeFinal, 5, 2).getOutcome());
        GameState loss = service.answer(beforeFinal, 5, 3);
        assertEquals(Outcome.LOSS, loss.getOutcome());
        assertEquals(3, loss.getLives(), "Третя відповідь фінального питання не зобов’язує списувати життя");
    }

    @Test
    void restartStartsFreshStateAndAttemptRegistryKeepsIndependentCounts() {
        AttemptRegistry attempts = new AttemptRegistry();
        assertEquals(1, attempts.beginAttempt("Марія"));
        assertEquals(1, attempts.beginAttempt("Андрій"));
        assertEquals(2, attempts.beginAttempt("Марія"));

        GameState oldGame = service.answer(service.start("Марія"), 1, 2);
        assertEquals(2, oldGame.getLives());
        GameState restarted = service.start(oldGame.getCandidateName());

        assertEquals(3, restarted.getLives());
        assertEquals(1, restarted.getQuestionNumber());
        assertEquals(2, attempts.countFor("Марія"));
        assertEquals(1, attempts.countFor("Андрій"));
    }

    @Test
    void rejectsInvalidOrStaleAnswers() {
        GameState game = service.start("Марія");

        assertThrows(IllegalArgumentException.class, () -> service.answer(game, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> service.answer(game, 1, 4));
        assertThrows(IllegalArgumentException.class, () -> service.start("   "));
    }

    private GameState reachFinalQuestion(String name) {
        GameState game = service.start(name);
        for (int question = 1; question < 5; question++) {
            game = service.answer(game, question, 1);
        }
        assertEquals(5, game.getQuestionNumber());
        return game;
    }

    private GameState answerThrough(GameState game, int question, int answer) {
        return service.answer(game, question, answer);
    }
}
