package ua.com.javarush.jsquad.m3.gretamart.service;

import ua.com.javarush.jsquad.m3.gretamart.model.Option;
import ua.com.javarush.jsquad.m3.gretamart.model.Step;
import ua.com.javarush.jsquad.m3.gretamart.repository.StepRepository;

import java.util.List;

/** The game rules: where the player is, where an answer leads, how stats change. */
public class QuestService {

    public static final String DEFAULT_PLAYER_NAME = "Незнайомець";
    public static final int MAX_NAME_LENGTH = 30;

    private final StepRepository repository;

    public QuestService() {
        this(StepRepository.getInstance());
    }

    public QuestService(StepRepository repository) {
        this.repository = repository;
    }

    public GameState newGame(String rawName) {
        return new GameState(normalizeName(rawName));
    }

    public Step currentStep(GameState state) {
        return repository.findById(state.getCurrentStepId())
                .orElseThrow(() -> new IllegalStateException("Unknown step: " + state.getCurrentStepId()));
    }

    /**
     * Applies the chosen answer to the current step.
     *
     * @return the step the player has moved to
     * @throws IllegalArgumentException if the answer index is invalid
     * @throws IllegalStateException    if the game is already finished
     */
    public Step answer(GameState state, int optionIndex) {
        Step current = currentStep(state);
        if (current.isFinal()) {
            throw new IllegalStateException("The game is over, restart it first");
        }
        List<Option> options = current.options();
        if (optionIndex < 0 || optionIndex >= options.size()) {
            throw new IllegalArgumentException("No such answer: " + optionIndex);
        }

        Step next = repository.findById(options.get(optionIndex).nextStepId())
                .orElseThrow(() -> new IllegalStateException("Broken quest link"));
        state.setCurrentStepId(next.id());
        if (next.isFinal()) {
            state.registerFinishedGame(next.isWin());
        }
        return next;
    }

    /** Starts over, keeping the player name and statistics. */
    public void restart(GameState state) {
        state.setCurrentStepId(StepRepository.START_ID);
    }

    String normalizeName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return DEFAULT_PLAYER_NAME;
        }
        String name = rawName.strip();
        return name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }
}
