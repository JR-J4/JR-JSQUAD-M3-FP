package ua.com.javarush.jsquad.m3.gretamart.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.com.javarush.jsquad.m3.gretamart.service.GameState;
import ua.com.javarush.jsquad.m3.gretamart.service.QuestService;

import java.io.IOException;

/** Shows the current step (GET) and applies the chosen answer (POST). */
@WebServlet(name = "GameServlet", value = "/game")
public class GameServlet extends HttpServlet {

    private final QuestService questService = new QuestService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GameState state = currentGame(req);
        if (state == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("step", questService.currentStep(state));
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GameState state = currentGame(req);
        if (state == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        try {
            questService.answer(state, Integer.parseInt(req.getParameter("answer")));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // missing/invalid answer or a double submit after the end: just show the current step again
        }
        resp.sendRedirect(req.getContextPath() + "/game");
    }

    private GameState currentGame(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (GameState) session.getAttribute(SessionKeys.GAME);
    }
}
