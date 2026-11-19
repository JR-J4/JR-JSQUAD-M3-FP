package ua.com.javarush.jsquad.m3.chipetsmariia;

import java.util.List;

public final class GameQuestion {
    private final int number;
    private final String title;
    private final String prompt;
    private final List<String> answers;

    public GameQuestion(int number, String title, String prompt, List<String> answers) {
        this.number = number;
        this.title = title;
        this.prompt = prompt;
        this.answers = List.copyOf(answers);
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
