package ua.com.javarush.jsquad.m3.chipetsmariia;

import java.util.List;

public final class QuestGameService {
    private static final List<GameQuestion> QUESTIONS = List.of(
            new GameQuestion(1, "Питання 1. Перевірка на адекватність.",
                    "Синьйор поправляє окуляри, дивиться у твоє резюме і з легкою усмішкою питає: «Бачу, %s, тут вказано, що ти знаєш Java Core на 9 з 10. Тоді скажи чесно: навіщо тобі ця робота, якщо можна піти в блогери?»",
                    List.of(
                            "Люблю, коли код працює передбачувано, а сувора типізація рятує від нічних дзвінків із продакшену.",
                            "Я з дитинства мріяв стати частиною вашої дружньої родини та писати Ентерпрайз.",
                            "Та я просто прочитав, що тут дають безкоштовні печеньки і платять у доларах."
                    )),
            new GameQuestion(2, "Питання 2. Пастка на уважність.",
                    "Синьйор хитро примружується: «Добре, %s, уяви класику: ти перевіряєш пароль користувача у змінній String input і порівнюєш його з еталоном \"secret\" через input == \"secret\". У тебе на комп'ютері все працює, а на сервері пускає через раз. У чому підступ?»",
                    List.of(
                            "Оператор == порівнює посилання на об'єкти в пам'яті, а не сам текст. Рядки треба порівнювати через .equals(), інакше магія String Pool зіграє злий жарт.",
                            "Мабуть, сервер просто втомився і йому треба дати трохи оперативної пам'яті.",
                            "Просто приберу перевірку пароля — менше коду, менше багів!"
                    )),
            new GameQuestion(3, "Питання 3. Пастка finally.",
                    "Синьйор хитро посміхається і відкриває блокнот: «%s, уяви, що в блоці try у тебе є return 1;, а в блоці finally написано return 2;. Що поверне такий метод і що станеться, якщо в try перед цим вилетів NullPointerException?»",
                    List.of(
                            "Метод поверне 2, бо блок finally виконується майже завжди і «перекриває» результат або навіть виняток із блоку try. Тому писати return у finally — погана практика.",
                            "Поверне 1, якщо все пройшло добре, або викине NullPointerException, якщо сталася помилка. Блок finally працює у фоні і не може змінити те, що вже повернув try.",
                            "Я не використовую finally, я просто ставлю Thread.sleep(5000) і сподіваюся, що сміттєзбирач сам усе прибере."
                    )),
            new GameQuestion(4, "Питання 4. Бойове хрещення з Git.",
                    "Синьйор нахиляється ближче до камери і переходить на шепіт: «П'ятниця, 17:55. Ти випадково зробив git push --force прямо в головну гілку main і затер коміти всієї команди за тиждень. Твої дії, %s?»",
                    List.of(
                            "Не панікую, дивлюся git reflog, відновлюю коміти локально, пушу назад і чесно пишу в робочий чат.",
                            "Видалю папку з проєктом на своєму комп'ютері і склоную заново з GitHub — може, воно само полагодиться.",
                            "Швидко звільняюся, видаляю GitHub, спалюю ноутбук і їду жити в ліс."
                    )),
            new GameQuestion(5, "Питання 5. Фінальний бос.",
                    "Останнє питання. Впала база даних на продакшені. Клієнти не можуть оплатити замовлення, менеджери кричать у чаті, а тімлід у горах без зв'язку. Що робиш, %s?",
                    List.of(
                            "Перевіряю логи, піднімаю резервний інстанс бази з бекапу і повідомляю менеджерів про орієнтовний час відновлення.",
                            "Заварюю міцну каву, без зайвих емоцій натискаю кнопку Reboot на сервері, а в робочому чаті спокійно пишу: «Це не падіння, ми просто тестуємо відмовостійкість системи під навантаженням».",
                            "Пишу в чат: «У мене локально все працює, проблема на вашому боці» і йду пити чай."
                    ))
    );

    public GameState start(String candidateName) {
        if (candidateName == null || candidateName.isBlank()) {
            throw new IllegalArgumentException("Ім'я кандидата не може бути порожнім");
        }
        return new GameState(candidateName, 1, 3, null);
    }

    public GameQuestion getQuestion(int questionNumber) {
        if (questionNumber < 1 || questionNumber > QUESTIONS.size()) {
            throw new IllegalArgumentException("Невідоме питання: " + questionNumber);
        }
        return QUESTIONS.get(questionNumber - 1);
    }

    public GameState answer(GameState state, int questionNumber, int answerNumber) {
        if (state == null || state.isFinished()) {
            throw new IllegalStateException("Немає активного проходження гри");
        }
        if (state.getQuestionNumber() != questionNumber) {
            throw new IllegalArgumentException("Надіслано відповідь не на поточне питання");
        }
        if (answerNumber < 1 || answerNumber > 3) {
            throw new IllegalArgumentException("Невідомий варіант відповіді");
        }

        if (questionNumber == 5) {
            return switch (answerNumber) {
                case 1 -> new GameState(state.getCandidateName(), 5, state.getLives(), Outcome.OFFER);
                case 2 -> new GameState(state.getCandidateName(), 5, state.getLives(), Outcome.DEVOPS);
                case 3 -> new GameState(state.getCandidateName(), 5, state.getLives(), Outcome.LOSS);
                default -> throw new IllegalStateException("Недосяжний варіант відповіді");
            };
        }

        if (answerNumber == 3) {
            return new GameState(state.getCandidateName(), questionNumber, 0, Outcome.LOSS);
        }

        int lives = state.getLives() - (answerNumber == 2 ? 1 : 0);
        if (lives <= 0) {
            return new GameState(state.getCandidateName(), questionNumber, 0, Outcome.LOSS);
        }
        return new GameState(state.getCandidateName(), questionNumber + 1, lives, null);
    }
}
