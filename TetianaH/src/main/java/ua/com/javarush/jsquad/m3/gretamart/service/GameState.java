package ua.com.javarush.jsquad.m3.gretamart.service;

import ua.com.javarush.jsquad.m3.gretamart.repository.StepRepository;

import java.io.Serializable;

/** Everything we keep in the HTTP session for one player. */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String playerName;
    private String currentStepId = StepRepository.START_ID;
    private int gamesPlayed;
    private int wins;
    private int losses;

    public GameState(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() { return playerName; }
    public String getCurrentStepId() { return currentStepId; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    void setCurrentStepId(String currentStepId) {
        this.currentStepId = currentStepId;
    }

    void registerFinishedGame(boolean won) {
        gamesPlayed++;
        if (won) {
            wins++;
        } else {
            losses++;
        }
    }
}
