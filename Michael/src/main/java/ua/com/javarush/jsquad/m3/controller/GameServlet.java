package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.com.javarush.jsquad.m3.model.Step;
import ua.com.javarush.jsquad.m3.service.QuestService;

import java.io.IOException;

@WebServlet(name = "GameServlet", urlPatterns = "/game")
public class GameServlet extends HttpServlet {

    private final QuestService questService = QuestService.createDefault();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!SessionKeys.hasGame(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String stepId = (String) session.getAttribute(SessionKeys.CURRENT_STEP_ID);
        Step step = questService.getStep(stepId);

        req.setAttribute("step", step);
        req.getRequestDispatcher("/WEB-INF/views/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (!SessionKeys.hasGame(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String stepId = (String) session.getAttribute(SessionKeys.CURRENT_STEP_ID);
        try {
            int answer = Integer.parseInt(req.getParameter("answer"));
            Step next = questService.choose(stepId, answer);
            session.setAttribute(SessionKeys.CURRENT_STEP_ID, next.id());
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid answer");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/game");
    }
}