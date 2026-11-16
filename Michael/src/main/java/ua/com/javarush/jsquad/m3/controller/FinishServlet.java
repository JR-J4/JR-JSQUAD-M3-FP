package ua.com.javarush.jsquad.m3.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "FinishServlet", urlPatterns = "/finish")
public class FinishServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            if (session.getAttribute(SessionKeys.CURRENT_STEP_ID) != null) {
                session.setAttribute(SessionKeys.GAME_FINISHED, true);
            }
            session.removeAttribute(SessionKeys.CURRENT_STEP_ID);
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}