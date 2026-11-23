package ua.com.javarush.jsquad.m3.gretamart.model;

/** One answer the player can choose and the step it leads to. */
public record Option(String text, String nextStepId) {
}
