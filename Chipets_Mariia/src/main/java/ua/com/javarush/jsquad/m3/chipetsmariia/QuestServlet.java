package ua.com.javarush.jsquad.m3.chipetsmariia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/quest")
public class QuestServlet extends HttpServlet {
    private static final String GAME_STATE = "quest.gameState";
    private static final String ATTEMPTS = "quest.attemptsByCandidate";
    private final QuestGameService gameService = new QuestGameService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String view = request.getParameter("view");
        if (view == null || view.isBlank() || "welcome".equals(view)) {
            showWelcome(request, response);
            return;
        }
        if ("question".equals(view)) {
            showQuestion(request, response);
            return;
        }
        if ("final".equals(view)) {
            showFinal(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("start".equals(action)) {
            startGame(request, response);
        } else if ("answer".equals(action)) {
            submitAnswer(request, response);
        } else if ("restart".equals(action)) {
            restartGame(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Невідома дія");
        }
    }

    private void showWelcome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String candidateName = normalizeName(request.getParameter("candidateName"));
        request.setAttribute("candidateName", candidateName);
        request.setAttribute("attemptCount", candidateName.isEmpty()
                ? 0 : attempts(request.getSession()).countFor(candidateName));
        request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
    }

    private void startGame(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String candidateName = normalizeName(request.getParameter("candidateName"));
        if (candidateName.isEmpty() || candidateName.length() > 80) {
            request.setAttribute("errorMessage", candidateName.isEmpty()
                    ? "Введи, будь ласка, ім’я кандидата."
                    : "Ім’я кандидата має містити не більше ніж 80 символів.");
            request.setAttribute("candidateName", candidateName);
            request.setAttribute("attemptCount", candidateName.isEmpty()
                    ? 0 : attempts(request.getSession()).countFor(candidateName));
            request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        incrementAttempt(session, candidateName);
        session.setAttribute(GAME_STATE, gameService.start(candidateName));
        response.sendRedirect(request.getContextPath() + "/quest?view=question");
    }

    private void showQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        GameState game = session == null ? null : (GameState) session.getAttribute(GAME_STATE);
        if (game == null) {
            response.sendRedirect(request.getContextPath() + "/quest?view=welcome");
            return;
        }
        if (game.isFinished()) {
            response.sendRedirect(request.getContextPath() + "/quest?view=final");
            return;
        }
        GameQuestion question = gameService.getQuestion(game.getQuestionNumber());
        request.setAttribute("game", game);
        request.setAttribute("question", question);
        request.setAttribute("questionPrompt", question.getPrompt().formatted(game.getCandidateName()));
        request.getRequestDispatcher("/WEB-INF/views/question" + question.getNumber() + ".jsp")
                .forward(request, response);
    }

    private void showFinal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        GameState game = session == null ? null : (GameState) session.getAttribute(GAME_STATE);
        if (game == null) {
            response.sendRedirect(request.getContextPath() + "/quest?view=welcome");
            return;
        }
        if (!game.isFinished()) {
            response.sendRedirect(request.getContextPath() + "/quest?view=question");
            return;
        }
        request.setAttribute("game", game);
        request.setAttribute("outcomeName", game.getOutcome().name());
        request.getRequestDispatcher("/WEB-INF/views/final.jsp").forward(request, response);
    }

    private void submitAnswer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        GameState game = session == null ? null : (GameState) session.getAttribute(GAME_STATE);
        if (game == null || game.isFinished()) {
            response.sendRedirect(request.getContextPath() + "/quest?view=welcome");
            return;
        }

        int submittedQuestion;
        int answerNumber;
        try {
            submittedQuestion = Integer.parseInt(request.getParameter("questionNumber"));
            answerNumber = Integer.parseInt(request.getParameter("answerNumber"));
        } catch (NumberFormatException | NullPointerException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некоректна відповідь");
            return;
        }
        if (submittedQuestion != game.getQuestionNumber() || answerNumber < 1 || answerNumber > 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Відповідь не відповідає поточному питанню");
            return;
        }

        session.setAttribute(GAME_STATE, gameService.answer(game, submittedQuestion, answerNumber));
        GameState updatedGame = (GameState) session.getAttribute(GAME_STATE);
        String destination = updatedGame.isFinished() ? "final" : "question";
        response.sendRedirect(request.getContextPath() + "/quest?view=" + destination);
    }

    private void restartGame(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        GameState game = session == null ? null : (GameState) session.getAttribute(GAME_STATE);
        if (session == null || game == null || !game.isFinished()) {
            response.sendRedirect(request.getContextPath() + "/quest?view=welcome");
            return;
        }
        incrementAttempt(session, game.getCandidateName());
        session.setAttribute(GAME_STATE, gameService.start(game.getCandidateName()));
        response.sendRedirect(request.getContextPath() + "/quest?view=question");
    }

    private AttemptRegistry attempts(HttpSession session) {
        Object current = session.getAttribute(ATTEMPTS);
        if (current instanceof AttemptRegistry registry) {
            return registry;
        }
        AttemptRegistry registry = new AttemptRegistry();
        session.setAttribute(ATTEMPTS, registry);
        return registry;
    }

    private void incrementAttempt(HttpSession session, String candidateName) {
        attempts(session).beginAttempt(candidateName);
    }

    private String normalizeName(String name) {
        return name == null ? "" : name.trim();
    }
}
