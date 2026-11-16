package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "StartServlet", urlPatterns = "/start")
public class StartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("playerName");
        if (name == null || name.isBlank()) {
            name = "Мандрівник";
        }

        HttpSession session = req.getSession();
        session.setAttribute(SessionKeys.PLAYER_NAME, name.trim());
        SessionKeys.startNewGame(session);

        resp.sendRedirect(req.getContextPath() + "/game");
    }
}