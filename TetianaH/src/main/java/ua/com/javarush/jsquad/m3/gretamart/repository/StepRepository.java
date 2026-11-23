package ua.com.javarush.jsquad.m3.gretamart.repository;

import ua.com.javarush.jsquad.m3.gretamart.model.Option;
import ua.com.javarush.jsquad.m3.gretamart.model.Outcome;
import ua.com.javarush.jsquad.m3.gretamart.model.Step;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Holds the whole quest graph in memory. */
public class StepRepository {

    public static final String START_ID = "start";

    private static final StepRepository INSTANCE = new StepRepository();

    private final Map<String, Step> steps = new LinkedHashMap<>();

    private StepRepository() {
        add(Step.question(START_ID,
                "Ви втрачаєте пам'ять. Прийняти виклик НЛО?",
                new Option("Прийняти виклик", "bridge"),
                new Option("Відхилити виклик", "lose-declined")));

        add(Step.question("bridge",
                "Ви прийняли виклик. Піднятися на капітанський місток?",
                new Option("Піднятися на місток", "who-are-you"),
                new Option("Відмовитися підніматися на місток", "lose-no-talks")));

        add(Step.question("who-are-you",
                "Ви піднялися на місток. Хто ви?",
                new Option("Розповісти правду про себе", "win-home"),
                new Option("Збрехати про себе", "lose-lie")));

        add(Step.ending("lose-declined", "Ви відхилили виклик.", Outcome.LOSE));
        add(Step.ending("lose-no-talks", "Ви не пішли на переговори.", Outcome.LOSE));
        add(Step.ending("lose-lie", "Ваша брехня була викрита.", Outcome.LOSE));
        add(Step.ending("win-home", "Вас повернули додому.", Outcome.WIN));
    }

    public static StepRepository getInstance() {
        return INSTANCE;
    }

    private void add(Step step) {
        steps.put(step.id(), step);
    }

    public Optional<Step> findById(String id) {
        return Optional.ofNullable(steps.get(id));
    }

    public Step getStart() {
        return steps.get(START_ID);
    }

    public Map<String, Step> getAll() {
        return Map.copyOf(steps);
    }
}
