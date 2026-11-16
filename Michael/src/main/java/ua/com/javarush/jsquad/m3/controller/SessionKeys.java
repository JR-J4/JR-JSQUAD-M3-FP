package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.http.HttpSession;
import ua.com.javarush.jsquad.m3.service.QuestService;

final class SessionKeys {

    static final String PLAYER_NAME = "playerName";
    static final String CURRENT_STEP_ID = "currentStepId";
    static final String GAMES_COUNT = "gamesCount";
    static final String GAME_FINISHED = "gameFinished";

    private SessionKeys() {
    }

    static void startNewGame(HttpSession session) {
        session.setAttribute(CURRENT_STEP_ID, QuestService.START_STEP_ID);
        Object value = session.getAttribute(GAMES_COUNT);
        int games = value instanceof Integer count ? count : 0;
        session.setAttribute(GAMES_COUNT, games + 1);
    }

    static boolean hasGame(HttpSession session) {
        return session != null
                && session.getAttribute(PLAYER_NAME) != null
                && session.getAttribute(CURRENT_STEP_ID) != null;
    }
}