package ua.com.javarush.jsquad.m3.gretamart.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.com.javarush.jsquad.m3.gretamart.service.GameState;
import ua.com.javarush.jsquad.m3.gretamart.service.QuestService;

import java.io.IOException;

/** Creates a new player in the session and sends them to the first question. */
@WebServlet(name = "StartServlet", value = "/start")
public class StartServlet extends HttpServlet {

    private final QuestService questService = new QuestService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        GameState state = questService.newGame(req.getParameter("name"));
        req.getSession().setAttribute(SessionKeys.GAME, state);
        resp.sendRedirect(req.getContextPath() + "/game");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
