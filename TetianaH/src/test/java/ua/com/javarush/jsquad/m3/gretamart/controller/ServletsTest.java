package ua.com.javarush.jsquad.m3.gretamart.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.javarush.jsquad.m3.gretamart.model.Step;
import ua.com.javarush.jsquad.m3.gretamart.service.GameState;
import ua.com.javarush.jsquad.m3.gretamart.service.QuestService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ServletsTest {

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private final QuestService service = new QuestService();

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        when(req.getContextPath()).thenReturn("/tetianaH");
    }

    @Test
    void welcomeServletForwardsToWelcomePage() throws Exception {
        when(req.getRequestDispatcher("/welcome.jsp")).thenReturn(dispatcher);

        new WelcomeServlet().doGet(req, resp);

        verify(dispatcher).forward(req, resp);
    }

    @Test
    void startServletPutsNewGameIntoSessionAndRedirects() throws Exception {
        when(req.getParameter("name")).thenReturn("Грета");
        when(req.getSession()).thenReturn(session);

        new StartServlet().doPost(req, resp);

        verify(req).setCharacterEncoding("UTF-8");
        verify(session).setAttribute(eq(SessionKeys.GAME), argThat(o ->
                o instanceof GameState g && g.getPlayerName().equals("Грета")));
        verify(resp).sendRedirect("/tetianaH/game");
    }

    @Test
    void gameServletWithoutSessionRedirectsToWelcome() throws Exception {
        when(req.getSession(false)).thenReturn(null);

        new GameServlet().doGet(req, resp);

        verify(resp).sendRedirect("/tetianaH/");
        verify(req, never()).getRequestDispatcher(any());
    }

    @Test
    void gameServletGetShowsCurrentStep() throws Exception {
        GameState state = service.newGame("A");
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionKeys.GAME)).thenReturn(state);
        when(req.getRequestDispatcher("/game.jsp")).thenReturn(dispatcher);

        new GameServlet().doGet(req, resp);

        verify(req).setAttribute(eq("step"), argThat(o -> o instanceof Step s && s.id().equals("start")));
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void gameServletPostAppliesAnswer() throws Exception {
        GameState state = service.newGame("A");
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionKeys.GAME)).thenReturn(state);
        when(req.getParameter("answer")).thenReturn("1");

        new GameServlet().doPost(req, resp);

        assertEquals(1, state.getLosses());
        verify(resp).sendRedirect("/tetianaH/game");
    }

    @Test
    void gameServletPostIgnoresGarbageAnswer() throws Exception {
        GameState state = service.newGame("A");
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionKeys.GAME)).thenReturn(state);
        when(req.getParameter("answer")).thenReturn("abc");

        new GameServlet().doPost(req, resp);

        assertEquals("start", state.getCurrentStepId());
        verify(resp).sendRedirect("/tetianaH/game");
    }

    @Test
    void restartServletResetsStepButKeepsStats() throws Exception {
        GameState state = service.newGame("A");
        service.answer(state, 1);
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionKeys.GAME)).thenReturn(state);

        new RestartServlet().doPost(req, resp);

        assertEquals("start", state.getCurrentStepId());
        assertEquals(1, state.getGamesPlayed());
        verify(resp).sendRedirect("/tetianaH/game");
    }
}
