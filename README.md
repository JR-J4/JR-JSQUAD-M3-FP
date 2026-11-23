# UFO Quest: text adventure (Servlets + JSP + JSTL)

A small text quest built as the final project of **Module 3 (Java Professional)**.
You lose your memory and a UFO invites you to take up a challenge. Each answer decides which question comes next, and the game ends in a win or a loss. After that you can start again.

## Story and gameplay

```
Start: You lose your memory. Accept the UFO's challenge?
├─ Accept → Climb up to the captain's bridge?
│   ├─ Climb up → You are on the bridge. Who are you?
│   │   ├─ Tell the truth → You are sent home        WIN
│   │   └─ Lie            → Your lie was exposed     LOSE
│   └─ Refuse → You did not go to the talks          LOSE
└─ Decline → You declined the challenge              LOSE
```

The quest texts are in Ukrainian.

## Features

- Welcome page with the back story and a form for the player's name
- Question page: one question at a time, answers as radio buttons
- Win and loss screens with a **"play again"** button
- Statistics block: IP address, player name, games played, wins and losses
- Data kept in the `HttpSession`: player name, current step, games played, wins, losses. The name and statistics survive a restart.
- Redirect after every POST (Post/Redirect/Get), so refreshing the page does not resubmit an answer
- Input handling: empty or long names are normalized, invalid answers are ignored, and user text is escaped in the JSP (`<c:out>`)

## Tech stack

- Java 21, Maven (multi-module project, WAR packaging)
- Jakarta Servlet 6.1, JSP, JSTL 3.0
- JUnit 5 and Mockito for tests
- JaCoCo for coverage
- Tomcat 11

## Project structure

```
TetianaH/
├── pom.xml
└── src
    ├── main
    │   ├── java/.../
    │   │   ├── controller/   WelcomeServlet, StartServlet, GameServlet, RestartServlet
    │   │   ├── model/        Step, Option, Outcome
    │   │   ├── repository/   StepRepository  (the quest graph)
    │   │   └── service/      QuestService (game rules), GameState (session data)
    │   └── webapp/           welcome.jsp, game.jsp, WEB-INF/web.xml
    └── test/java/...         unit tests for repository, service and servlets
```

| URL        | Method | What it does                                   |
|------------|--------|------------------------------------------------|
| `/`        | GET    | Welcome page                                   |
| `/start`   | POST   | Creates the player in the session              |
| `/game`    | GET    | Shows the current question or the result       |
| `/game`    | POST   | Applies the chosen answer                      |
| `/restart` | POST   | Starts over, keeping the name and statistics   |

The servlets are thin. The rules (where an answer leads, how statistics change) are in `QuestService`, and the quest itself is data in `StepRepository`, so questions can be added or changed without touching the servlets.

## Tests

22 JUnit tests cover:
- the quest graph (no broken links, final steps have no options, at least one win and one loss)
- the game rules (winning and losing paths, invalid answers, no double counting after the end, restart keeps statistics)
- the servlets, with mocked request, response and session

```bash
mvn test
```

## How to run

Requires JDK 21+, Maven and Tomcat 11 (the code uses the `jakarta.servlet` API).

```bash
mvn clean package
cp TetianaH/target/tetianah.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/catalina.sh run
```

Or with Docker, from the repository root (this builds all modules):

```bash
docker build -t jsquad-m3 .
docker run --rm -p 8080:8080 jsquad-m3
```

Then open **http://localhost:8080/tetianah/**
