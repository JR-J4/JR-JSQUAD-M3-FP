package ua.com.javarush.jsquad.m3.gretamart.model;

/** Result of a step: the game goes on, or it ends with a win / a loss. */
public enum Outcome {
    CONTINUE,
    WIN,
    LOSE;

    public boolean isFinal() {
        return this != CONTINUE;
    }
}
