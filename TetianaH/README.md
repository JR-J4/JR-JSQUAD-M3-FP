# UFO Quest (TetianaH)

Text quest on Servlets + JSP + JSTL. URL after deploy: `/tetianaH/`.

- `/` — welcome page (back story + name form)
- `/game` — current question / result, statistics block
- `/restart` — play again after a win or a loss (name and stats stay in the session)

Session (`HttpSession`, attribute `game`): player name, games played, wins, losses, current step.
Quest graph lives in `StepRepository`; rules in `QuestService`. Tests: `mvn -pl TetianaH test`.
