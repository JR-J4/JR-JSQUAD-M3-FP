package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "RestartServlet", urlPatterns = "/restart")
public class RestartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(SessionKeys.PLAYER_NAME) == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        SessionKeys.startNewGame(session);
        resp.sendRedirect(req.getContextPath() + "/game");
    }
}