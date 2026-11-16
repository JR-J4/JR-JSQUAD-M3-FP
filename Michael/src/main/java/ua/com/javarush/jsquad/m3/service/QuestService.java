package ua.com.javarush.jsquad.m3.service;

import ua.com.javarush.jsquad.m3.model.Option;
import ua.com.javarush.jsquad.m3.model.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestService {

    public static final String START_STEP_ID = "start";

    private final Map<String, Step> steps = new HashMap<>();

    public QuestService(List<Step> stepList) {
        for (Step step : stepList) {
            steps.put(step.id(), step);
        }
    }

    // Квест «Падіння продакшену»
    public static QuestService createDefault() {
        return new QuestService(List.of(
                Step.question("start",
                        "П'ятниця, 18:47. Продакшен упав: сайт не відкривається, клієнти завалили підтримку. З чого почнете?",
                        new Option("Перезапустити сервер", "restarted"),
                        new Option("Відкрити логи", "logs")),

                Step.question("restarted",
                        "Сервер перезапустився, але за хвилину впав знову. Час спливає. Що далі?",
                        new Option("Перезапустити ще раз", "lose_loop"),
                        new Option("Все ж відкрити логи", "logs")),

                Step.question("logs",
                        "У логах купа помилок: після вчорашнього релізу закінчився пул з'єднань з базою даних. Що зробите?",
                        new Option("Відкотити останній реліз", "rollback"),
                        new Option("Швидко написати хотфікс", "hotfix")),

                Step.question("rollback",
                        "Ви відкотили реліз, і сайт ожив! Тімлід питає, що сталося. Що скажете?",
                        new Option("Розповісти все як є", "win_honest"),
                        new Option("Сказати, що це не ваша провина", "lose_blame")),

                Step.question("hotfix",
                        "Хотфікс готовий. Куди його викотити?",
                        new Option("Одразу на продакшн", "lose_deploy"),
                        new Option("Спочатку на тестовий сервер", "win_tested")),

                Step.win("win_honest", "Тімлід подякував за чесність і швидку реакцію. Вам довірили складніші задачі. Перемога!"),
                Step.win("win_tested", "Хотфікс спрацював з першого разу, сайт ожив, а ви герой вечора. Перемога!"),
                Step.lose("lose_loop", "Ви перезапускали сервер до півночі, а сайт так і не ожив. Поразка."),
                Step.lose("lose_blame", "Тімлід перевірив історію комітів і побачив правду. Довіру втрачено. Поразка."),
                Step.lose("lose_deploy", "У хотфіксі була помилка, і вона остаточно поклала сервер. Поразка.")
        ));
    }

    public Step getStep(String id) {
        Step step = steps.get(id);
        if (step == null) {
            throw new IllegalArgumentException("Unknown step: " + id);
        }
        return step;
    }

    public Step choose(String currentStepId, int optionIndex) {
        Step current = getStep(currentStepId);
        if (current.isFinal() || optionIndex < 0 || optionIndex >= current.options().size()) {
            throw new IllegalArgumentException("Bad answer " + optionIndex + " on step " + currentStepId);
        }
        return getStep(current.options().get(optionIndex).nextStepId());
    }
}