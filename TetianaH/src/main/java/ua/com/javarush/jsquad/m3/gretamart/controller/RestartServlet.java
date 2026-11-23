package ua.com.javarush.jsquad.m3.gretamart.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.com.javarush.jsquad.m3.gretamart.service.GameState;
import ua.com.javarush.jsquad.m3.gretamart.service.QuestService;

import java.io.IOException;

/** Starts the quest again after a win or a loss; the name and statistics stay in the session. */
@WebServlet(name = "RestartServlet", value = "/restart")
public class RestartServlet extends HttpServlet {

    private final QuestService questService = new QuestService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        GameState state = session == null ? null : (GameState) session.getAttribute(SessionKeys.GAME);
        if (state == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        questService.restart(state);
        resp.sendRedirect(req.getContextPath() + "/game");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
